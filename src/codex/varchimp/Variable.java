/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp;

import java.util.logging.Logger;

/**
 *
 * @author gary
 * @param <T>
 */
public interface Variable <T> {
    
    public static final Logger LOG = Logger.getLogger(Variable.class.getName());
    
    /**
     * The type of variable this pointer is tracking.
     * @return 
     */
    public Class<T> getVariableType();
    /**
     * The object which this tracked variable belongs to.
     * @return 
     */
    public Object getSubject();
    /**
     * Get Pull object.
     * @return 
     */
    public Pull<T> getPuller();
    /**
     * Get Push object.
     * @return 
     */
    public Push<T> getPusher();    
    /**
     * Set the group this variable is part of.
     * @param group 
     */
    public void setVariableGroup(String group);
    
    /**
     * Create a copy for the given subject.
     * @param subject
     * @return 
     */
    public Variable copy(Object subject);
    
    /**
     * Get string representing the name of this pointer.
     * @return 
     */
    public default String getVariableLabel() {
        return getPusher().getSetterName();
    }
    /**
     * Returns a string representing which group this variable belongs to.
     * Used generally used to group variables together that will be removed
     * in the same time.
     * @return 
     */
    public default String getVariableGroup() {
        return null;
    }
    /**
     * Pulls the tracked variable's value.
     * @return 
     */
    public default T getVariableValue() {
        return getVariableValue(false);
    }
    /**
     * Pulls the tracked variable's value.
     * @param sneaky if true, does not set the last access token used for detecting changes
     * @return 
     */
    public default T getVariableValue(boolean sneaky) {
        return getPuller().pull();
    }
    /**
     * Pushes the given value onto the tracked variable.
     * @param value 
     */
    public default void setVariableValue(T value) {
        getPusher().push(value);
    }
    
}
