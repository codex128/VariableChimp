/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp.gui;

import com.simsilica.lemur.RangedValueModel;

/**
 * Interface for rendering a {@code RangedValueModel} to a String.
 * 
 * @author gary
 */
public interface ValueDisplay {
    
    /**
     *
     */
    public static final ValueDisplay
            DEFAULT = (RangedValueModel model) -> ""+model.getValue();
    
    /**
     *
     * @param model
     * @return
     */
    public String displayValue(RangedValueModel model);
    
}
