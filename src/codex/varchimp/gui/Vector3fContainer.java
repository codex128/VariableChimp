/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp.gui;

import codex.varchimp.CombinedReference;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.SpringGridLayout;
import codex.varchimp.Variable;

/**
 * Displays GUI for editing {@code Vector3f}.
 * 
 * @author gary
 */
public class Vector3fContainer extends VariableContainer<Vector3f> {
    
    NumberScroller x, y, z;
    
    /**
     *
     * @param variable
     */
    public Vector3fContainer(Variable<Vector3f> variable) {
        super(variable);
    }
    
    @Override
    protected void initEditingGui() {
        SpringGridLayout layout = new SpringGridLayout();
        editContainer.setLayout(layout);
        layout.addChild(0, 0, new Label("x:"));
        x = layout.addChild(0, 1, new NumberScroller());    
        x.setModel(VariableContainer.createDefaultModel(Float.class));
        x.setValueDisplay(new LimitedValueDisplay(5));
        layout.addChild(1, 0, new Label("y:"));
        y = layout.addChild(1, 1, new NumberScroller());        
        y.setModel(VariableContainer.createDefaultModel(Float.class));
        y.setValueDisplay(new LimitedValueDisplay(5));
        layout.addChild(2, 0, new Label("z:"));
        z = layout.addChild(2, 1, new NumberScroller());
        z.setModel(VariableContainer.createDefaultModel(Float.class));
        y.setValueDisplay(new LimitedValueDisplay(5));
        setReference(new CombinedReference(
                x.getModel().createReference(),
                y.getModel().createReference(),
                z.getModel().createReference()));
    }
    @Override
    protected void pull(Vector3f value) {
        x.getModel().setValue(value.x);
        y.getModel().setValue(value.y);
        z.getModel().setValue(value.z);
    }
    @Override
    protected Vector3f push() {
        Vector3f value = new Vector3f();
        value.x = (float)x.getModel().getValue();
        value.y = (float)y.getModel().getValue();
        value.z = (float)z.getModel().getValue();
        return value;
    }
    @Override
    public Class getVariableType() {
        return Vector3f.class;
    }
    @Override
    public VariableContainer create(Variable variable) {
        return new Vector3fContainer(variable);
    }
    
}
