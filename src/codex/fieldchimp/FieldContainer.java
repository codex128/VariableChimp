/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.fieldchimp;

import com.jme3.asset.AssetManager;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.core.VersionedReference;

/**
 *
 * @author gary
 * @param <T>
 */
public abstract class FieldContainer <T> extends Container {
    
    private final Field<T> field;
    private VersionedReference<T> reference;
    
    public FieldContainer(Field<T> field) {
        this.field = field;
    }
    
    public abstract void initializeUI(AssetManager assetManager);    
    protected abstract void pull(T value);
    protected abstract T push();
    
    public void pullFieldValue() {
        pull(field.getFieldValue());
    }
    public void pushFieldValue() {
        field.setFieldValue(push());
    }
    
    public Field<T> getField() {
        return field;
    }
    public VersionedReference<T> getReference() {
        return reference;
    }
    
}
