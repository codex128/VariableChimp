/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp;

import static codex.varchimp.Variable.LOG;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 * Class for handling pushes.
 * 
 * Invokes a setter method defined by a string to push a given value to a field.
 * 
 * @author gary
 * @param <T>
 */
public class Push <T> {
    
    /**
     *
     */
    protected Variable<T> user;

    /**
     *
     */
    protected String setter;
    
    /**
     *
     * @param setter
     */
    public Push(String setter) {
        this.setter = setter;
    }
    
    /**
     *
     * @param user
     */
    protected void setUser(Variable<T> user) {
        this.user = user;
    }

    /**
     *
     * @param value
     */
    public void push(T value) {
        if (user == null) return;
        try {
            Method method = user.getSubject().getClass().getMethod(setter, user.getVariableType());
            method.invoke(user.getSubject(), value);
        } catch (NoSuchMethodException ex) {
            LOG.log(Level.SEVERE, "Setter method does not exist!", ex);
        } catch(SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOG.log(Level.SEVERE, "Failed to invoke setter method!", ex);
        }
    }
    
    /**
     *
     * @return
     */
    public Variable<T> getUser() {
        return user;
    }

    /**
     *
     * @return
     */
    public String getSetterName() {
        return setter;
    }
    
}
