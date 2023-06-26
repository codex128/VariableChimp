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
import com.simsilica.lemur.component.BoxLayout;
import com.simsilica.lemur.event.PopupState;
import java.util.LinkedList;
import java.util.logging.Logger;
import java.util.logging.Level;
import codex.varchimp.gui.VariableContainerFactory;

/**
 *
 * @author gary
 */
public class VariableChimpAppState extends BaseAppState {
    
    public static final String FIELD_USERDATA = "FieldChimp[field]";
    private static final Logger LOG = Logger.getLogger(VariableChimpAppState.class.getName());
    
    LinkedList<Variable> variables = new LinkedList<>();
    LinkedList<Variable> preInitVars = new LinkedList<>();
    LinkedList<VariableContainer> containers = new LinkedList<>();
    LinkedList<VariableContainerFactory> factories = new LinkedList<>();
    Container gui = new Container();
    Container varList = new Container();
    Vector2f windowSize;
    ColorRGBA background = new ColorRGBA(0f, 0f, 0f, .7f);
    boolean cursorLocked = false;
    boolean popupOpen = false;
    
    public VariableChimpAppState() {
        setEnabled(false);
    }
    
    @Override
    protected void initialize(Application app) {
        
        windowSize = new Vector2f(app.getContext().getSettings().getWidth(),
                app.getContext().getSettings().getHeight());
        
        gui.addChild(varList);
        gui.setLocalTranslation(0f, windowSize.y, 0f);
        varList.setLayout(new BoxLayout());
        
        for (Variable f : preInitVars) {
            initField(f);
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
    
    private void initField(Variable field) {
        VariableContainerFactory factory = getFactory(field);
        if (factory == null) {
            LOG.log(Level.WARNING, "Field has no corresponding factory!");
            return;
        }
        variables.add(field);
        VariableContainer container = factory.create(field);
        containers.add(container);
        container.initialize();
        varList.addChild(container);
        container.pullFieldValue();
        container.getReference().update();
    }
    private VariableContainerFactory getFactory(Variable field) {
        for (VariableContainerFactory f : factories) {
            if (f.accept(field)) {
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
        containers.stream().filter(f -> f.getReference().needsUpdate()).forEach(f -> {
            f.pushFieldValue();
            f.getReference().update();
        });
    }
    public void pullChanges() {
        containers.stream().filter(f -> f.getVariable().variableChanged()).forEach(f -> {
            f.pullFieldValue();
            f.getReference().update();
        });
    }
    
    public void register(Variable field) {
        if (!variables.contains(field)) {
            if (isInitialized()) initField(field);
            else preInitVars.add(field);
        }
    }
    public void registerAll(Variable... fields) {
        for (Variable f : fields) {
            register(f);
        }
    }
    public boolean remove(Variable field) {
        return variables.remove(field);
    }
    public void clear() {
        variables.clear();
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
