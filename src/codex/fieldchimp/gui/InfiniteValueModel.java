/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.fieldchimp.gui;

import com.simsilica.lemur.RangedValueModel;
import com.simsilica.lemur.core.VersionedReference;

/**
 *
 * @author gary
 */
public class InfiniteValueModel implements RangedValueModel {
    
    private long version;
    private double value = 0f;
    private double min = 0f, max = 1f;
    private boolean minEnabled = true, maxEnabled = true;
    
    public InfiniteValueModel() {}
    
    @Override
    public void setValue(double val) {
        if (value == val) return;
        value = val;
        version++;
        constrain();
    }
    @Override
    public double getValue() {
        return value;
    }
    @Override
    public void setPercent(double val) {
        throw new UnsupportedOperationException("Does not support percentages!");
    }
    @Override
    public double getPercent() {
        throw new UnsupportedOperationException("Does not support percentages!");
    }
    @Override
    public void setMaximum(double max) {
        if (this.max == max) return;
        this.max = max;
        version++;
        constrain();
    }
    @Override
    public double getMaximum() {
        return max;
    }
    @Override
    public void setMinimum(double min) {
        if (this.min == min) return;
        this.min = min;
        version++;
        constrain();
    }
    @Override
    public double getMinimum() {
        return min;
    }
    @Override
    public long getVersion() {
        return version;
    }
    @Override
    public Double getObject() {
        return getValue();
    }
    @Override
    public VersionedReference<Double> createReference() {
        return new VersionedReference<Double>(this);
    }
    
    public void enableMinimum(boolean enable) {
        minEnabled = enable;
    }
    public void enableMaximum(boolean enable) {
        maxEnabled = enable;
    }
    public boolean isMinimumEnabled() {
        return minEnabled;
    }
    public boolean isMaximumEnabled() {
        return maxEnabled;
    }
    
    private void constrain() {
        if (minEnabled && value < min) value = min;
        if (maxEnabled && value > max) value = max;
    }
    
}
