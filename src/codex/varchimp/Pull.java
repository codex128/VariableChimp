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
 * Class for handling pulls.
 * 
 * Invokes a getter method defined by a string and returns the result.
 * 
 * @author gary
 * @param <T>
 */
public class Pull <T> {
    
    /**
     *
     */
    protected Variable<T> user;

    /**
     *
     */
    protected String getter;
    
    /**
     *
     * @param getter
     */
    public Pull(String getter) {
        this.getter = getter;
    }
    
    /**
     *
     * @param user
     */
    protected void setUser(Variable<T> user) {
        this.user = user;
    }
    
    /**
     * Fetches a value from the user's subject and returns it.
     * Invokes a getter method defined by the getter string.
     * @return 
     */
    public T pull() {
        if (user == null) return null;
        try {
            Method method = user.getSubject().getClass().getMethod(getter);
            if (user.getVariableType().isAssignableFrom(method.getReturnType())) {
                return (T)method.invoke(user.getSubject());
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
    
    /**
     * Get the user.
     * @return 
     */
    public Variable<T> getUser() {
        return user;
    }
    /**
     * Get the name of the getter method.
     * @return 
     */
    public String getGetterName() {
        return getter;
    }
    
}
