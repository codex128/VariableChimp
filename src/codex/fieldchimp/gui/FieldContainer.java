/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.fieldchimp.gui;

import codex.fieldchimp.Field;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.BoxLayout;
import com.simsilica.lemur.core.VersionedReference;

/**
 *
 * @author gary
 * @param <T>
 */
public abstract class FieldContainer <T> extends Container implements FieldContainerFactory {
    
    protected final Field<T> field;
    protected Container editContainer, buttonContainer;
    protected VersionedReference reference;
    private boolean initialized = false;
    
    public FieldContainer(Field<T> field) {
        this.field = field;
    }
    
    /**
     * Initializes the Lemur gui used to edit the field.
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
        addChild(new Label(field.getFieldLabel()));
        editContainer = addChild(new Container());
        buttonContainer = addChild(new Container());
        buttonContainer.setLayout(new BoxLayout(Axis.X, FillMode.Even));
        initButtons();
        initEditingGui();
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
     * Pulls the field value to the gui value (similar to git's pull command).
     */
    public void pullFieldValue() {
        if (!isInitialized()) return;
        pull(field.getFieldValue());
    }
    /**
     * Pushes the gui value to the field value (similar to git's push command).
     */
    public void pushFieldValue() {
        if (!isInitialized()) return;
        field.setFieldValue(push());
    }
    
    public Field<T> getField() {
        return field;
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
