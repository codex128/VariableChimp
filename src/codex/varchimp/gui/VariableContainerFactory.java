/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.varchimp.gui;

import codex.varchimp.Variable;

/**
 * Interface for creating {@code VariableContainers} when requested.
 * 
 * @author gary
 * @param <T>
 */
public interface VariableContainerFactory <T> {
    
    /**
     * Get the variable type this factory makes containers for.
     * @return 
     */
    public Class<T> getVariableType();
    /**
     * Creates a new VariableContainer.
     * @param field
     * @return 
     */
    public abstract VariableContainer create(Variable field);
    
    /**
     * Returns true if this factory agrees to create a container for the variable.
     * @param variable
     * @return 
     */
    public default boolean accept(Variable variable) {
        return getVariableType().isAssignableFrom(variable.getVariableType());
    }    
    
}
