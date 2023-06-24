/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.fieldchimp;

/**
 *
 * @author gary
 */
public abstract class FieldContainerFactory <T> {
    
    private final Class type;
    
    public FieldContainerFactory(Class type) {
        this.type = type;
    }
    
    public Class getFieldType() {
        return type;
    }
    public boolean accept(Field field) {
        return field.getFieldType() == getFieldType();
    }
    
    public abstract FieldContainer create(Field field);
    
    
}
