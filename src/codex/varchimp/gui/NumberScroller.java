/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp.gui;

import com.jme3.math.FastMath;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.DefaultRangedValueModel;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.RangedValueModel;
import com.simsilica.lemur.component.BorderLayout;
import com.simsilica.lemur.core.GuiControl;
import com.simsilica.lemur.core.VersionedReference;
import com.simsilica.lemur.style.Attributes;
import com.simsilica.lemur.style.ElementId;
import com.simsilica.lemur.style.StyleDefaults;
import com.simsilica.lemur.style.Styles;

/**
 *
 * @author gary
 */
public class NumberScroller extends Panel {
    
    public static final String
            ELEMENT_ID = "number-scroller",
            INCREMENT_ID = "increment.button",
            DECREMENT_ID = "decrement.button",
            RANGE_ID = "range";
    
    private final ElementId id;
    private final BorderLayout layout;
    private Button increment, decrement;
    private Label value;
    private Panel range;
    private double delta = 1.0;
    private RangedValueModel model;
    private VersionedReference state;
    private ValueDisplay display = ValueDisplay.DEFAULT;
    
    public NumberScroller() {
        this(new DefaultRangedValueModel(0, 1, 0), new ElementId(ELEMENT_ID), null);
    }
    public NumberScroller(ElementId id) {
        this(new DefaultRangedValueModel(0, 1, 0), id, null);
    }
    public NumberScroller(String style) {
        this(new DefaultRangedValueModel(0, 1, 0), new ElementId(ELEMENT_ID), style);
    }
    public NumberScroller(ElementId id, String style) {
        this(new DefaultRangedValueModel(0, 1, 0), id, style);
    }
    public NumberScroller(RangedValueModel model, ElementId id, String style) {
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
        value = rangeLayout.addChild(BorderLayout.Position.Center, new Label(""+model.getValue()));
        increment.addClickCommands(new ChangeValueCommand(1));
        decrement.addClickCommands(new ChangeValueCommand(-1));
    }
    
    @StyleDefaults(ELEMENT_ID)
    public static void initializeDefaultStyles( Styles styles, Attributes attrs ) {
        ElementId parent = new ElementId(ELEMENT_ID);
        styles.getSelector(parent.child(INCREMENT_ID), null).set("text", ">", false);
        styles.getSelector(parent.child(DECREMENT_ID), null).set("text", "<", false);
    }
    
    @Override
    public void updateLogicalState(float tpf) {
        super.updateLogicalState(tpf);
        if (state == null || state.update()) {
            value.setText(display.displayValue(model));
        }
    }
    
    public void setDelta(double delta) {
        this.delta = delta;
    }
    public double getDelta() {
        return delta;
    }
    public RangedValueModel getModel() {
        return model;
    }
    public void setModel(RangedValueModel model) {
        if (this.model == model) return;
        this.model = model;
        state = this.model.createReference();
    }
    public ValueDisplay getValueDisplay() {
        return display;
    }
    public void setValueDisplay(ValueDisplay display) {
        assert display != null;
        this.display = display;
    }
    
    private class ChangeValueCommand implements Command<Button> {        
        int direction;        
        public ChangeValueCommand(int direction) {
            this.direction = FastMath.sign(direction);
        }        
        @Override
        public void execute(Button source) {
            model.setValue(model.getValue()+delta*direction);
        }        
    }
    
}
