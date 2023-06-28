# VariableChimp for JMonkeyEngine3
VariableChimp allows developers to change fields during runtime using an in-program interface. This can significantly boost development time because programmers would no longer have to restart the program every time they want to tweak a field value.

# Quick Start
Initialize VariableChimp using the `VarChimp` class (make sure Lemur's `GuiGlobals` is initialized first).
```
VarChimp.initialize(application);
```
Then, register a Variable for changes. This code registers a `Var` which is used to change the local translation of a spatial.
```
Spatial mySpatial = new Node("my-spatial");
Variable v = new Var(mySpatial, Vector3f.class, new Pull("getLocalTranslation"), new Push("setLocalTranslation"));
VarChimp.get().register(v);
```
In the program, press **F1** to toggle the VariableChimp GUI.

### What are `Variable` and `Var`?
`Variable` is an interface for tracking and updating a field. `Var` is the class implementation of `Variable`.

### What do each of these arguments do?
* The first argument is the object which stores the field we want to edit. This is known as the *subject*.
* The second argument indicates what data type we're working with. In this case it's Vector3f.
* The third and fourth arguments take Push/Pull objects which handle any pushing and pulling that will occur.

### What is pulling?
Pulling, like the git command, is applying the field value to the GUI, so that the GUI displays whatever the field is. In VariableChimp, the `Pull` class handles the pulling; it takes one argument, a string, which indicates a getter method belonging to the subject that returns the field we want to edit.

### What is pushing?
Pushing is exactly the opposite of pulling. Instead of applying the field to the GUI, we apply the GUI to the field (so that the field is whatever the GUI displays). The `Push` class handles pushing; it takes one argument, a string, which indicates a setter method belonging to the subject that sets the field we want to edit.

### Direct Field Access
Normally, `Push` and `Pull` access getters and setters in order to change fields. However, `FieldPull` and `FieldPush` directly access the field of the subject. They each require one string, which should be the exact name of the field.

### Additional, Faster `Var` Constructors
```
// edits field through getter and setter
new Var(myObject, int.class, "getNumber", "setNumber");
// directly accesses field "num" on both pushes and pulls 
new Var(myObject, int.class, "num");
```

# More Advanced Usage

### Variable Groups
Putting variables in the same group is a great way to quickly access that set of variables, for whatever you need. The `Var` class has a setter for the group, or you can use a constructor to set it. Variables can change groups at any time.

### Variable Caching
VariableChimp allows you to temporarily cache variables, then apply them to a different object. This is useful if a state is replaced by another state, but you want to keep the same variables and their values.

To cache a group:
```
VarChimp.get().cacheGroup("my-variable-group");
```
To apply those variables to `myObject`:
```
VarChimp.get().applyCache("my-variable-group", myObject);
```
Applying variables actually creates new copies and deletes (by default) the old variables.

If you'd like to create new variables only if there is no cache to apply:
```
VarChimp.get().applyCacheOrElse("my-variable-group", myObject, () -> {
    VarChimp.get().register(new Var("my-variable-group", myObject, float.class, "myFloat"));
});
```

# Dependencies
* [JMonkeyEngine 3](https://github.com/jMonkeyEngine/jmonkeyengine) (only the latest stable version is being maintained)
* [Lemur 1.16+](https://github.com/jMonkeyEngine-Contributions/Lemur)
* JDK 8+
