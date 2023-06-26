/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.fieldchimp;

/**
 *
 * @author gary
 * @param <T>
 */
public abstract class AbstractField <T> implements Field<T> {
    
    private final Object subject;
    private final Pull<T> getter;
    private final Push<T> setter;
    private T lastAccessValue;
    
    public AbstractField(Object subject, Pull<T> getter, Push<T> setter) {
        this.subject = subject;
        this.getter = getter;
        this.setter = setter;
        initialize();
    }
    
    private void initialize() {        
        getter.setField(this);
        setter.setField(this);
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
    public void setLastAccessValue(T value) {
        lastAccessValue = value;
    }
    @Override
    public T getLastAccessValue() {
        return lastAccessValue;
    }
    @Override
    public String toString() {
        return getClass().getSimpleName()+"["+getPusher().getSetterName()+"]";
    }
    
}
