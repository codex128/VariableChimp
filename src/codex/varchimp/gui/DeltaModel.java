/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp.gui;

/**
 * 
 * @author gary
 */
public class DeltaModel {
    
    double[] delta;
    int min = 0;
    
    public DeltaModel(double... delta) {
        setDelta(delta);
    }
    
    public void set(DeltaModel model) {
        delta = model.delta;
    }
    public void setDelta(double... delta) {
        this.delta = delta;
        verifyLength(min);
    }
    public void setDelta(int index, double delta) {
        this.delta[index] = delta;
    }
    public void setMinimumDeltaValues(int min) {
        this.min = min;
        verifyLength(this.min);
    }
    
    public double[] getDelta() {
        return delta;
    }
    public double getDelta(int index) {
        return index;
    }
    public double getLast() {
        return delta[delta.length-1];
    }
    public int getLength() {
        return delta.length;
    }
    public int getMinimumDeltaValues() {
        return min;
    }
    
    public void verifyLength(int min) {
        if (delta.length < min) {
            throw new IllegalStateException("Model does not contain enough delta values!");
        }
    }
    
}
