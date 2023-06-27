# VariableChimp for JMonkeyEngine3
VariableChimp allows developers to change fields during runtime with an easy-to-use interface. This can significantly boost development time because programmers would no longer have to restart the program every time they want to change a field value.

# Quick Start
First, initialize VariableChimp using the `VarChimp` class.
```
VarChimp.initialize(application);
```
Then, register a Variable for changes. This code registers a `Var` which is used to track the local translation of a spatial.
```
Spatial mySpatial = new Node("my-spatial");
Variable v = new Var(mySpatial, Vector3f.class, new Pull("getLocalTranslation"), new Push("setLocalTranslation"));
VarChimp.get().register(v);
```

### What is `Variable` and `Var`?
`Variable` is an interface for tracking and updating a field. `Var` is the class implementation of `Variable`.

### What do each of these arguments do?
* The first argument is the object which stores the field we want to edit. This is known as the *subject*.
* The second argument indicates what data type we're working with. In this case it's Vector3f.
* The third and fourth arguments take Push/Pull objects which handle any pushing and pulling that will occur.

### What is pulling?
Pulling, like the git command, is applying the field value to the GUI, so that the GUI displays whatever the field is. In VariableChimp, the `Pull` class handles the pulling; it takes one argument, a string, which indicates a getter method belonging to the subject that returns the field we want to edit.

### What is pushing?
Pushing is exactly the opposite of pulling. Instead of applying the field to the GUI, we apply the GUI to the field (so that the field is whatever the GUI displays). The `Push` class handles pushing; it takes one argument, a string, which indicates a setter method belonging to the subject that sets the field we want to edit.

# Dependencies
* [JMonkeyEngine 3.6+](https://github.com/jMonkeyEngine/jmonkeyengine) (probably works for earlier versions such as 3.5 as well)
* [Lemur 1.16+](https://github.com/jMonkeyEngine-Contributions/Lemur)
* JDK 8+
