/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp;

import com.jme3.app.Application;
import com.jme3.input.KeyInput;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;

/**
 * Main access class for VariableChimp.
 * 
 * @author gary
 */
public class VarChimp {
    
    private static VarChimpAppState instance;
    private static VarChimpInitializer initializer = VarChimpInitializer.DEFAULT;
    
    /**
     *
     */
    public static final String VERSION = "v0.7";

    /**
     *
     */
    public static final String
            ACTIVE_INPUT = "VarChimp-activeinput",

    /**
     *
     */
    PASSIVE_INPUT = "VarChimp-passiveinput";

    /**
     *
     */
    public static final FunctionId
            F_OPENWINDOW = new FunctionId(ACTIVE_INPUT, "start-context");
    private static int
            key_openwindow = KeyInput.KEY_F1;
    
    /**
     * Initialize VariableChimp context.
     * @param app 
     */
    public static void initialize(Application app) {
        assert app != null;
        System.out.println("Initializing VariableChimp "+VERSION);
        if (GuiGlobals.getInstance() == null) {
            throw new NullPointerException("Lemur GUI context not started!");
        }
        initInputMappings(GuiGlobals.getInstance().getInputMapper());
        instance = new VarChimpAppState();
        initializer.initialize(app, instance);
        app.getStateManager().attach(instance);
    }
    /**
     * Sets the initializer responsible for initializing certain parts of startup.
     * @param init 
     */
    public static void setInitializer(VarChimpInitializer init) {
        assert init != null;
        if (contextStarted()) {
            System.out.println("Redundent setting of VarChimp initializer: context already started.");
        }
        else {
            initializer = init;
        }
    }
    private static void initInputMappings(InputMapper im) {
        im.map(F_OPENWINDOW, key_openwindow);
    }
    /**
     * Returns true if the context has been started.
     * @return context started
     */
    public static boolean contextStarted() {
        return instance != null;
    }
    
    /**
     * Get the VarChimpAppState instance for this context.
     * @return 
     */
    public static VarChimpAppState get() {
        return instance;
    }
    
    /**
     * Sets the keyboard input which opens the VariableChimp window.
     * @param key 
     */
    public static void setOpenWindowKey(int key) {
        remap(F_OPENWINDOW, key_openwindow, key);
        key_openwindow = key;
    }
    private static void remap(FunctionId func, int oldKey, int newKey) {
        if (!contextStarted()) return;
        InputMapper im = GuiGlobals.getInstance().getInputMapper();
        im.removeMapping(func, oldKey);
        im.map(func, newKey);
    }
    
}
