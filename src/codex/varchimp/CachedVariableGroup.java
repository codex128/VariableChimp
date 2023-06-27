/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp;

import codex.varchimp.gui.VariableContainer;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 *
 * @author gary
 */
public class CachedVariableGroup {
    
    String groupname;
    LinkedList<CachedVariable> cache = new LinkedList<>();
    
    /**
     * Add a variable to this cache.
     * @param variable 
     */
    public void add(Variable variable) {
        if (groupname == null) {
            groupname = variable.getVariableGroup();
        }
        else if (!groupname.equals(variable.getVariableGroup())) {
            return;
        }
        cache.add(new CachedVariable(variable));
    }
    /**
     * Add a stream of variables to this cache.
     * @param stream 
     */
    public void addAll(Stream<Variable> stream) {
        stream.forEach(v -> add(v));
    }
    /**
     * Clear all variables from this cache.
     */
    public void clear() {
        cache.clear();
        groupname = null;
    }
    
    /**
     * Register a copy of all variables in this cache.
     * Performs a push (instead of a pull) by default.
     * @param subject 
     */
    public void applyTo(Object subject) {
        applyTo(subject, false);
    }
    /**
     * Register a copy of all variables in this cache.
     * @param subject
     * @param pull if true, perform a pull on register, otherwise perform a push
     */
    public void applyTo(Object subject, boolean pull) {
        cache.stream().forEach(v -> {
            Variable variable = v.variable.copy(subject);
            VarChimp.get().register(variable, pull);
            if (!pull) {
                VariableContainer container = VarChimp.get().getVariableContainer(variable);
                container.applyPullValue(v.value);
                container.pushValue();
            }
        });
    }
    
    public String getGroupName() {
        return groupname;
    }
    
    protected static final class CachedVariable {
        Variable variable;
        Object value;
        protected CachedVariable(Variable variable) {
            this.variable = variable;
            value = VarChimp.get().getVariableContainer(this.variable).fetchPushValue();
        }
    }
    
}
