/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.varchimp.gui;

import codex.varchimp.VariablePointer;

/**
 *
 * @author gary
 * @param <T>
 */
public interface VariableContainerFactory <T> {
    
    public Class<T> getVariableType();    
    public abstract VariableContainer create(VariablePointer field);
    
    public default boolean accept(VariablePointer variable) {
        return getVariableType().isAssignableFrom(variable.getVariableType());
    }    
    
}
