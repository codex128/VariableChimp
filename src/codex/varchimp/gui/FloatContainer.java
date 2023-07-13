/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp.gui;

import codex.varchimp.Variable;

/**
 * Displays GUI for editing float values.
 * 
 * @author gary
 */
public class FloatContainer extends VariableContainer<Float> {
    
    NumberInput input;
    
    /**
     *
     * @param field
     */
    public FloatContainer(Variable<Float> field) {
        super(field);
    }
    
    @Override
    protected void initEditingGui() {
        input = editContainer.addChild(new NumberInput());
        input.setModel(VariableContainer.createDefaultModel(Float.class));
        reference = input.getModel().createReference();
    }
    @Override
    protected void pull(Float value) {
        input.getModel().setValue(value);
    }
    @Override
    protected Float push() {
        return (float)input.getModel().getValue();
    }
    @Override
    public Class<Float> getVariableType() {
        return float.class;
    }
    @Override
    public VariableContainer create(Variable variable) {
        return new FloatContainer(variable);
    }
    
}
