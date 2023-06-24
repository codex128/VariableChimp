/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.fieldchimp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
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
    public String getFieldGetterName();
    public String getFieldSetterName();
    public FieldId getId();
    public void setLastAccessValue(T value);
    public T getLastAccessValue();
    public T constrainFieldValue(T value);
    
    public default String getFieldLabel() {
        return getFieldSetterName();
    }    
    public default T getFieldValue() {
        return getFieldValue(false);
    }
    public default T getFieldValue(boolean sneaky) {
        try {
            Method method = getSubject().getClass().getMethod(getFieldGetterName());
            if (method.getReturnType() == getFieldType()) {
                T value = (T)method.invoke(getSubject());
                if (!sneaky) setLastAccessValue(value);
                return value;
            }
            else {
                LOG.log(Level.SEVERE, "Getter return type does not match field type!");
            }
        } catch (NoSuchMethodException ex) {
            LOG.log(Level.SEVERE, "Getter method does not exist!", ex);
        } catch(SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOG.log(Level.SEVERE, "Failed to invoke getter method!", ex);
        }
        return null;
    }
    public default void setFieldValue(T value) {
        try {
            Method method = getSubject().getClass().getMethod(getFieldSetterName(), getFieldType());
            method.invoke(getSubject(), value);
        } catch (NoSuchMethodException ex) {
            LOG.log(Level.SEVERE, "Setter method does not exist!", ex);
        } catch(SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOG.log(Level.SEVERE, "Failed to invoke setter method!", ex);
        }
    }
    public default boolean fieldChanged() {
        return !getFieldValue().equals(getLastAccessValue());
    }
    
}
