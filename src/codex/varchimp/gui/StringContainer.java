/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp.gui;

import codex.varchimp.Variable;
import com.simsilica.lemur.TextField;

/**
 * Displays GUI for editing Strings.
 * 
 * @author gary
 */
public class StringContainer extends VariableContainer<String> {
    
    TextField input;
    
    /**
     *
     * @param variable
     */
    public StringContainer(Variable<String> variable) {
        super(variable);
    }
    
    /**
     *
     */
    @Override
    protected void initEditingGui() {
        input = editContainer.addChild(new TextField(""));
        setReference(input.getDocumentModel().createReference());
    }

    /**
     *
     * @param value
     */
    @Override
    protected void pull(String value) {
        input.getDocumentModel().setText(value);
    }

    /**
     *
     * @return
     */
    @Override
    protected String push() {
        return input.getDocumentModel().getText();
    }

    /**
     *
     * @return
     */
    @Override
    public Class getVariableType() {
        return String.class;
    }

    /**
     *
     * @param field
     * @return
     */
    @Override
    public VariableContainer create(Variable field) {
        return new StringContainer(field);
    }
    
}
