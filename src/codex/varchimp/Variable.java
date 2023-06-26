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
    
    public Class<T> getVariableType();
    public Object getSubject();
    public Pull<T> getPuller();
    public Push<T> getPusher();
    public void setLastAccessValue(T value);
    public T getLastAccessValue();
    
    public default String getVariableLabel() {
        return getPusher().getSetterName();
    }    
    public default T getVariableValue() {
        return getVariableValue(false);
    }
    public default T getVariableValue(boolean sneaky) {
        T value = getPuller().pull();
        if (!sneaky && valueChanged(value, getLastAccessValue())) {
            setLastAccessValue(value);
        }
        return value;
    }
    public default void setVariableValue(T value) {
        getPusher().push(value);
    }
    public default boolean variableChanged() {
        return !Variable.this.getVariableValue().equals(getLastAccessValue());
    }
    
    private static boolean valueChanged(Object value, Object prev) {
        return (value == null && prev != null)
            || (value != null && prev == null)
            || (value != null && prev != null && !value.equals(prev));
    }
    
}
