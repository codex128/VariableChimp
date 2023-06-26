/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.fieldchimp;

import codex.fieldchimp.gui.FieldContainerFactory;
import codex.fieldchimp.gui.FieldContainer;
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

/**
 *
 * @author gary
 */
public class FieldChimpAppState extends BaseAppState {
    
    public static final String FIELD_USERDATA = "FieldChimp[field]";
    private static final Logger LOG = Logger.getLogger(FieldChimpAppState.class.getName());
    
    LinkedList<Field> fields = new LinkedList<>();
    LinkedList<Field> preInitFields = new LinkedList<>();
    LinkedList<FieldContainer> containers = new LinkedList<>();
    LinkedList<FieldContainerFactory> factories = new LinkedList<>();
    Container gui = new Container();
    Container fieldList = new Container();
    Vector2f windowSize;
    ColorRGBA background = new ColorRGBA(0f, 0f, 0f, .7f);
    boolean cursorLocked = false;
    boolean popupOpen = false;
    
    public FieldChimpAppState() {
        setEnabled(false);
    }
    
    @Override
    protected void initialize(Application app) {
        
        windowSize = new Vector2f(app.getContext().getSettings().getWidth(),
                app.getContext().getSettings().getHeight());
        
        gui.addChild(fieldList);
        gui.setLocalTranslation(0f, windowSize.y, 0f);
        fieldList.setLayout(new BoxLayout());
        
        for (Field f : preInitFields) {
            initField(f);
        }
        preInitFields.clear();
        
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
    
    private void initField(Field field) {
        FieldContainerFactory factory = getFactory(field);
        if (factory == null) {
            LOG.log(Level.WARNING, "Field has no corresponding factory!");
            return;
        }
        fields.add(field);
        FieldContainer container = factory.create(field);
        containers.add(container);
        container.initialize();
        fieldList.addChild(container);
        container.pullFieldValue();
        container.getReference().update();
    }
    private FieldContainerFactory getFactory(Field field) {
        for (FieldContainerFactory f : factories) {
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
        containers.stream().filter(f -> f.getField().fieldChanged()).forEach(f -> {
            f.pullFieldValue();
            f.getReference().update();
        });
    }
    
    public void register(Field field) {
        if (!fields.contains(field)) {
            if (isInitialized()) initField(field);
            else preInitFields.add(field);
        }
    }
    public void registerAll(Field... fields) {
        for (Field f : fields) {
            register(f);
        }
    }
    public boolean remove(Field field) {
        return fields.remove(field);
    }
    public void clear() {
        fields.clear();
    }
    
    public void registerFactory(FieldContainerFactory factory) {
        factories.add(factory);
    }
    public void registerAllFactories(FieldContainerFactory... factories) {
        for (FieldContainerFactory f : factories) {
            registerFactory(f);
        }
    }
    
}
