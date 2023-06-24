/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.fieldchimp;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.simsilica.lemur.core.VersionedList;
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
    
    VersionedList<Field> fields = new VersionedList<>();
    LinkedList<FieldContainer> containers = new LinkedList<>();
    LinkedList<FieldContainerFactory> factories = new LinkedList<>();
    ViewPort vp;
    Node gui = new Node();
    
    public FieldChimpAppState() {}
    
    @Override
    protected void initialize(Application app) {}
    @Override
    protected void cleanup(Application app) {
        clear();
        containers.clear();
        factories.clear();
    }
    @Override
    protected void onEnable() {
        if (vp != null) {
            vp.attachScene(gui);
        }
        pullChanges();
    }
    @Override
    protected void onDisable() {
        pushChanges();
        if (vp != null) {
            vp.detachScene(gui);
        }
    }
    @Override
    public void update(float tpf) {
        if (vp != null) {
            gui.updateLogicalState(tpf);
            gui.updateGeometricState();
        }
    }
    
    private void initField(Field field) {
        FieldContainerFactory factory = getFactory(field);
        if (factory == null) {
            LOG.log(Level.WARNING, "Field has no corresponding factory!");
            return;
        }
        fields.add(field);
        FieldContainer container = factory.create(field);
        containers.add(container);
        gui.attachChild(container);
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
            initField(field);
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
    
    public void setViewPort(ViewPort vp) {
        if (isEnabled()) {
            if (this.vp != null) {
                this.vp.detachScene(gui);
            }
            vp.attachScene(gui);
        }
        this.vp = vp;
    }
    
}
