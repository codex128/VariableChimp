/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import java.util.LinkedList;

/**
 *
 * @author gary
 */
public abstract class VarChimpAppState extends BaseAppState {

    protected LinkedList<VarChimpListener> listeners = new LinkedList<>();
    
    @Override
    protected void cleanup(Application app) {
        stateCleanup(app);
        clearAllListeners();
    }
    @Override
    protected void onEnable() {
        listeners.stream().forEach(l -> l.onStateEnabled(this));
        stateEnabled();
    }
    @Override
    protected void onDisable() {
        listeners.stream().forEach(l -> l.onStateDisabled(this));
        stateDisabled();
    }
    
    protected abstract void stateCleanup(Application app);
    protected abstract void stateEnabled();
    protected abstract void stateDisabled();
    
    public void addListener(VarChimpListener listener) {
        listeners.add(listener);
    }
    public void removeListener(VarChimpListener listener) {
        listeners.remove(listener);
    }
    public void clearAllListeners() {
        listeners.clear();
    }
    
}
