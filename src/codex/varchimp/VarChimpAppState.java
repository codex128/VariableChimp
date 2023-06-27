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
import com.simsilica.lemur.component.SpringGridLayout;
import java.io.File;
import java.util.function.Function;

/**
 *
 * @author gary
 */
public class VarChimpAppState extends BaseAppState {
    
    public static final String FIELD_USERDATA = "FieldChimp[field]";
    private static final Logger LOG = Logger.getLogger(VarChimpAppState.class.getName());
    
    LinkedList<Variable> variables = new LinkedList<>();
    LinkedList<Variable> preInitVars = new LinkedList<>();
    LinkedList<VariableContainer> containers = new LinkedList<>();
    LinkedList<VariableContainerFactory> factories = new LinkedList<>();
    Container gui = new Container();
    Container varList = new Container();
    SpringGridLayout varLayout;
    File layoutPrefs;
    Vector2f windowSize;
    ColorRGBA background = new ColorRGBA(0f, 0f, 0f, .7f);
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
        varLayout = new SpringGridLayout();
        varList.setLayout(varLayout);
        
        for (Variable f : preInitVars) {
            initVariable(f);
        }
        preInitVars.clear();
        
    }
    @Override
    protected void cleanup(Application app) {
        clear();
        containers.clear();
        factories.clear();
    }
    @Override
    protected void onEnable() {
        openPopup();
        pullChanges();
        cursorLocked = !GuiGlobals.getInstance().isCursorEventsEnabled();
        GuiGlobals.getInstance().setCursorEventsEnabled(true);
    }
    @Override
    protected void onDisable() {
        pushChanges();
        closePopup();
        GuiGlobals.getInstance().setCursorEventsEnabled(!cursorLocked);
    }
    @Override
    public void update(float tpf) {}
    
    private void initVariable(Variable variable) {
        VariableContainerFactory factory = getFactory(variable);
        if (factory == null) {
            LOG.log(Level.WARNING, "Variable has no corresponding factory!");
            return;
        }
        variables.add(variable);
        VariableContainer container = factory.create(variable);
        containers.add(container);
        container.initialize();
        varLayout.addChild(0, 0, container);
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
    
    public void pushChanges() {
        containers.stream().filter(f -> f.getReference().update()).forEach(f -> {
            f.pushFieldValue();
        });
    }
    public void pullChanges() {
        containers.stream().forEach(f -> {
            f.pullFieldValue();
            f.getReference().update();
        });
    }
    
    public void register(Variable variable) {
        if (!variables.contains(variable)) {
            if (isInitialized()) initVariable(variable);
            else preInitVars.add(variable);
        }
    }
    public void registerAll(Variable... variables) {
        for (Variable f : variables) {
            register(f);
        }
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
        removeAllMatching(v -> v.getVariableGroup().equals(group));
    }
    public void clear() {
        variables.clear();
    }
    
    private VariableContainer getVariableContainer(Variable variable) {
        return containers.stream().filter(c -> c.getVariable() == variable).findAny().orElse(null);
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
