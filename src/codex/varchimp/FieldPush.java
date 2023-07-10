/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Pushing that directly accesses and sets a field belonging to the user's subject.
 * 
 * @author gary
 * @param <T>
 */
public class FieldPush <T> extends Push<T> {
    
    Field field;
    
    /**
     *
     * @param setter
     */
    public FieldPush(String setter) {
        super(setter);
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
                field = this.user.getSubject().getClass().getDeclaredField(setter);
                if (!field.getType().isAssignableFrom(this.user.getVariableType())) {
                    throw new IllegalArgumentException("Field type does not match variable type!");
                }
                field.setAccessible(true);
            } catch (NoSuchFieldException | SecurityException ex) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "error locating field");
            }
        }
    }    

    /**
     *
     * @param value
     */
    @Override
    public void push(T value) {
        if (user == null) return;
        try {
            field.set(user.getSubject(), value);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "An error occured while setting a field", ex);
        }
    }
    
}
