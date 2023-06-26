/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp;

import com.jme3.app.Application;

/**
 *
 * @author gary
 */
public class VarChimp {
    
    private static VarChimpAppState instance;
    private static VarChimpInitializer initializer = VarChimpInitializer.DEFAULT;
    
    public static void setInitializer(VarChimpInitializer init) {
        assert init != null;
        initializer = init;
    }
    public static void initialize(Application app) {
        assert app != null;
        instance = new VarChimpAppState();
        initializer.initialize(app, instance);
        app.getStateManager().attach(instance);
    }
    public static VarChimpAppState get() {
        return instance;
    }
    
}
