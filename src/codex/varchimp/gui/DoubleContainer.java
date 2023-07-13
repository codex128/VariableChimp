/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp.gui;

import codex.varchimp.Variable;

/**
 * Displays GUI for editing double values.
 * 
 * @author gary
 */
public class DoubleContainer extends VariableContainer<Double> {
    
    NumberInput input;
    
    /**
     *
     * @param variable
     */
    public DoubleContainer(Variable variable) {
        super(variable);
    }
    
    @Override
    protected void initEditingGui() {
        input = editContainer.addChild(new NumberInput());
        input.setModel(VariableContainer.createDefaultModel(Double.class));
        setReference(input.getModel().createReference());
    }
    @Override
    protected void pull(Double value) {
        input.getModel().setValue(value);
    }
    @Override
    protected Double push() {
        return input.getModel().getValue();
    }
    @Override
    public Class getVariableType() {
        return double.class;
    }
    @Override
    public VariableContainer create(Variable variable) {
        return new DoubleContainer(variable);
    }
    
}
