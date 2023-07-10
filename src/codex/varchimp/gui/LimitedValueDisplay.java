/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp.gui;

import com.simsilica.lemur.RangedValueModel;

/**
 * Implementation of value display which limits the number of characters rendered.
 * 
 * @author gary
 */
public class LimitedValueDisplay implements ValueDisplay {
    
    int length = 5;
    
    /**
     *
     */
    public LimitedValueDisplay() {}

    /**
     *
     * @param length
     */
    public LimitedValueDisplay(int length) {
        this.length = length;
    }

    /**
     *
     * @param model
     * @return
     */
    @Override
    public String displayValue(RangedValueModel model) {
        String str = ""+model.getValue();
        if (str.length() > length) {
            str = str.substring(0, length);
        }
        return str;
    }
    
}
