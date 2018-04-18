package com.redhat.qe.sekuli.common.model;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.redhat.qe.sekuli.common.SekuliCommonUtils;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class FileBase64 {
    private String path;
    private String base64String;

    public boolean isValid() {
        if (path == null) {
            return false;
        } else if (base64String == null) {
            return false;
        }
        return true;
    }

    public static FileBase64 get(Map<String, Object> data) {
        return SekuliCommonUtils.GSON.fromJson(SekuliCommonUtils.toJson(data), FileBase64.class);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBase64String() {
        return base64String;
    }

    public void setBase64String(String base64String) {
        this.base64String = base64String;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
