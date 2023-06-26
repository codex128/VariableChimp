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
import codex.varchimp.VariablePointer;

/**
 *
 * @author gary
 * @param <T>
 */
public abstract class VariableContainer <T> extends Container implements VariableContainerFactory {
    
    protected final VariablePointer<T> variable;
    protected Container editContainer, buttonContainer;
    protected VersionedReference reference;
    private boolean initialized = false;
    
    public VariableContainer(VariablePointer<T> variable) {
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
        pullFieldValue();
    }
    protected void initButtons() {
        Button push = buttonContainer.addChild(new Button("Push"));
        push.addClickCommands(new PushPullCommand(true));
        Button pull = buttonContainer.addChild(new Button("Pull"));
        pull.addClickCommands(new PushPullCommand(false));
    }
    
    /**
     * Pulls the variable value to the gui value (similar to git's pull command).
     */
    public void pullFieldValue() {
        if (!isInitialized()) return;
        pull(variable.getVariableValue());
    }
    /**
     * Pushes the gui value to the variable value (similar to git's push command).
     */
    public void pushFieldValue() {
        if (!isInitialized()) return;
        variable.setVariableValue(push());
    }
    
    protected void setReference(VersionedReference ref) {
        reference = ref;
    }
    
    public VariablePointer<T> getVariable() {
        return variable;
    }
    public VersionedReference getReference() {
        return reference;
    }
    public boolean isInitialized() {
        return initialized;
    }
    
    
    private class PushPullCommand implements Command<Button> {
        
        private boolean push;
        
        public PushPullCommand(boolean push) {
            this.push = push;
        }
        
        @Override
        public void execute(Button source) {
            if (push) pushFieldValue();
            else pullFieldValue();
        }        
    }
    
}
