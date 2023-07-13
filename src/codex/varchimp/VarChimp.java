/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp;

import codex.varchimp.gui.GuiDisplayState;
import codex.varchimp.gui.*;
import com.jme3.app.Application;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;

/**
 * Main access class for VariableChimp.
 * 
 * @author gary
 */
public class VarChimp {
    
    private static GuiDisplayState guiRoot;
    private static VariableManipulatorState instance;
    
    /**
     * The version of VariableChimp used.
     */
    public static final String VERSION = "v0.1.8";
    
    /**
     * Array of all factory containers that ship with VariableChimp.
     */
    public static final VariableContainerFactory[]
    DEF_FACTORIES = {
        new FloatContainer(null),
        new IntegerContainer(null),
        new DoubleContainer(null),
        new LongContainer(null),
        new StringContainer(null),
        new Vector3fContainer(null),
        new QuaternionContainer(null),
    };

    public static final String
            ACTIVE_INPUT = "VarChimp-activeinput",
            PASSIVE_INPUT = "VarChimp-passiveinput";

    public static final FunctionId
            F_OPENWINDOW = new FunctionId(ACTIVE_INPUT, "open-window"),
            F_SCROLL = new FunctionId(PASSIVE_INPUT, "scroll");
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
        guiRoot = new GuiDisplayState();
        app.getStateManager().attach(guiRoot);
        instance = new VariableManipulatorState();
        instance.registerAllFactories(DEF_FACTORIES);
        app.getStateManager().attach(instance);
    }
    private static void initInputMappings(InputMapper im) {
        im.map(F_OPENWINDOW, key_openwindow);
        im.map(F_SCROLL, MouseInput.AXIS_WHEEL);
    }
    
    /**
     * Get the {@code VariableManipulatorState} instance for this context.
     * @return 
     */
    public static VariableManipulatorState get() {
        return instance;
    }
    /**
     * Get the {@code GuiDisplayState} instance for this context.
     * @return 
     */
    public static GuiDisplayState getGuiRoot() {
        return guiRoot;
    }
    
    /**
     * Returns true if the context has been started.
     * @return context started
     */
    public static boolean contextStarted() {
        return instance != null;
    }
    /**
     * Returns true if the state has been initialized.
     * <p>This differs from VarChimp being initialized, since states are
     * initialized on the next update.
     * @return 
     */
    public static boolean stateInitialized() {
        return instance.isInitialized();
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
