/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp.gui;

import codex.varchimp.CombinedVersionedReference;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.DefaultRangedValueModel;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.SpringGridLayout;
import codex.varchimp.VariablePointer;

/**
 *
 * @author gary
 */
public class Vector3fContainer extends VariableContainer<Vector3f> {
    
    NumberScroller x, y, z;
    
    public Vector3fContainer(VariablePointer<Vector3f> variable) {
        super(variable);
    }
    
    @Override
    protected void initEditingGui() {
        SpringGridLayout layout = new SpringGridLayout();
        editContainer.setLayout(layout);
        layout.addChild(0, 0, new Label("x:"));
        x = layout.addChild(0, 1, new NumberScroller());    
        x.setModel(new DefaultRangedValueModel(-Float.MAX_VALUE, Float.MAX_VALUE, 0.0));
        x.setValueDisplay(new LimitedValueDisplay(5));
        layout.addChild(1, 0, new Label("y:"));
        y = layout.addChild(1, 1, new NumberScroller());        
        y.setModel(new DefaultRangedValueModel(-Float.MAX_VALUE, Float.MAX_VALUE, 0.0));
        y.setValueDisplay(new LimitedValueDisplay(5));
        layout.addChild(2, 0, new Label("z:"));
        z = layout.addChild(2, 1, new NumberScroller());
        z.setModel(new DefaultRangedValueModel(-Float.MAX_VALUE, Float.MAX_VALUE, 0.0));
        y.setValueDisplay(new LimitedValueDisplay(5));
        setReference(new CombinedVersionedReference(
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
    public VariableContainer create(VariablePointer variable) {
        return new Vector3fContainer(variable);
    }
    
}
