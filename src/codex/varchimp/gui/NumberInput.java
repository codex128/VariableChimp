/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp.gui;

import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.FastMath;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.DefaultRangedValueModel;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.RangedValueModel;
import com.simsilica.lemur.component.BorderLayout;
import com.simsilica.lemur.component.TextEntryComponent;
import com.simsilica.lemur.core.GuiControl;
import com.simsilica.lemur.core.VersionedReference;
import com.simsilica.lemur.event.KeyListener;
import com.simsilica.lemur.event.MouseListener;
import com.simsilica.lemur.input.AnalogFunctionListener;
import com.simsilica.lemur.input.Axis;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;
import com.simsilica.lemur.style.Attributes;
import com.simsilica.lemur.style.ElementId;
import com.simsilica.lemur.style.StyleDefaults;
import com.simsilica.lemur.style.Styles;

/**
 * GUI element for editing numbers.
 * 
 * @author gary
 */
public class NumberInput extends Panel implements AnalogFunctionListener, StateFunctionListener {
    
    public static final String 
            ELEMENT_ID = "numberInput",
            INCREMENT_ID = "right.button",
            DECREMENT_ID = "left.button",
            RANGE_ID = "range",
            INPUT_GROUP = "numberInput[inputGroup]";
    
    private static enum InputType {
        NORMAL("normal"), DRAGGING("dragging"), TYPING("typing");
        private String name;
        private InputType(String name) {
            this.name = name;
        }
        private String getName() {
            return name;
        }
    }
    
    private static final FunctionId
            F_DRAG = new FunctionId(INPUT_GROUP, "drag"),
            F_CONFIRM = new FunctionId(INPUT_GROUP, "confirm"),
            F_RELEASE = new FunctionId(INPUT_GROUP, "release"),
            F_DENY = new FunctionId(INPUT_GROUP, "deny");
    private static boolean inputInit = false;
    
    private final ElementId id;
    private final BorderLayout layout;
    private Button increment, decrement;
    private Label value;
    private Panel range;
    private RangedValueModel model;
    private TextEntryComponent text;
    private VersionedReference state;
    private DeltaModel delta = new DeltaModel(1.0, 0.1);
    private InputType inputType = InputType.NORMAL;
    private double lastValue;
    private String typedNumber = "";
    private boolean inputListening = false;
    private DoubleClickListener doubleclick;
    private KeyHandler keys;
    private int renderLength = 5;
    
    public NumberInput() {
        this(new DefaultRangedValueModel(0, 1, 0), new ElementId(ELEMENT_ID), null);
    }
    public NumberInput(ElementId id) {
        this(new DefaultRangedValueModel(0, 1, 0), id, null);
    }
    public NumberInput(String style) {
        this(new DefaultRangedValueModel(0, 1, 0), new ElementId(ELEMENT_ID), style);
    }
    public NumberInput(ElementId id, String style) {
        this(new DefaultRangedValueModel(0, 1, 0), id, style);
    }
    public NumberInput(RangedValueModel model, ElementId id, String style) {
        initInputMappings(GuiGlobals.getInstance().getInputMapper());
        Styles styles = GuiGlobals.getInstance().getStyles();
        styles.initializeStyles(getClass());
        //id = new ElementId(ELEMENT_ID);
        this.id = id;
        layout = new BorderLayout();
        //model = new DefaultRangedValueModel(0, 1, 0);
        setModel(model);
        getControl(GuiControl.class).setLayout(layout);
        increment = layout.addChild(BorderLayout.Position.East, new Button(null, id.child(INCREMENT_ID), style));
        decrement = layout.addChild(BorderLayout.Position.West, new Button(null, id.child(DECREMENT_ID), style));
        range = layout.addChild(BorderLayout.Position.Center, new Panel(50, 2, id.child(RANGE_ID), style));
        BorderLayout rangeLayout = new BorderLayout();
        range.getControl(GuiControl.class).setLayout(rangeLayout);
        doubleclick = new DoubleClickListener(.5f);
        range.addMouseListener(doubleclick);
        //range.addMouseListener(new DragListener());
        value = rangeLayout.addChild(BorderLayout.Position.Center, new Label(""+model.getValue()));
        increment.addClickCommands(new IncrementValueCommand(1));
        decrement.addClickCommands(new IncrementValueCommand(-1));
        enableInputListening(true);
    }
    
