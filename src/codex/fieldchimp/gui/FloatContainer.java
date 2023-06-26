/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.fieldchimp.gui;

import codex.fieldchimp.Field;
import com.simsilica.lemur.DefaultRangedValueModel;

/**
 *
 * @author gary
 */
public class FloatContainer extends FieldContainer<Float> {
    
    NumberScroller scroller;
    
    public FloatContainer(Field field) {
        super(field);
    }
    
    @Override
    protected void initEditingGui() {
        scroller = editContainer.addChild(new NumberScroller());
        scroller.setModel(new DefaultRangedValueModel(-Float.MAX_VALUE, Float.MAX_VALUE, 0.0));
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
    public Class<Float> getFieldType() {
        return float.class;
    }
    @Override
    public FieldContainer create(Field field) {
        return new FloatContainer(field);
    }
    
}
