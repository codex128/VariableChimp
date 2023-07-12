/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.varchimp;

import com.jme3.app.state.AppState;

/**
 *
 * @author gary
 */
public interface VarChimpListener {
    
    public void onStateEnabled(AppState state);
    public void onStateDisabled(AppState state);
    public void onGuiEnabled();
    public void onGuiDisabled();
    
}
