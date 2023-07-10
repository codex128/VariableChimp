/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp.gui;

import com.simsilica.lemur.RangedValueModel;
import codex.varchimp.Variable;

/**
 * Displays GUI for editing int values.
 * 
 * @author gary
 */
public class IntegerContainer extends VariableContainer<Integer> {
    
    NumberScroller scroller;
    
    /**
     *
     * @param field
     */
    public IntegerContainer(Variable<Integer> field) {
        super(field);
    }
    
    @Override
    public void initEditingGui() {
        scroller = addChild(new NumberScroller());
        scroller.setModel(VariableContainer.createDefaultModel(Integer.class));
        scroller.setValueDisplay((RangedValueModel model) -> ""+(int)model.getValue());
    }
    @Override
    protected void pull(Integer value) {
        scroller.getModel().setValue(value);
    }
    @Override
    protected Integer push() {
        return (int)scroller.getModel().getValue();
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
