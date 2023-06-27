/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp;

/**
 *
 * @author gary
 * @param <T>
 */
public class Var <T> implements Variable<T> {
    
    private String group;
    private final Object subject;
    private final Class<T> type;
    private final Pull<T> getter;
    private final Push<T> setter;
    
    public Var(Object subject, Class<T> type, Pull<T> getter, Push<T> setter) {
        this.subject = subject;
        this.type = type;
        this.getter = getter;
        this.setter = setter;
        initialize();
    }
    public Var(String group, Object subject, Class<T> type, Pull<T> getter, Push<T> setter) {
        this(subject, type, getter, setter);
        this.group = group;
    }
    
    private void initialize() {        
        getter.setUser(this);
        setter.setUser(this);
    }
    public void setVariableGroup(String group) {
        this.group = group;
    }
    
    @Override
    public String getVariableGroup() {
        if (group == null) {
            return Variable.super.getVariableGroup();
        }
        return group;
    }
    @Override
    public Class<T> getVariableType() {
        return type;
    }
    @Override
    public Object getSubject() {
        return subject;
    }
    @Override
    public Pull<T> getPuller() {
        return getter;
    }
    @Override
    public Push<T> getPusher() {
        return setter;
    }
    @Override
    public String toString() {
        return getClass().getSimpleName()+"["+getPusher().getSetterName()+"]";
    }
    
}
