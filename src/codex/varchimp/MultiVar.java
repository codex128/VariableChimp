/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp;

/**
 *
 * @author gary
 */
public class MultiVar <T> implements Variable<T> {
    
    private String group;
    private Object[] subjects;
    private final Class<T> type;
    private final Pull<T> getter;
    private final MultiPush<T> setter;
    
    public MultiVar(Class<T> type, String getter, String setter) {
        this(null, type, new Pull(getter), new MultiPush(setter));
    }
    public MultiVar(Class<T> type, String getter, String setter, Object... subjects) {
        this(null, type, new Pull(getter), new MultiPush(setter), subjects);
    }
    public MultiVar(Class<T> type, Pull<T> getter, MultiPush<T> setter, Object... subjects) {
        this(null, type, getter, setter, subjects);
    }
    public MultiVar(Class<T> type, Pull<T> getter, MultiPush<T> setter) {
        this(null, type, getter, setter);
    }
    public MultiVar(String group, Class<T> type, String getter, String setter) {
        this(group, type, new Pull(getter), new MultiPush(setter));
    }
    public MultiVar(String group, Class<T> type, String getter, String setter, Object... subjects) {
        this(group, type, new Pull(getter), new MultiPush(setter), subjects);
    }
    public MultiVar(String group, Class<T> type, Pull<T> getter, MultiPush<T> setter, Object... subjects) {
        this(group, type, getter, setter);
        this.subjects = subjects;
    }
    public MultiVar(String group, Class<T> type, Pull<T> getter, MultiPush<T> setter) {
        this.group = group;
        this.type = type;
        this.getter = getter;
        this.setter = setter;
        initialize();
    }
    
    private void initialize() {        
        getter.setUser(this);
        setter.setUser(this);
    }
    
    @Override
    public Class<T> getVariableType() {
        return type;
    }
    @Override
    public Object getSubject() {
        return subjects[0];
    }
    @Override
    public Pull<T> getPuller() {
        return getter;
    }
    @Override
    public MultiPush<T> getPusher() {
        return setter;
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
    public MultiVar<T> copy(Object subject) {
        return new MultiVar(group, type, getter, setter, subject);
    }
    
    public void setSubjects(Object... subjects) {
        this.subjects = subjects;
    }
    public Object[] getSubjects() {
        return subjects;
    }
    
}