    @Override
    public void updateLogicalState(float tpf) {
        super.updateLogicalState(tpf);
        doubleclick.update(tpf);
        if (inputType == InputType.TYPING) {
            value.setText(typedNumber);
        }
        else if (state == null || state.update()) {
            value.setText(""+model.getValue());
            if (inputType == InputType.NORMAL) {
                lastValue = model.getValue();
            }
        }
    }
    @Override
    public void valueActive(FunctionId func, double value, double tpf) {
        if (func == F_DRAG && inputType == InputType.DRAGGING) {
            model.setValue(model.getValue()+delta.getDelta(1)*value);
        }
    }    
    @Override
    public void valueChanged(FunctionId func, InputState value, double tpf) {
        if ((func == F_CONFIRM && value != InputState.Off && inputType != InputType.NORMAL) ||
                func == F_RELEASE && value == InputState.Off && inputType == InputType.DRAGGING) {
            stopSpecialNumberEntry();
            if (inputType == InputType.TYPING) {
                model.setValue(parseText());
            }
        }
        else if (func == F_DENY && value != InputState.Off && inputType != InputType.NORMAL) {
            stopSpecialNumberEntry();
            model.setValue(lastValue);
        }
    }
    @Override
    protected void setParent(Node p) {
        super.setParent(p);
        enableInputListening(parent != null);
    }
    
    private void enableInputListening(boolean enable) {
        if (enable) {
            if (!inputListening) {
                GuiGlobals.getInstance().getInputMapper().addAnalogListener(this, F_DRAG);
                GuiGlobals.getInstance().getInputMapper().addStateListener(this,
                        F_CONFIRM, F_RELEASE, F_DENY);
                keys = new KeyHandler();
                GuiGlobals.getInstance().addKeyListener(keys);
                inputListening = true;
            }
        }
        else if (inputListening) {
            GuiGlobals.getInstance().getInputMapper().removeAnalogListener(this, F_DRAG);
            GuiGlobals.getInstance().getInputMapper().removeStateListener(this,
                    F_CONFIRM, F_RELEASE, F_DENY);
            GuiGlobals.getInstance().removeKeyListener(keys);
            inputListening = false;
        }
    }
    private void startNumberTyping() {
        System.out.println("start typing mode");
        inputType = InputType.TYPING;
        lastValue = model.getValue();
    }
    private void startNumberDragging() {
        System.out.println("start dragging mode");
        inputType = InputType.DRAGGING;
        GuiGlobals.getInstance().setCursorEventsEnabled(false, true);
        lastValue = model.getValue();
    }
    private void stopSpecialNumberEntry() {
        if (inputType == InputType.DRAGGING) {
            GuiGlobals.getInstance().setCursorEventsEnabled(true, true);
        }
        else if (inputType == InputType.TYPING) {}
        inputType = InputType.NORMAL;
    }
    private void enterCharacter(char c) {
        if (Character.isDigit(c) || c == '.') {
            System.out.println("accept digit \""+c+"\"");
            value.setText(value.getText()+c);
        }
    }
    private void removeLastCharacter() {
        System.out.println("remove last character");
        if (value.getText().length() <= 1) {
            value.setText("");
        }
        else {
            value.setText(value.getText().substring(0, typedNumber.length()-1));
        }
    }
    private double parseText() {
        Double d = safeParseDouble(value.getText());
        if (d != null) return d;
        else return lastValue;
    }
    
    public void setDelta(DeltaModel delta) {
        this.delta = delta;
        this.delta.setMinimumDeltaValues(2);
    }
    public DeltaModel getDelta() {
        return delta;
    }
    public RangedValueModel getModel() {
        return model;
    }
    public void setModel(RangedValueModel model) {
        if (this.model == model) return;
        this.model = model;
        this.model.setMaximum(Double.MAX_VALUE);
        this.model.setMinimum(-Double.MAX_VALUE);
        state = this.model.createReference();
    }    
    
