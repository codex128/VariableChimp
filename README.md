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

# Terminology


# Dependencies
* JMonkeyEngine 3.6 (probably works for earlier versions as well)
* Lemur 1.16+
* JDK 8+
