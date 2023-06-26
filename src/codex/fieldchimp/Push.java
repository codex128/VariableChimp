/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.fieldchimp;

import static codex.fieldchimp.Variable.LOG;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 *
 * @author gary
 * @param <T>
 */
public class Push <T> {
    
    private Variable<T> user;
    private String setter;
    
    public Push(String setter) {
        this.setter = setter;
    }
    
    protected void setUser(Variable<T> user) {
        this.user = user;
    }
    public void push(T value) {
        if (user == null) return;
        try {
            Method method = user.getSubject().getClass().getMethod(setter, user.getFieldType());
            method.invoke(user.getSubject(), value);
        } catch (NoSuchMethodException ex) {
            LOG.log(Level.SEVERE, "Setter method does not exist!", ex);
        } catch(SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOG.log(Level.SEVERE, "Failed to invoke setter method!", ex);
        }
    }
    
    public Variable<T> getUser() {
        return user;
    }
    public String getSetterName() {
        return setter;
    }
    
}
