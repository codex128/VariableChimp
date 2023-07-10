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
    
    /**
     *
     * @param refs
     */
    public CombinedReference(VersionedReference... refs) {
        super(DudVersionedObject.INSTANCE);
        this.refs = refs;
    }
    
    /**
     *
     * @return
     */
    @Override
    public boolean needsUpdate() {
        for (VersionedReference r : refs) {
            if (r.needsUpdate()) return true;
        }
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean update() {
        boolean updated = false;
        for (VersionedReference r : refs) {
            if (r.update()) updated = true;
        }
        return updated;
    }    
    
    /**
     * A do-nothing versioned object.
     */
    private static final class DudVersionedObject implements VersionedObject {
        
        /**
         *
         */
        public static final DudVersionedObject INSTANCE = new DudVersionedObject();
        
        /**
         *
         * @return
         */
        @Override
        public long getVersion() {
            return 0l;
        }

        /**
         *
         * @return
         */
        @Override
        public Object getObject() {
            return this;
        }

        /**
         *
         * @return
         */
        @Override
        public VersionedReference createReference() {
            return new VersionedReference(this);
        }
        
    }
    
}
