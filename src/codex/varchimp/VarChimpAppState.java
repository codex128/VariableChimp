/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp;

import codex.varchimp.gui.VariableContainer;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.event.PopupState;
import java.util.LinkedList;
import java.util.logging.Logger;
import java.util.logging.Level;
import codex.varchimp.gui.VariableContainerFactory;
import com.simsilica.lemur.component.BoxLayout;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 *
 * @author gary
 */
public class VarChimpAppState extends BaseAppState implements StateFunctionListener {
    
    public static final String FIELD_USERDATA = "FieldChimp[field]";
    private static final Logger LOG = Logger.getLogger(VarChimpAppState.class.getName());
    
    LinkedList<Variable> variables = new LinkedList<>();
    LinkedList<Variable> preInitVars = new LinkedList<>();
    LinkedList<VariableContainer> containers = new LinkedList<>();
    LinkedList<VariableContainerFactory> factories = new LinkedList<>();
    LinkedList<CachedVariableGroup> caches = new LinkedList<>();
    Container gui = new Container();
    Container varList = new Container();
    BoxLayout varLayout;
    Vector2f windowSize;
    ColorRGBA background = new ColorRGBA(0f, 0f, 0f, .7f);
    String currentDefaultGroup;
    boolean cursorLocked = false;
    boolean popupOpen = false;
    
    public VarChimpAppState() {
        setEnabled(false);
    }
    
    @Override
    protected void initialize(Application app) {
        
        windowSize = new Vector2f(app.getContext().getSettings().getWidth(),
                app.getContext().getSettings().getHeight());
        
        gui.addChild(varList);
        gui.setLocalTranslation(0f, windowSize.y, 0f);
        varLayout = new BoxLayout();
        varList.setLayout(varLayout);
        
        for (Variable f : preInitVars) {
            initVariable(f, true);
        }
        preInitVars.clear();
        
        InputMapper im = GuiGlobals.getInstance().getInputMapper();
        im.addStateListener(this, VarChimp.F_OPENWINDOW);
        im.activateGroup(VarChimp.ACTIVE_INPUT);
        
    }
    @Override
    protected void cleanup(Application app) {
        clear();
        containers.clear();
        factories.clear();
        clearAllCaches();
        InputMapper im = GuiGlobals.getInstance().getInputMapper();
        im.removeStateListener(this, VarChimp.F_OPENWINDOW);
        im.deactivateGroup(VarChimp.ACTIVE_INPUT);
    }
    @Override
    protected void onEnable() {
        openPopup();
        pullChanges();
        InputMapper im = GuiGlobals.getInstance().getInputMapper();
        im.activateGroup(VarChimp.PASSIVE_INPUT);
        cursorLocked = !GuiGlobals.getInstance().isCursorEventsEnabled();
        GuiGlobals.getInstance().setCursorEventsEnabled(true);
    }
    @Override
    protected void onDisable() {
        pushChanges();
        closePopup();
        InputMapper im = GuiGlobals.getInstance().getInputMapper();
        im.deactivateGroup(VarChimp.PASSIVE_INPUT);
        GuiGlobals.getInstance().setCursorEventsEnabled(!cursorLocked);
    }
    @Override
    public void update(float tpf) {}
    @Override
    public void valueChanged(FunctionId func, InputState value, double tpf) {
        if (func == VarChimp.F_OPENWINDOW && value != InputState.Off) {
            setEnabled(!isEnabled());
        }
    }
    
    private void initVariable(Variable variable, boolean pull) {
        VariableContainerFactory factory = getFactory(variable);
        if (factory == null) {
            LOG.log(Level.WARNING, "Variable has no corresponding factory!");
            return;
        }
        if (currentDefaultGroup != null && variable.getVariableGroup() == null) {
            variable.setVariableGroup(currentDefaultGroup);
        }
        variables.add(variable);
        VariableContainer container = factory.create(variable);
        containers.add(container);
        container.initialize(pull);
        varLayout.addChild(container);
        container.getReference().update();
    }
    private void cleanupVariable(Variable variable) {
        if (!variables.remove(variable)) return;
        VariableContainer container = getVariableContainer(variable);
        if (!containers.remove(container)) return;
        varList.detachChild(container);
    }
    private VariableContainerFactory getFactory(Variable variable) {
        for (VariableContainerFactory f : factories) {
            if (f.accept(variable)) {
                return f;
            }
        }
        return null;
    }
    
    private void openPopup() {
        if (popupOpen) return;
        getState(PopupState.class).showModalPopup(gui, (PopupState source) -> {
            setPopupAsClosed();
            setEnabled(false);
        }, background);
        popupOpen = true;
    }
    private void closePopup() {
        if (popupOpen) {
            getState(PopupState.class).closePopup(gui);
            setPopupAsClosed();
        }
    }
    private void setPopupAsClosed() {
        popupOpen = false;
    }
    public VariableContainer getVariableContainer(Variable variable) {
        return containers.stream().filter(c -> c.getVariable() == variable).findAny().orElse(null);
    }
    
    public void pushChanges() {
        containers.stream().filter(f -> f.getReference().update()).forEach(f -> {
            f.pushValue();
        });
    }
    public void pullChanges() {
        containers.stream().forEach(f -> {
            f.pullValue();
            f.getReference().update();
        });
    }
    
