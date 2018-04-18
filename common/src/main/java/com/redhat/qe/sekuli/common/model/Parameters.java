package com.redhat.qe.sekuli.common.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class Parameters extends HashMap<String, Object> {
    /**  */
    private static final long serialVersionUID = -4979742502287082795L;

    public Integer getInt(String key) {
        return getInt(key, null);
    }

    public Integer getInt(String key, Integer defaultValue) {
        String value = getString(key, defaultValue);
        return value != null ?
                getDouble(key, defaultValue == null ? null : defaultValue.doubleValue()).intValue() : null;
    }

    public Float getFloat(String key) {
        return getFloat(key, null);
    }

    public Float getFloat(String key, Float defaultValue) {
        String value = getString(key, defaultValue);
        return value != null ? Float.parseFloat(getString(key, defaultValue)) : null;
    }

    public Double getDouble(String key) {
        return getDouble(key, null);
    }

    public Double getDouble(String key, Double defaultValue) {
        String value = getString(key, defaultValue);
        return value != null ? Double.parseDouble(value) : null;
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, Object defaultValue) {
        Object value = getParameter(key, defaultValue);
        return value != null ? String.valueOf(value) : null;
    }

    public Object getParameter(String key) {
        return getParameter(key, null);
    }

    @SuppressWarnings("unchecked")
    public Object getParameter(String key, Object defaultValue) {
        String[] keys = key.split("\\.");
        Object value = null;
        Map<String, Object> _source = this;
        int keysSize = keys.length;
        while (keysSize > 0) {
            if (keysSize == 1) {
                value = _source.get(keys[keys.length - keysSize]);
                break;
            } else {
                _source = (Map<String, Object>) _source.get(keys[keys.length - keysSize]);
                keysSize--;
            }
            if (_source == null) {
                break;
            }
        }
        if (value != null) {
            return value;
        }
        return defaultValue;
    }
}
