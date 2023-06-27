/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp.gui;

import codex.varchimp.Variable;

/**
 *
 * @author gary
 */
public class DoubleContainer extends VariableContainer<Double> {
    
    NumberScroller scroller;
    
    public DoubleContainer(Variable variable) {
        super(variable);
    }
    
    @Override
    protected void initEditingGui() {
        scroller = editContainer.addChild(new NumberScroller());
        scroller.setModel(VariableContainer.createDefaultModel(Double.class));
        setReference(scroller.getModel().createReference());
    }
    @Override
    protected void pull(Double value) {
        scroller.getModel().setValue(value);
    }
    @Override
    protected Double push() {
        return scroller.getModel().getValue();
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
