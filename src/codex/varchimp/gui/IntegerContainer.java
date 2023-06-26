/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp.gui;

import com.simsilica.lemur.DefaultRangedValueModel;
import com.simsilica.lemur.RangedValueModel;
import codex.varchimp.VariablePointer;

/**
 *
 * @author gary
 */
public class IntegerContainer extends VariableContainer<Integer> {
    
    NumberScroller scroller;
    
    public IntegerContainer(VariablePointer<Integer> field) {
        super(field);
    }
    
    @Override
    public void initEditingGui() {
        scroller = addChild(new NumberScroller());
        scroller.setModel(new DefaultRangedValueModel(Integer.MIN_VALUE, Integer.MAX_VALUE, 0));
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
    public VariableContainer create(VariablePointer variable) {
        return new IntegerContainer(variable);
    }
    
}
