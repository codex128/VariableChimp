/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp;

import codex.varchimp.gui.GuiDisplayState;
import codex.varchimp.gui.VariableContainer;
import com.jme3.app.Application;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import java.util.LinkedList;
import java.util.logging.Logger;
import java.util.logging.Level;
import codex.varchimp.gui.VariableContainerFactory;
import com.jme3.scene.Node;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.component.BoxLayout;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * This is the main management class for VariableChimp.
 * 
 * <p>A majority of VariableChimp actions are accessible by or through
 * this class. To access this class, use {@code VarChimp.get()} after
 * initializing VariableChimp.
 * 
 * @author gary
 */
public class VariableManipulatorState extends VarChimpAppState implements StateFunctionListener {
    
    private static final Logger LOG = Logger.getLogger(VariableManipulatorState.class.getName());
    
    private LinkedList<Variable>
            variables = new LinkedList<>(),
            preInitVars = new LinkedList<>();
    private LinkedList<VariableContainer> containers = new LinkedList<>();
    private LinkedList<VariableContainerFactory> factories = new LinkedList<>();
    private LinkedList<CachedVariableGroup> caches = new LinkedList<>();
    private GuiDisplayState guiRoot;
    private Node scene = new Node("varchimp[varManipulationScene]");
    private Container gui = new Container();
    private Container varList = new Container();
    private BoxLayout varLayout;
    private String currentDefaultGroup = "varchimp-group";
    
    /**
     *
     */
    public VariableManipulatorState() {
        setEnabled(false);
    }
    
    /**
     *
     * @param app
     */
    @Override
    protected void initialize(Application app) {
        
        guiRoot = VarChimp.getGuiRoot();
        
        scene.setLocalTranslation(0f, 0f, 0f);
        scene.attachChild(gui);
        initGlobalButtons();
        varLayout = new BoxLayout();
        gui.addChild(varList).setLayout(varLayout);
        gui.setLocalTranslation(0f, guiRoot.getWindowSize().y, 0f);
        
        preInitVars.stream().forEach(v -> initVariable(v));
        preInitVars.clear();
        preInitVars = null;
        
        InputMapper im = GuiGlobals.getInstance().getInputMapper();
        im.addStateListener(this, VarChimp.F_OPENWINDOW);
        im.activateGroup(VarChimp.ACTIVE_INPUT);
        
    }
    /**
     *
     * @param app
     */
    @Override
    protected void stateCleanup(Application app) {
        clear();
        containers.clear();
        factories.clear();
        clearAllCaches();
        clearAllListeners();
        InputMapper im = GuiGlobals.getInstance().getInputMapper();
        im.removeStateListener(this, VarChimp.F_OPENWINDOW);
        im.deactivateGroup(VarChimp.ACTIVE_INPUT);
    }
    /**
     *
     */
    @Override
    protected void stateEnabled() {
        guiRoot.enableGui();
        guiRoot.getGuiRoot().attachChild(scene);
        pullChanges();
        GuiGlobals.getInstance().requestCursorEnabled(this);
        GuiGlobals.getInstance().requestFocus(scene);
    }
    /**
     *
     */
    @Override
    protected void stateDisabled() {
        scene.removeFromParent();
        guiRoot.releaseGui();
        GuiGlobals.getInstance().releaseCursorEnabled(this);
    }
    /**
     *
     * @param func
     * @param value
     * @param tpf
     */
    @Override
    public void valueChanged(FunctionId func, InputState value, double tpf) {
        if (func == VarChimp.F_OPENWINDOW && value != InputState.Off) {
            setEnabled(!isEnabled());
        }
    }
    
