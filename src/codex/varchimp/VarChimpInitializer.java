/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp;

import codex.varchimp.gui.DoubleContainer;
import codex.varchimp.gui.FloatContainer;
import codex.varchimp.gui.IntegerContainer;
import codex.varchimp.gui.LongContainer;
import codex.varchimp.gui.QuaternionContainer;
import codex.varchimp.gui.VariableContainerFactory;
import codex.varchimp.gui.Vector3fContainer;
import com.jme3.app.Application;

/**
 * Interface for performing things on VariableChimp initialization.
 * 
 * @author gary
 */
public interface VarChimpInitializer {
    
    /**
     * Array of all factory containers that ship with VariableChimp.
     */
    public static final VariableContainerFactory[]
    DEF_FACTORIES = {
        new FloatContainer(null),
        new IntegerContainer(null),
        new DoubleContainer(null),
        new LongContainer(null),
        new Vector3fContainer(null),
        new QuaternionContainer(null),
    };
    
    /**
     * Default initializer.
     */
    public static final VarChimpInitializer
    DEFAULT = (Application app, VarChimpAppState state) -> {
        state.registerAllFactories(DEF_FACTORIES);
    };
    
    /**
     * Called on initialization.
     * @param app
     * @param state 
     */
    public void initialize(Application app, VarChimpAppState state);
    
}
