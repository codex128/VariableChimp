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
public class Variable <T> implements VariablePointer<T> {
    
    private final Object subject;
    private final Class<T> type;
    private final Pull<T> getter;
    private final Push<T> setter;
    
    public Variable(Object subject, Class<T> type, Pull<T> getter, Push<T> setter) {
        this.subject = subject;
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
