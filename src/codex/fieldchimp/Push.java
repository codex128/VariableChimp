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
public class Push <T> {
    
    private Field<T> field;
    private String setter;
    
    public Push(String setter) {
        this.setter = setter;
    }
    
    protected void setField(Field<T> field) {
        this.field = field;
    }
    public void push(T value) {
        if (field == null) return;
        try {
            Method method = field.getSubject().getClass().getMethod(setter, field.getFieldType());
            method.invoke(field.getSubject(), value);
        } catch (NoSuchMethodException ex) {
            LOG.log(Level.SEVERE, "Setter method does not exist!", ex);
        } catch(SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOG.log(Level.SEVERE, "Failed to invoke setter method!", ex);
        }
    }
    
    public Field<T> getField() {
        return field;
    }
    public String getSetterName() {
        return setter;
    }
    
}
