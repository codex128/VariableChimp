/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp.gui;

import com.simsilica.lemur.RangedValueModel;
import codex.varchimp.Variable;

/**
 * Displays GUI for editing long values.
 * 
 * @author gary
 */
public class LongContainer extends VariableContainer<Long> {
    
    NumberScroller scroller;
    
    /**
     *
     * @param variable
     */
    public LongContainer(Variable<Long> variable) {
        super(variable);
    }
    
    @Override
    protected void initEditingGui() {
        scroller = editContainer.addChild(new NumberScroller());
        scroller.setModel(VariableContainer.createDefaultModel(Long.class));
        scroller.setValueDisplay((RangedValueModel model) -> ""+(long)model.getValue());
        setReference(scroller.getModel().createReference());
    }
    @Override
    protected void pull(Long value) {
        scroller.getModel().setValue(value);
    }
    @Override
    protected Long push() {
        return (long)scroller.getModel().getValue();
    }
    @Override
    public Class getVariableType() {
        return long.class;
    }
    @Override
    public VariableContainer create(Variable field) {
        return new LongContainer(field);
    }
    
}