    public void register(Variable variable) {
        register(variable, true);
    }
    public void register(Variable variable, boolean pull) {
        if (!variables.contains(variable)) {
            if (isInitialized()) initVariable(variable, pull);
            else preInitVars.add(variable);
        }
    }
    public void registerAll(Variable... variables) {
        for (Variable f : variables) {
            register(f, true);
        }
    } 
    
    public Stream<Variable> streamGroup(String group) {
        return variables.stream().filter(v -> group.equals(v.getVariableGroup()));
    }
    /**
     * Create and store a cache of all variables in the given group.
     * Removes all variables from the update list in the cache by default, because that is
     * probably the best practice. Otherwise, there might be some collision
     * between variables that are basically the same.
     * @param group
     * @return created cache
     */
    public CachedVariableGroup cacheGroup(String group) {
        return cacheGroup(group, true);
    }
    /**
     * Create and store a cache of all variables in the given group.
     * @param group
     * @param removeOnCache if true, removes all variables from the update list that are part of the cache.
     * It is highly suggested to do this.
     * @return created cache
     */
    public CachedVariableGroup cacheGroup(String group, boolean removeOnCache) {
        CachedVariableGroup c = new CachedVariableGroup();
        c.addAll(streamGroup(group));
        caches.addLast(c);
        if (removeOnCache) removeGroup(group);
        return c;
    }
    /**
     * Returns the first cache of the given group.
     * Or null if none is found.
     * @param group
     * @return 
     */
    public CachedVariableGroup getCache(String group) {
        return caches.stream().filter(c -> group.equals(c.getGroupName())).findAny().orElse(null);
    }
    /**
     * Applies all the variables in the cache (of the given group).
     * Removes the cache by default, so that all variables stored in the cache are lost.
     * This is the best practice, since the cache now stores basically redundent information
     * and there might be collision between similar variables.
     * @param group
     * @param subject
     * @return the applied cache
     */
    public CachedVariableGroup applyCache(String group, Object subject) {
        return applyCache(group, subject, true);
    }
    /**
     * Applies all the variables in the cache (of the given group).
     * @param group
     * @param subject
     * @param removeOnApply removes the cache when applied
     * @return the applied cache
     */
    public CachedVariableGroup applyCache(String group, Object subject, boolean removeOnApply) {
        CachedVariableGroup c = getCache(group);
        c.applyTo(subject);
        if (removeOnApply) removeCache(c);
        return c;
    }
    /**
     * Apply the cache (of the group) only if it exists.
     * Otherwise, runs the Runnable. Removes cache when applied by default.
     * @param group
     * @param subject
     * @param run
     * @return 
     */
    public CachedVariableGroup applyCacheOrElse(String group, Object subject, Runnable run) {
        return applyCacheOrElse(group, subject, true, run);
    }
    /**
     * Apply the cache (of the group) only if it exists.Otherwise, runs the Runnable.
     * @param group
     * @param subject
     * @param removeOnApply
     * @param run
     * @return 
     */
    public CachedVariableGroup applyCacheOrElse(String group, Object subject, boolean removeOnApply, Runnable run) {
        CachedVariableGroup c = getCache(group);
        if (c != null) {
            c.applyTo(subject);
            if (removeOnApply) removeCache(c);
            return c;
        }
        run.run();
        return null;
    }
    /**
     * Removes the cache (of the given group).
     * All variables in the cache will be lost, unless they were (for some reason)
     * not removed from the update list when the cache was created.
     * @param group
     * @return 
     */
    public CachedVariableGroup removeCache(String group) {
        CachedVariableGroup c = getCache(group);
        if (c != null) removeCache(c);
        return c;
    }
    /**
     * Removes the given cache.
     * All variables in the cache will be lost, unless they were (for some reason)
     * not removed from the update list when the cache was created.
     * @param cache
     * @return 
     */
    public boolean removeCache(CachedVariableGroup cache) {
        if (caches.remove(cache)) {
            cache.clear();
            return true;
        }
        return false;
    }
    /**
     * Removes all caches.
     */
    public void clearAllCaches() {
        caches.stream().forEach(c -> c.clear());
        caches.clear();
    }
    
    public boolean remove(Variable variable) {
        return variables.remove(variable);
    }
    public void removeAllMatching(Function<Variable, Boolean> test) {
        LinkedList<Variable> remove = new LinkedList<>();
        for (Variable v : variables) {
            if (test.apply(v)) {
                remove.add(v);
            }
        }
        for (Variable v : remove) {
            cleanupVariable(v);
        }
    }
    public void removeGroup(String group) {
        removeAllMatching(v -> group.equals(v.getVariableGroup()));
    }
    public void clear() {
        variables.clear();
    }
    
    public void setDefaultGroup(String group) {
        currentDefaultGroup = group;
    }
    
    public void registerFactory(VariableContainerFactory factory) {
        factories.add(factory);
    }
    public void registerAllFactories(VariableContainerFactory... factories) {
        for (VariableContainerFactory f : factories) {
            registerFactory(f);
        }
    }
    
}
