/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp;

import static codex.varchimp.VariablePointer.LOG;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 *
 * @author gary
 * @param <T>
 */
public class Pull <T> {
    
    protected VariablePointer<T> user;
    protected String getter;
    
    public Pull(String getter) {
        this.getter = getter;
    }
    
    protected void setUser(VariablePointer<T> user) {
        this.user = user;
    }
    
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
    
    public VariablePointer<T> getUser() {
        return user;
    }
    public String getGetterName() {
        return getter;
    }
    
}
