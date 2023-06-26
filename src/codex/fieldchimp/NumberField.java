/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.fieldchimp;

import com.simsilica.lemur.DefaultRangedValueModel;
import com.simsilica.lemur.RangedValueModel;

/**
 *
 * @author gary
 * @param <T>
 */
public class NumberField <T extends Number> extends AbstractField<T> {
    
    public static final RangedValueModel
            DEFAULT_RANGE = new DefaultRangedValueModel(-Double.MAX_VALUE, Double.MAX_VALUE, 0.0);
    
    private final Class<T> type;
    private RangedValueModel model = DEFAULT_RANGE;
    
    public NumberField(Object subject, Class<T> type, Pull<T> getter, Push<T> setter) {
        super(subject, getter, setter);
        this.type = type;
    }
    
    @Override
    public Class<T> getFieldType() {
        return type;
    }
    
    public void setModel(RangedValueModel model) {
        this.model = model;
    }
    public RangedValueModel getModel() {
        return model;
    }
    
}
