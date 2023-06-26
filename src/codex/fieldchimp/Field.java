/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.fieldchimp;

import java.util.logging.Logger;

/**
 *
 * @author gary
 * @param <T>
 */
public interface Field <T> {
    
    public static final Logger LOG = Logger.getLogger(Field.class.getName());
    
    public Class<T> getFieldType();
    public Object getSubject();
    public Pull<T> getPuller();
    public Push<T> getPusher();
    public void setLastAccessValue(T value);
    public T getLastAccessValue();
    
    public default String getFieldLabel() {
        return getPusher().getSetterName();
    }    
    public default T getFieldValue() {
        return getFieldValue(false);
    }
    public default T getFieldValue(boolean sneaky) {
        T value = getPuller().pull();
        if (!sneaky && valueChanged(value, getLastAccessValue())) {
            setLastAccessValue(value);
        }
        return value;
    }
    public default void setFieldValue(T value) {
        getPusher().push(value);
    }
    public default boolean fieldChanged() {
        return !getFieldValue().equals(getLastAccessValue());
    }
    
    private static boolean valueChanged(Object value, Object prev) {
        return (value == null && prev != null)
            || (value != null && prev == null)
            || (value != null && prev != null && !value.equals(prev));
    }
    
}
