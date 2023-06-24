/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.fieldchimp;

/**
 *
 * @author gary
 */
public class FieldId {
    
    private static int nextId = 0;    
    private final int id;
    
    public FieldId() {
        id = nextId++;
    }
    
    public int getId() {
        return id;
    }
    @Override
    public String toString() {
        return "FieldId"+id;
    }
    
}
