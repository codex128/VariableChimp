/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp;

/**
 * Default implementation of Variable.
 * 
 * @author gary
 * @param <T>
 */
public class Var <T> implements Variable<T> {
    
    private String group;
    private Object subject;
    private final Class<T> type;
    private final Pull<T> getter;
    private final Push<T> setter;
    
    public Var(Class<T> type, String fieldname) {
        this.type = type;
        getter = new FieldPull(fieldname);
        setter = new FieldPush(fieldname);
    }
    public Var(Object subject, Class<T> type, String fieldname) {
        this(subject, type, new FieldPull(fieldname), new FieldPush(fieldname));
    }
    public Var(String group, Object subject, Class<T> type, String fieldname) {
        this(group, subject, type, new FieldPull(fieldname), new FieldPush(fieldname));
    }
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
    public Var(Object subject, Class<T> type, String getter, String setter) {
        this(subject, type, new Pull(getter), new Push(setter));
    }
    public Var(String group, Object subject, Class<T> type, String getter, String setter) {
        this(group, subject, type, new Pull(getter), new Push(setter));
    }
    
    private void initialize() {        
        getter.setUser(this);
        setter.setUser(this);
    }
    
    /**
     * Sets the subject.
     * @param subject 
     */
    public void setSubject(Object subject) {
        this.subject = subject;
    }
    
    @Override
    public void setVariableGroup(String group) {
        this.group = group;
    }    
    @Override
    public String getVariableGroup() {
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
    public Var<T> copy(Object subject) {
        return new Var(group, subject, type, getter, setter);
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return getClass().getSimpleName()+"["+getPusher().getSetterName()+"]";
    }    
    
    public static Var[] create(String group, Var... vars) {
        for (Var v : vars) {
            v.group = group;
        }
        return vars;
    }
    public static Var[] create(String group, Object subject, Var... vars) {
        for (Var v : vars) {
            v.group = group;
            v.subject = subject;
        }
        return vars;
    }
    
}
