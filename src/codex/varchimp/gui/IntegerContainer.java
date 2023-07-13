/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp.gui;

import codex.varchimp.Variable;

/**
 * Displays GUI for editing int values.
 * 
 * @author gary
 */
public class IntegerContainer extends VariableContainer<Integer> {
    
    NumberInput input;
    
    /**
     *
     * @param field
     */
    public IntegerContainer(Variable<Integer> field) {
        super(field);
    }
    
    @Override
    public void initEditingGui() {
        input = addChild(new NumberInput());
        input.setModel(VariableContainer.createDefaultModel(Integer.class));
    }
    @Override
    protected void pull(Integer value) {
        input.getModel().setValue(value);
    }
    @Override
    protected Integer push() {
        return (int)input.getModel().getValue();
    }
    @Override
    public Class getVariableType() {
        return int.class;
    }
    @Override
    public VariableContainer create(Variable variable) {
        return new IntegerContainer(variable);
    }
    
}
