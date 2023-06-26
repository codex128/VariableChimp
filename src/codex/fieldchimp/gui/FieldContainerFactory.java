/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.fieldchimp.gui;

import codex.fieldchimp.Field;

/**
 *
 * @author gary
 * @param <T>
 */
public interface FieldContainerFactory <T> {
    
    public Class<T> getFieldType();    
    public abstract FieldContainer create(Field field);
    
    public default boolean accept(Field field) {
        return getFieldType().isAssignableFrom(field.getFieldType());
    }    
    
}
