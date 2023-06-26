# VariableChimp for JMonkeyEngine3
VariableChimp allows developers to change fields during runtime with an easy-to-use interface. This can significantly boost development time because programmers would no longer have to restart the program every time they want to change a field value.

# Quick Start
First, initialize VariableChimp using the `VarChimp` class.
```
VarChimp.initialize(application);
```
Then, register a field for changes.
```
VarChimp.get().register(new Variable(myObject, double.class, new Pull("getSpeed"), new Push("setSpeed")));
```
The `Variable` class is responsible for maitaining the field you want to change.
Its arguments are as follows:
* The owner of the field you want to change (we used myObject).
* The type of the field you want to change (we used double.class).
* The puller (updates the gui to reflect the field).
* The pusher (updates the field to reflect the gui).
The argument passed into Pull and Push is the same as a getter/setter pair that myObject should have.
When you update the gui or field using Pull or Push, they will use the method corresponding to the string you gave them.

# Dependencies
* JMonkeyEngine 3.6 (probably works for earlier versions as well)
* Lemur 1.16+
* JDK 8+
