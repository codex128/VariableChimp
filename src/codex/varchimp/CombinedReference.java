/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp;

import com.simsilica.lemur.core.VersionedObject;
import com.simsilica.lemur.core.VersionedReference;

/**
 * Reference that detects changes in other references.
 * 
 * @author gary
 */
public class CombinedReference extends VersionedReference {
    
    private final VersionedReference[] refs;
    
    public CombinedReference(VersionedReference... refs) {
        super(new DudVersionedObject());
        this.refs = refs;
    }
    
    @Override
    public boolean needsUpdate() {
        for (VersionedReference r : refs) {
            if (r.needsUpdate()) return true;
        }
        return false;
    }
    @Override
    public boolean update() {
        boolean updated = false;
        for (VersionedReference r : refs) {
            if (r.update()) updated = true;
        }
        return updated;
    }    
    
    public static final class DudVersionedObject implements VersionedObject {

        @Override
        public long getVersion() {
            return 0l;
        }
        @Override
        public Object getObject() {
            return this;
        }
        @Override
        public VersionedReference createReference() {
            return new VersionedReference(this);
        }
        
    }
    
}
