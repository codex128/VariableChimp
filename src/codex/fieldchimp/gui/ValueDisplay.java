/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.fieldchimp.gui;

import com.simsilica.lemur.RangedValueModel;

/**
 *
 * @author gary
 */
public interface ValueDisplay {
    
    public static final ValueDisplay
            DEFAULT = (RangedValueModel model) -> ""+model.getValue();
    
    public String displayValue(RangedValueModel model);
    
}
