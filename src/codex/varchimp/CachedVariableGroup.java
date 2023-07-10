/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp;

import codex.varchimp.gui.VariableContainer;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * Data set representing a single group of cached variables.
 * 
 * @author gary
 */
public class CachedVariableGroup {
    
    private String groupname;
    private LinkedList<CachedVariable> cache = new LinkedList<>();
    
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
            VarChimp.get().register(variable);
            if (!pull) {
                VariableContainer container = VarChimp.get().getVariableContainer(variable);
                container.applyPullValue(v.value);
                container.pushValue();
            }
        });
    }
    
    private void setGroupName(String name) {
        groupname = name;
    }
    /**
     * Get the group this cache is containing.
     * @return 
     */
    public String getGroupName() {
        return groupname;
    }
    
    /**
     * Create a cache of all given variables.
     * <p>Assigns the group name to all variables.
     * @param groupname
     * @param variables
     * @return 
     */
    public static CachedVariableGroup cache(String groupname, Variable... variables) {
        CachedVariableGroup cache = new CachedVariableGroup();
        cache.setGroupName(groupname);
        for (Variable v : variables) {
            v.setVariableGroup(groupname);
            cache.add(v);
        }
        if (VarChimp.contextStarted()) {
            VarChimp.get().addCache(cache);
        }
        return cache;
    }
    /**
     * Create a cache of the variable stream.
     * <p>Assigns the group name to all variables.
     * @param groupname
     * @param variables
     * @return 
     */
    public static CachedVariableGroup cache(String groupname, Stream<Variable> variables) {
        CachedVariableGroup cache = new CachedVariableGroup();
        cache.setGroupName(groupname);
        variables.forEach(v -> {
            v.setVariableGroup(groupname);
            cache.add(v);
        });
        if (VarChimp.contextStarted()) {
            VarChimp.get().addCache(cache);
        }
        return cache;
    }
    
    /**
     *
     */
    protected static final class CachedVariable {
        Variable variable;
        Object value;

        /**
         *
         * @param variable
         */
        protected CachedVariable(Variable variable) {
            this.variable = variable;
            value = VarChimp.get().getVariableContainer(this.variable).fetchPushValue();
        }
    }
    
}
