/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp;

import codex.varchimp.gui.FloatContainer;
import codex.varchimp.gui.IntegerContainer;
import codex.varchimp.gui.Vector3fContainer;
import com.jme3.app.Application;

/**
 *
 * @author gary
 */
public interface VarChimpInitializer {
    
    public static final VarChimpInitializer
    DEFAULT = (Application app, VarChimpAppState state) -> {
        state.registerAllFactories(
                new FloatContainer(null),
                new IntegerContainer(null),
                new Vector3fContainer(null));
    };
    
    public void initialize(Application app, VarChimpAppState state);
    
}
