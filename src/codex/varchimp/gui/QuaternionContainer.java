/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp.gui;

import codex.varchimp.CombinedReference;
import com.jme3.math.Quaternion;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.SpringGridLayout;
import codex.varchimp.Variable;

/**
 * Displays GUI for editing {@code Quaternions}.
 * 
 * @author gary
 */
public class QuaternionContainer extends VariableContainer<Quaternion> {
    
    NumberScroller x, y, z;
    
    /**
     *
     * @param variable
     */
    public QuaternionContainer(Variable<Quaternion> variable) {
        super(variable);
    }
    
    /**
     *
     */
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
        z.setValueDisplay(new LimitedValueDisplay(5));
        setReference(new CombinedReference(
                x.getModel().createReference(),
                y.getModel().createReference(),
                z.getModel().createReference()));
    }

    /**
     *
     * @param value
     */
    @Override
    protected void pull(Quaternion value) {
        float[] angles = value.toAngles(new float[3]);
        x.getModel().setValue(angles[0]);
        y.getModel().setValue(angles[1]);
        z.getModel().setValue(angles[2]);
    }

    /**
     *
     * @return
     */
    @Override
    protected Quaternion push() {
        return new Quaternion().fromAngles(
                (float)x.getModel().getValue(),
                (float)y.getModel().getValue(),
                (float)z.getModel().getValue());
    }

    /**
     *
     * @return
     */
    @Override
    public Class getVariableType() {
        return Quaternion.class;
    }

    /**
     *
     * @param field
     * @return
     */
    @Override
    public VariableContainer create(Variable field) {
        return new QuaternionContainer(field);
    }
    
}
