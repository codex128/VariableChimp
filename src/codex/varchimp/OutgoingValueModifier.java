/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp;

/**
 *
 * @author gary
 * @param <T>
 */
public interface OutgoingValueModifier <T> {
    
    public T modify(T value);
    
}
