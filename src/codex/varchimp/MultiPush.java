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
 *
 * @author gary
 * @param <T>
 */
public class MultiPush <T> extends Push<T> {
    
    MultiVar<T> usr;
    
    public MultiPush(String setter) {
        super(setter);
    }
    
    @Override
    protected void setUser(Variable<T> user) {
        if (!(user instanceof MultiVar)) {
            throw new IllegalStateException("Can only operate with MultiVar!");
        }
        this.user = user;
        usr = (MultiVar)user;
    }    
    @Override
    public void push(T value) {
        if (usr == null) return;        
        try {
            for (Object sub : usr.getSubjects()) {
                if (sub == null) continue;
                Method method = sub.getClass().getMethod(setter, user.getVariableType());
                method.invoke(user.getSubject(), value);
            }
        } catch (NoSuchMethodException ex) {
            LOG.log(Level.SEVERE, "Setter method does not exist!", ex);
        } catch(SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOG.log(Level.SEVERE, "Failed to invoke setter method!", ex);
        }
    }
    
}
