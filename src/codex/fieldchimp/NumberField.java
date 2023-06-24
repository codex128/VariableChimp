/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.fieldchimp;

/**
 *
 * @author gary
 * @param <T>
 */
public class NumberField <T extends Number> extends AbstractGetterSetter<T> {
    
    private final Class<T> type;
    private T min, max;
    
    public NumberField(Object subject, Class<T> type, String getter, String setter) {
        super(subject, getter, setter);
        this.type = type;
    }
    
    @Override
    public Class<T> getFieldType() {
        return type;
    }
    @Override
    public T constrainFieldValue(T value) {
        return constrain(value, min, max);
    }
    
    private T constrain(T value, T min, T max) {
        if (compare(value, min) <= 0) return min;
        if (compare(value, max) >= 0) return max;
        return value;
    }
    public static int compare(Number n1, Number n2) {
        if (n1.getClass() != n2.getClass()) {
            throw new IllegalArgumentException("Both arguments must be the same type!");
        }
        if (n1 instanceof Integer) {
            int i1 = n1.intValue(), i2 = n2.intValue();
            if (i1 < i2) return -1;
            if (i1 > i2) return 1;
            return 0;
        }
        if (n1 instanceof Float) {
            float f1 = n1.floatValue(), f2 = n2.floatValue();
            if (f1 < f2) return -1;
            if (f1 > f2) return 1;
            return 0;
        }
        if (n1 instanceof Double) {
            double d1 = n1.doubleValue(), d2 = n2.doubleValue();
            if (d1 < d2) return -1;
            if (d1 > d2) return 1;
            return 0;
        }
        if (n1 instanceof Long) {
            long l1 = n1.longValue(), l2 = n2.longValue();
            if (l1 < l2) return -1;
            if (l1 > l2) return 1;
            return 0;
        }
        if (n1 instanceof Short) {
            short s1 = n1.shortValue(), s2 = n2.shortValue();
            if (s1 < s2) return -1;
            if (s1 > s2) return 1;
            return 0;
        }
        if (n1 instanceof Byte) {
            byte b1 = n1.byteValue(), b2 = n2.byteValue();
            if (b1 < b2) return -1;
            if (b1 > b2) return 1;
            return 0;
        }
        return 0;
    }
    
}
