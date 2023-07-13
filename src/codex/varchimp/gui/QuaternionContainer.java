/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp.gui;

import codex.varchimp.util.CombinedReference;
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

    NumberInput x, y, z;
    
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
        x = layout.addChild(0, 1, new NumberInput());
        x.setModel(VariableContainer.createDefaultModel(Float.class));
        layout.addChild(1, 0, new Label("y:"));
        y = layout.addChild(1, 1, new NumberInput());
        y.setModel(VariableContainer.createDefaultModel(Float.class));
        layout.addChild(2, 0, new Label("z:"));
        z = layout.addChild(2, 1, new NumberInput());
        z.setModel(VariableContainer.createDefaultModel(Float.class));
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