    private void initVariable(Variable variable) {
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
        container.initialize();
        varLayout.addChild(container);
        container.getReference().update();
    }
    private void cleanupVariable(Variable variable) {
        VariableContainer container = getVariableContainer(variable);
        if (container == null || !containers.remove(container)) return;
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
    private void initGlobalButtons() {
        Container buttons = gui.addChild(new Container());
        BoxLayout layout = new BoxLayout(Axis.X, FillMode.Even);
        buttons.setLayout(layout);
        Button push = layout.addChild(new Button("Push All"));
        push.addClickCommands(new PushPullCommand(true));
        Button pull = layout.addChild(new Button("Pull All"));
        pull.addClickCommands(new PushPullCommand(false));
    }
    
    /**
     * Push changes from the GUI to the variable.
     */
    public void pushChanges() {
        containers.stream().filter(v -> v.getReference().update()).forEach(v -> {
            v.pushValue();
        });
    }
    /**
     * Pull values from the variable to the GUI.
     * <p>Pulls all values regardless if they have been changed or not.
     * Tracking variable changes was considered far more inefficient and clunky.
     */
    public void pullChanges() {
        containers.stream().forEach(v -> {
            v.pullValue();
            v.getReference().update();
        });
    }
    
    /**
     * Register the given variable if it is not currently registered already.
     * @param variable 
     */
    public void register(Variable variable) {
        if (!variables.contains(variable)) {
            if (isInitialized()) initVariable(variable);
            else preInitVars.add(variable);
        }
    }
    /**
     * Register all given variables if they are not currently registered already.
     * @param variables 
     */
    public void registerAll(Variable... variables) {
        for (Variable f : variables) {
            register(f);
        }
    }
    /**
     * Returns a stream containing all members of the given group.
     * @param group
     * @return 
     */
    public Stream<Variable> streamGroup(String group) {
        return variables.stream().filter(v -> group.equals(v.getVariableGroup()));
    }
    /**
     * Removes (unregisters) the variable.
     * @param variable
     * @return true if the variable was previously registered
     */
    public boolean remove(Variable variable) {
        if (variables.remove(variable)) {
            cleanupVariable(variable);
            return true;
        }
        return false;
    }
    /**
     * Removes all variables satisfying the Function.
     * @param test 
     */
    public void removeAllMatching(Function<Variable, Boolean> test) {
        for (Iterator<Variable> it = variables.iterator(); it.hasNext();) {
            Variable v = it.next();
            if (test.apply(v)) {
                cleanupVariable(v);
                it.remove();
            }
        }
    }
    /**
     * Removes all members of the group.
     * @param group 
     */
    public void removeGroup(String group) {
        removeAllMatching(v -> group.equals(v.getVariableGroup()));
    }
    /**
     * Clears all variables.
     */
    public void clear() {
        variables.stream().forEach(v -> cleanupVariable(v));
        variables.clear();
    }
    
    /**
     * Adds the given cache to the cache list.
     * @param cache 
     */
    public void addCache(CachedVariableGroup cache) {
        
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
     * Removes each cache corresponding with each group name.
     * @param groups 
     */
    public void removeCaches(String... groups) {
        for (String g : groups) {
            removeCache(g);
        }
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
    
    /**
     * Sets the default group all variables with no group are added to
     * on registration.
     * @param group 
     */
    public void setDefaultGroup(String group) {
        currentDefaultGroup = group;
    }
    /**
     * Set the z position of the gui scene.
     * <p>
     * Is useful to ensure that the gui is always rendered in front of all other gui elements
     * rendered in the same viewport.
     * @param z 
     */
    public void setZPosition(float z) {
        scene.setLocalTranslation(0f, 0f, z);
    }
    
    /**
     * Fetch the variable container pertaining to the given variable.
     * @param variable
     * @return 
     */
    public VariableContainer getVariableContainer(Variable variable) {
        return containers.stream().filter(c -> c.getVariable() == variable).findAny().orElse(null);
    }
    /**
     * Get the default group which all variables with no group (on registration) are added to.
     * @return 
     */
    public String getDefaultGroup() {
        return currentDefaultGroup;
    }
    /**
     * Get the z position of the gui scene.
     * @return 
     */
    public float getZPosition() {
        return scene.getLocalTranslation().z;
    }
    
    /**
     * Registers the container factory at the end of the factory queue.
     * @param factory 
     */
    public void registerFactory(VariableContainerFactory factory) {
        factories.add(factory);
    }
    /**
     * Registers all given container factories at the end of the factory queue.
     * @param factories 
     */
    public void registerAllFactories(VariableContainerFactory... factories) {
        for (VariableContainerFactory f : factories) {
            registerFactory(f);
        }
    }
    
    
    private final class PushPullCommand implements Command<Button> {        
        private boolean push;   
        public PushPullCommand(boolean push) {
            this.push = push;
        }        
        @Override
        public void execute(Button source) {
            if (push) pushChanges();
            else pullChanges();
        }        
    }
    
}
