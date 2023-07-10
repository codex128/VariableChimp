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
import java.util.LinkedList;
import java.util.logging.Logger;
import java.util.logging.Level;
import codex.varchimp.gui.VariableContainerFactory;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.component.BoxLayout;
import com.simsilica.lemur.core.GuiMaterial;
import com.simsilica.lemur.core.UnshadedMaterialAdapter;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;
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
public class VarChimpAppState extends BaseAppState implements StateFunctionListener {
    
    private static final Logger LOG = Logger.getLogger(VarChimpAppState.class.getName());
    
    private LinkedList<Variable>
            variables = new LinkedList<>(),
            preInitVars = new LinkedList<>();
    private LinkedList<VariableContainer> containers = new LinkedList<>();
    private LinkedList<VariableContainerFactory> factories = new LinkedList<>();
    private LinkedList<CachedVariableGroup> caches = new LinkedList<>();
    private LinkedList<VarChimpListener> listeners = new LinkedList<>();
    private ViewPort[] vps;
    private Node node = new Node("varchimp-master-node");
    private Container gui = new Container();
    private Container varList = new Container();
    private BoxLayout varLayout;
    private Vector2f windowSize;
    private GuiMaterial backgroundMat;
    private String currentDefaultGroup = "varchimp-group";
    private boolean cursorLocked = false;
    private boolean popupOpen = false;
    
    /**
     *
     */
    public VarChimpAppState() {
        setEnabled(false);
    }
    
    /**
     *
     * @param app
     */
    @Override
    protected void initialize(Application app) {
        
        windowSize = new Vector2f(app.getContext().getSettings().getWidth(),
                app.getContext().getSettings().getHeight());
        
        if (app instanceof SimpleApplication) {
            vps = new ViewPort[]{((SimpleApplication)app).getGuiViewPort()};
        }
        else {
            LOG.log(Level.INFO, "VariableChimp is unable to find the GUI view port. Must be set manually.");
            vps = new ViewPort[0];
        }
        
        node.setLocalTranslation(0f, 0f, 20f);
        node.setCullHint(Spatial.CullHint.Never);
        node.setQueueBucket(RenderQueue.Bucket.Gui);
        node.attachChild(createBackgroundBlocker(-10f, new ColorRGBA(0f, 0f, 0f, .7f)));
        node.attachChild(gui);
        initGlobalButtons();
        gui.addChild(varList);
        gui.setLocalTranslation(0f, windowSize.y, 0f);
        varLayout = new BoxLayout();
        varList.setLayout(varLayout);
        
        preInitVars.stream().forEach(v -> initVariable(v));
        preInitVars.clear();
        
        InputMapper im = GuiGlobals.getInstance().getInputMapper();
        im.addStateListener(this, VarChimp.F_OPENWINDOW);
        im.activateGroup(VarChimp.ACTIVE_INPUT);
        
    }
    /**
     *
     * @param app
     */
    @Override
    protected void cleanup(Application app) {
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
    protected void onEnable() {
        openPopup();
        pullChanges();
        InputMapper im = GuiGlobals.getInstance().getInputMapper();
        im.activateGroup(VarChimp.PASSIVE_INPUT);
        cursorLocked = !GuiGlobals.getInstance().isCursorEventsEnabled();
        GuiGlobals.getInstance().setCursorEventsEnabled(true);
        listeners.stream().forEach(l -> l.onVarChimpEnabled());
    }
    /**
     *
     */
    @Override
    protected void onDisable() {
        listeners.stream().forEach(l -> l.onVarChimpDisabled());
        pushChanges();
        closePopup();
        InputMapper im = GuiGlobals.getInstance().getInputMapper();
        im.deactivateGroup(VarChimp.PASSIVE_INPUT);
        GuiGlobals.getInstance().setCursorEventsEnabled(!cursorLocked);
    }
    /**
     *
     * @param tpf
     */
    @Override
    public void update(float tpf) {
        node.updateLogicalState(tpf);
        node.updateGeometricState();
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
    
    private Geometry createBackgroundBlocker(float z, ColorRGBA color) {
        Geometry blocker = new Geometry("background-blocker", new Quad(windowSize.x, windowSize.y));        
        blocker.setMaterial(createBackgroundMaterial(color).getMaterial());
        blocker.setLocalTranslation(0f, 0f, z);
        return blocker;
    }
    private GuiMaterial createBackgroundMaterial(ColorRGBA color) {
        if (backgroundMat != null) {
            return backgroundMat;
        }
        backgroundMat = new UnshadedMaterialAdapter(new Material(getApplication().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md"));
        backgroundMat.setColor(color);
        backgroundMat.getMaterial().getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        backgroundMat.getMaterial().setTransparent(true);
        backgroundMat.getMaterial().getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        return backgroundMat;
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
    private void openPopup() {
        if (popupOpen) return;
        for (ViewPort v : vps) {
            v.attachScene(node);
        }
        popupOpen = true;
    }
    private void closePopup() {
        if (popupOpen) {
            for (ViewPort v : vps) {
                v.detachScene(node);
            }
            setPopupAsClosed();
        }
    }
    private void setPopupAsClosed() {
        popupOpen = false;
    }
    
    /**
     * Push changes from the GUI to the variable.
     */
    public void pushChanges() {
        containers.stream().filter(f -> f.getReference().update()).forEach(f -> {
            f.pushValue();
        });
    }
    /**
     * Pull changes from the GUI to the variable.
     * Actually pulls all values even if they haven't been changed, which is fine,
     * if a little inefficient.
     */
    public void pullChanges() {
        containers.stream().forEach(f -> {
            f.pullValue();
            f.getReference().update();
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
        LinkedList<Variable> remove = new LinkedList<>();
        for (Variable v : variables) {
            if (test.apply(v)) {
                remove.add(v);
            }
        }
        for (Variable v : remove) {
            cleanupVariable(v);
            remove.remove(v);
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
     * Sets the viewports that render the GUI.
     * @param vps 
     */
    public void setViewPorts(ViewPort... vps) {
        assert vps != null;
        if (this.vps != null && popupOpen) for (ViewPort v : this.vps) {
            v.detachScene(node);
        }
        this.vps = vps;
        if (popupOpen) for (ViewPort v : this.vps) {
            v.attachScene(node);
        }
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
     * Set the background color.
     * @param color 
     */
    public void setBackgroundColor(ColorRGBA color) {
        if (backgroundMat == null) createBackgroundMaterial(color);
        else backgroundMat.setColor(color);
    }
    /**
     * Set the z position of the gui scene.
     * <p>
     * Is useful to ensure that the gui is always rendered in front of all other gui elements
     * rendered in the same viewport.
     * @param z 
     */
    public void setZPosition(float z) {
        node.setLocalTranslation(0f, 0f, z);
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
    
    public void addListener(VarChimpListener listener) {
        listeners.add(listener);
    }
    public boolean removeListener(VarChimpListener listener) {
        return listeners.remove(listener);
    }
    public void clearAllListeners() {
        listeners.clear();
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
