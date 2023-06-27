# VariableChimp for JMonkeyEngine3
VariableChimp allows developers to change fields during runtime with an easy-to-use interface. This can significantly boost development time because programmers would no longer have to restart the program every time they want to change a field value.

# Quick Start
First, initialize VariableChimp using the `VarChimp` class.
```
VarChimp.initialize(application);
```
Then, register a Variable for changes. This code registers a Variable which is used to change the location of a Node.
![code-snippet](https://github.com/codex128/VariableChimp/blob/master/assets/Textures/code-snippet.png?raw=true)

**What do each of these do?**
* `Variable` makes sure the corresponding GUI and field match (when requested). It is really the heart of the library.
* The first argument to Variable is the object which stores the field we want to edit. This is known as the *subject*.
* The second argument to Variable indicates what data type we're working with. In this case it's Vector3f.
* The third argument to Variable is the class which will handle *pulling*.
* The fourth argument to Variable is the class which will handle *pushing*.

**What is pulling?**
Pulling, like the git command, is applying the field value to the GUI, so that the GUI displays whatever the field is. In VariableChimp, the `Pull` class handles the pulling; it takes one argument, a string, which indicates a getter method belonging to the subject that returns the field we want to edit.

**What is pushing?**
Pushing is exactly the opposite of pulling. Instead of applying the field to the GUI, we apply the GUI to the field (so that the field is whatever the GUI displays). The `Push` class handles pushing; it takes one argument, a string, which indicates a setter method belonging to the subject that sets the field we want to edit.

# Customization
VariableChimp is designed to meet your needs, whatever they are. It is designed to support (with some effort) any data type imaginable.

**Creating Custom Variable GUI**
Any class that wants to display GUI for a variable type must extend `VariableContainer`.
```
public class MyCustomContainer extends VariableContainer<Double> {

    Slider slider;

    public MyCustomContainer(VariablePointer<Double> field) {
        super(field);
    }
    
    @Override
    protected void initEditingGui() {
        // create a simple slider gui
        slider = ...;
        // save the VersionedReference of the slider (very, very important!)
        setReference(slider.getModel().createReference());
    }
    @Override
    protected void pull(Double value) {
        // display the incoming value on the GUI
        slider.getModel().setValue(value);
    }
    @Override
    protected Double push() {
        // return whatever is being displayed on the GUI
        return scroller.getModel().getValue();
    }
    @Override
    public Class<Double> getVariableType() {
        // return the data type MyCustomContainer is working with
        return double.class;
    }
    @Override
    public VariableContainer create(VariablePointer variable) {
        // creates a new MyCustomContainer
        // this
        return new MyCustomContainer(variable);
    }    
}
```

# Dependencies
* JMonkeyEngine 3.6 (probably works for earlier versions as well)
* Lemur 1.16+
* JDK 8+