    @StyleDefaults(ELEMENT_ID)
    public static void initializeDefaultStyles( Styles styles, Attributes attrs ) {
        ElementId parent = new ElementId(ELEMENT_ID);
        styles.getSelector(parent.child(INCREMENT_ID), null).set("text", ">", false);
        styles.getSelector(parent.child(DECREMENT_ID), null).set("text", "<", false);
    }
    private static void initInputMappings(InputMapper im) {
        if (inputInit) return;
        im.map(F_DRAG, Axis.MOUSE_X);
        im.map(F_CONFIRM, KeyInput.KEY_RETURN);
        im.map(F_RELEASE, com.simsilica.lemur.input.Button.MOUSE_BUTTON1);
        im.map(F_DENY, com.simsilica.lemur.input.Button.MOUSE_BUTTON3);
        im.activateGroup(INPUT_GROUP);
        inputInit = true;
    }
    private static Double safeParseDouble(String string) {
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c != '.' && !Character.isDigit(c)) {
                return null;
            }
        }
        return Double.parseDouble(string);
    }
    
    private class IncrementValueCommand implements Command<Button> {        
        int direction;        
        public IncrementValueCommand(int direction) {
            this.direction = FastMath.sign(direction);
        }        
        @Override
        public void execute(Button source) {
            if (inputType != InputType.NORMAL) return;
            model.setValue(model.getValue()+delta.getDelta(0)*direction);
        }        
    }
    private class DoubleClickListener implements MouseListener {
        float margin, duration = 0f;
        boolean clicked = false;
        
        private DoubleClickListener(float margin) {
            this.margin = margin;
        }
        
        public void update(float tpf) {
            if (clicked && (duration += tpf) >= margin) {
                clicked = false;
                duration = 0f;
            }
        }
        
        @Override
        public void mouseButtonEvent(MouseButtonEvent mbe, Spatial sptl, Spatial sptl1) {
            if (mbe.getButtonIndex() == MouseInput.BUTTON_LEFT) {
                if (mbe.isReleased()) {
                    if (clicked) {
                        startNumberTyping();
                        duration = 0f;
                        clicked = false;
                    }
                }
                else clicked = true;
            }
        }
        @Override
        public void mouseEntered(MouseMotionEvent mme, Spatial sptl, Spatial sptl1) {}
        @Override
        public void mouseExited(MouseMotionEvent mme, Spatial sptl, Spatial sptl1) {}
        @Override
        public void mouseMoved(MouseMotionEvent mme, Spatial sptl, Spatial sptl1) {}
        
    }
    private class DragListener implements MouseListener {
        boolean pressed = false;
        @Override
        public void mouseButtonEvent(MouseButtonEvent mbe, Spatial sptl, Spatial sptl1) {
            if (mbe.getButtonIndex() == MouseInput.BUTTON_LEFT) {
                pressed = mbe.isPressed();
            }
        }
        @Override
        public void mouseEntered(MouseMotionEvent mme, Spatial sptl, Spatial sptl1) {}
        @Override
        public void mouseExited(MouseMotionEvent mme, Spatial sptl, Spatial sptl1) {}
        @Override
        public void mouseMoved(MouseMotionEvent mme, Spatial sptl, Spatial sptl1) {
            if (inputType == InputType.NORMAL && pressed) {
                startNumberDragging();
                pressed = false;
                mme.setConsumed();
            }
        }        
    }
    private class KeyHandler implements KeyListener {
        @Override
        public void onKeyEvent(KeyInputEvent kie) {
            if (inputType != InputType.TYPING) return;
            char c = kie.getKeyChar();
            if (c != 0) {
                enterCharacter(c);
            }
            else if (kie.getKeyCode() == KeyInput.KEY_BACK) {
                removeLastCharacter();
            }
        }        
    }
    
}
