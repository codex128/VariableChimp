/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.fieldchimp;

import static codex.fieldchimp.Field.LOG;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 *
 * @author gary
 * @param <T>
 */
public class Pull <T> {
    
    private Field<T> field;
    private String getter;
    
    public Pull(String getter) {
        this.getter = getter;
    }
    
    protected void setField(Field<T> field) {
        this.field = field;
    }
    public T pull() {
        if (field == null) return null;
        try {
            Method method = field.getSubject().getClass().getMethod(getter);
            if (field.getFieldType().isAssignableFrom(method.getReturnType())) {
                T value = (T)method.invoke(field.getSubject());
                //if (!sneaky) setLastAccessValue(value);
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
    
    public Field<T> getField() {
        return field;
    }
    public String getGetterName() {
        return getter;
    }
    
}
