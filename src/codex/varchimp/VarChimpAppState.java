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
public class VarChimpAppState extends BaseAppState {
    
    public static final String FIELD_USERDATA = "FieldChimp[field]";
    private static final Logger LOG = Logger.getLogger(VarChimpAppState.class.getName());
    
    LinkedList<VariablePointer> variables = new LinkedList<>();
    LinkedList<VariablePointer> preInitVars = new LinkedList<>();
    LinkedList<VariableContainer> containers = new LinkedList<>();
    LinkedList<VariableContainerFactory> factories = new LinkedList<>();
    Container gui = new Container();
    Container varList = new Container();
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
        varList.setLayout(new BoxLayout());
        
        for (VariablePointer f : preInitVars) {
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
    
    private void initField(VariablePointer field) {
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
        container.getReference().update();
    }
    private VariableContainerFactory getFactory(VariablePointer field) {
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
    
    public void register(VariablePointer field) {
        if (!variables.contains(field)) {
            if (isInitialized()) initField(field);
            else preInitVars.add(field);
        }
    }
    public void registerAll(VariablePointer... fields) {
        for (VariablePointer f : fields) {
            register(f);
        }
    }
    public boolean remove(VariablePointer field) {
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
