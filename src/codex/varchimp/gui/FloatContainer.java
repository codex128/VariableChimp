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
public class FloatContainer extends VariableContainer<Float> {
    
    NumberScroller scroller;
    
    public FloatContainer(Variable<Float> field) {
        super(field);
    }
    
    @Override
    protected void initEditingGui() {
        scroller = editContainer.addChild(new NumberScroller());
        scroller.setModel(VariableContainer.createDefaultModel(Float.class));
        reference = scroller.getModel().createReference();
    }
    @Override
    protected void pull(Float value) {
        scroller.getModel().setValue(value);
    }
    @Override
    protected Float push() {
        return (float)scroller.getModel().getValue();
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
