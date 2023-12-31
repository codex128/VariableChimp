/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Pulling that directly accesses a field belonging to the user's subject.
 * 
 * @author gary
 * @param <T>
 */
public class FieldPull <T> extends Pull<T> {
    
    Field field;
    
    /**
     *
     * @param getter
     */
    public FieldPull(String getter) {
        super(getter);
    }
    
    /**
     *
     * @param user
     */
    @Override
    public void setUser(Variable<T> user) {
        super.setUser(user);
        if (this.user != null) {
            try {
                field = this.user.getSubject().getClass().getDeclaredField(getter);
                if (!field.getType().isAssignableFrom(user.getVariableType())) {
                    throw new IllegalArgumentException("Field type does not match variable type!");
                }
                field.setAccessible(true);
            } catch (NoSuchFieldException | SecurityException ex) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "An error occured while fetching a field", ex);
            }
        }
    }
    @Override
    public T pull() {
        if (user == null || field == null) {
            return null;
        }
        try {
            return (T)field.get(user.getSubject());
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "An error occured while getting a field", ex);
        }
        return null;
    }
    
}
