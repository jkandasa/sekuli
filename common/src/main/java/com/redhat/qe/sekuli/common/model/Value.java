package com.redhat.qe.sekuli.common.model;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */
public class Value {
    private Object value;

    public Value() {

    }

    public Value(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : null;
    }
}
