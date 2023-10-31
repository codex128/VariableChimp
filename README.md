# VariableChimp for JMonkeyEngine3
VariableChimp allows developers to change fields during runtime using an in-program interface. It is designed to work out-of-the-box and be as easy to use as possible.

# Download
Go ahead and download the latest stable version of VariableChimp from the [releases](https://github.com/codex128/VariableChimp/releases),<br>
or you can download the source code and build it yourself.

You also need to download a couple dependencies:
* [JMonkeyEngine 3.6](https://github.com/jMonkeyEngine/jmonkeyengine)
* [Lemur 1.16+](https://github.com/jMonkeyEngine-Contributions/Lemur)

# Quick Start
Initialize VariableChimp using the `VarChimp` class (make sure Lemur's `GuiGlobals` is initialized first).
```java
VarChimp.initialize(application);
```
Then, register a Variable for changes. This code registers a `Var` which is used to change the local translation of a spatial.
```java
Spatial mySpatial = new Node("my-spatial");
Variable v = new Var(mySpatial, Vector3f.class, new Pull("getLocalTranslation"), new Push("setLocalTranslation"));
VarChimp.get().register(v);
```
In the program, press **F1** to toggle the VariableChimp GUI. Edit the position of `mySpatial` using the sliders under "setLocalTranslation," then press the "Push" button directly below. Congratulations! You have just changed the position of `mySpatial`!

### What are `Variable` and `Var`?
`Variable` is an interface for tracking and updating a field. `Var` is the class implementation of `Variable`.

### What do each argument do?
* The first argument is the object which stores the field we want to edit. This is known as the *subject*.
* The second argument indicates what data type we're working with. In this case it's Vector3f.
* The third and fourth arguments take Push/Pull objects which handle any pushing and pulling that will occur.

### What is pulling?
Pulling, like the git command, is applying the field value to the GUI, so that the GUI displays whatever the field is. In VariableChimp, the `Pull` class handles the pulling; it takes one argument, a string, which indicates a getter method belonging to the subject that returns the field we want to edit.

So when you press the "Pull" button in the GUI, it fetches the field value from the world and displays it on the GUI.

### What is pushing?
Pushing is exactly the opposite of pulling. Instead of applying the field to the GUI, we apply the GUI to the field (so that the field is whatever the GUI displays). The `Push` class handles pushing; it takes one argument, a string, which indicates a setter method belonging to the subject that sets the field we want to edit.

So when you press the "Push" button in the GUI, it takes the value displayed on the GUI and applies it to the world.

### Variable Groups
Putting variables in the same group is a great way to quickly access that set of variables again, for whatever you need. The `Var` class has a setter for the group, or you can use a constructor to set it. Variables can change groups at any time.

Variable has a setter method for the group name.
```java
variable.setVariableGroup("my-group");
```
Or `Var` has constructors that set the group.
```java
Variable v = new Var("my-group", myObject, int.class, new Pull("getNumber"), new Push("setNumber"));
```
If you do not set the group of the variable, the default group will be assigned when the variable is registered. Accessing and changing the default group can be done through VarChimp.
```java
VarChimp.get().getDefaultGroup();
VarChimp.get().setDefaultGroup("my-group");
```

### Direct Field Access
Normally, `Push` and `Pull` access getters and setters in order to change fields. However, `FieldPull` and `FieldPush` directly access the field of the subject. They each require one string, which should be the exact name of the field. This can be useful when there aren't getter/setter methods already available.
```java
new Var(myObject, int.class, new FieldPull("num"), new FieldPush("num"));
```

### Faster ways to create Vars
Edits field through getter and setter
```java
new Var(myObject, int.class, "getNumber", "setNumber");
```
Directly accesses field "num" on both pushes and pulls 
```java
new Var(myObject, int.class, "num");
```
Applies a group name (and a subject, optionally) to a list of `Vars`.
```java
Var.create("my-group", new Var(myObject, int.class, "num"), new Var(myObject2, int.class, "num"));
Var.create("my-group", myObject, new Var(int.class, "num"), new Var(double.class, "score"));
```

# More Advanced Usage

### Variable Caching
VariableChimp allows you to temporarily cache variables, then apply them to a different object. This is useful if you want to apply a group of variables to another subject.

To cache a group:
```java
VarChimp.get().cacheGroup("my-group");
```
To apply those variables to `myObject`:
```java
VarChimp.get().applyCache("my-group", myObject);
```
If you'd like to create new variables only if there is no cache to apply:
```java
VarChimp.get().applyCacheOrElse("my-group", myObject, () -> {
    VarChimp.get().register(new Var("my-group", myObject, int.class, "num"));
});
```
> **Important!**<br>
> By default, whenever you cache a variable, it is automatically unregistered, and whenever you apply a cache, it gets removed.

# Thanks
* [JMonkeyEngine Team](https://jmonkeyengine.org/) for making a great 3D java engine, and answering my questions (even when they were dumb :P).
* [pspeed42](https://github.com/pspeed42) for developing the Lemur library.
