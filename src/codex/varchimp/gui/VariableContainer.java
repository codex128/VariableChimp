/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp.gui;

import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.BoxLayout;
import com.simsilica.lemur.core.VersionedReference;
import com.simsilica.lemur.DefaultRangedValueModel;
import com.simsilica.lemur.RangedValueModel;
import codex.varchimp.Variable;

/**
 * Maintains a GUI which displays and edits a data type.
 * 
 * @author gary
 * @param <T>
 */
public abstract class VariableContainer <T> extends Container implements VariableContainerFactory {
    
    /**
     *
     */
    protected final Variable<T> variable;

    /**
     *
     */
    protected Container editContainer,

    /**
     *
     */
    buttonContainer;

    /**
     *
     */
    protected VersionedReference reference;
    private boolean initialized = false;
    
    /**
     * 
     * @param variable 
     */
    public VariableContainer(Variable<T> variable) {
        this.variable = variable;
    }
    
    /**
     * Initializes the Lemur gui used to edit the variable.
     */
    protected abstract void initEditingGui();
    /**
     * Applies the given value to the gui.
     * @param value 
     */
    protected abstract void pull(T value);
    /**
     * Returns the value represented by the gui.
     * @return 
     */
    protected abstract T push();
    
    /**
     * Initializes Lemur gui.
     */
    public void initialize() {
        addChild(new Label(variable.getVariableLabel()));
        editContainer = addChild(new Container());
        buttonContainer = addChild(new Container());
        buttonContainer.setLayout(new BoxLayout(Axis.X, FillMode.Even));
        initButtons();
        initEditingGui();
        if (reference == null) {
            throw new NullPointerException("Versioned reference was not initialized, please initialize reference during initialization.");
        }
        initialized = true;
        pullValue();
    }

    /**
     *
     */
    protected void initButtons() {
        Button push = buttonContainer.addChild(new Button("Push"));
        push.addClickCommands(new PushPullCommand(true));
        Button pull = buttonContainer.addChild(new Button("Pull"));
        pull.addClickCommands(new PushPullCommand(false));
        Button print = buttonContainer.addChild(new Button("Print"));
        print.addClickCommands(new PrintCommand());
    }
    
    /**
     * Pulls the variable value to the gui value (similar to git's pull command).
     */
    public void pullValue() {
        if (!isInitialized()) return;
        pull(variable.getVariableValue());
    }
    /**
     * Pushes the gui value to the variable value (similar to git's push command).
     */
    public void pushValue() {
        if (!isInitialized()) return;
        variable.setVariableValue(push());
    }
    /**
     * Pulls and applies the given value to the GUI, no questions asked.
     * @param value 
     */
    public void applyPullValue(T value) {
        pull(value);
    }
    /**
     * Fetches (without actually performing a push) the push value.
     * @return 
     */
    public T fetchPushValue() {
        return push();
    }
    
    /**
     * Sets the VersionedReferenced used to detect GUI changes.
     * @param ref 
     */
    protected void setReference(VersionedReference ref) {
        reference = ref;
    }
    
    /**
     * 
     * @return 
     */
    public Variable<T> getVariable() {
        return variable;
    }
    /**
     * Get the VersionedReference used to detect GUI changes.
     * @return 
     */
    public VersionedReference getReference() {
        return reference;
    }
    /**
     * 
     * @return 
     */
    public boolean isInitialized() {
        return initialized;
    }
    
    /**
     * Create a RangedValueModel for the number type.
     * @param type
     * @return 
     */
    public static RangedValueModel createDefaultModel(Class<? extends Number> type) {
        if (Integer.class.isAssignableFrom(type)) {
            return new DefaultRangedValueModel(Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        }
        else if (Float.class.isAssignableFrom(type)) {
            return new DefaultRangedValueModel(-Float.MAX_VALUE, Float.MAX_VALUE, 0f);
        }
        else if (Double.class.isAssignableFrom(type)) {
            return new DefaultRangedValueModel(-Double.MAX_VALUE, Double.MAX_VALUE, 0.0);
        }
        else if (Long.class.isAssignableFrom(type)) {
            return new DefaultRangedValueModel(Long.MIN_VALUE, Long.MAX_VALUE, 0l);
        }
        return null;
    }
    
    private class PushPullCommand implements Command<Button> {
        
        private boolean push;
        
        public PushPullCommand(boolean push) {
            this.push = push;
        }
        
        @Override
        public void execute(Button source) {
            if (push) pushValue();
            else pullValue();
        }        
    }
    private class PrintCommand implements Command<Button> {
        @Override
        public void execute(Button source) {
            System.out.println(variable.getVariableLabel()+" for "+variable.getSubject()+" = "+push());
        }        
    }
    
}
