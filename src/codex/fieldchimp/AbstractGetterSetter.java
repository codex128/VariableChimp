/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.fieldchimp;

/**
 *
 * @author gary
 */
public abstract class AbstractGetterSetter <T> implements Field<T> {
    
    private final Object subject;
    private final String getter, setter;
    private final FieldId id = new FieldId();
    private T lastAccessValue;
    
    public AbstractGetterSetter(Object subject, String getter, String setter) {
        this.subject = subject;
        this.getter = getter;
        this.setter = setter;
        lastAccessValue = getFieldValue();
    }
    
    @Override
    public Object getSubject() {
        return subject;
    }
    @Override
    public String getFieldGetterName() {
        return getter;
    }
    @Override
    public String getFieldSetterName() {
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
    public FieldId getId() {
        return id;
    }
    @Override
    public String toString() {
        return getFieldSetterName();
    }
    
}
