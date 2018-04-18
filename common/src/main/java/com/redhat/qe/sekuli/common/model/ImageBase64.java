package com.redhat.qe.sekuli.common.model;

import com.redhat.qe.sekuli.common.SekuliCommonUtils;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */
public class ImageBase64 {
    private String targetName;
    private String base64String;
    private Float similarity;

    public static ImageBase64 get(Object data) {
        return SekuliCommonUtils.GSON.fromJson(SekuliCommonUtils.toJson(data), ImageBase64.class);
    }

    public boolean isValid() {
        if (targetName == null) {
            return false;
        } else if (base64String == null) {
            return false;
        }
        return true;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getBase64String() {
        return base64String;
    }

    public void setBase64String(String base64String) {
        this.base64String = base64String;
    }

    public Float getSimilarity() {
        if (similarity == null) {
            similarity = 0.8F;
        }
        return similarity;
    }

    public void setSimilarity(Float similarity) {
        this.similarity = similarity;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TargetName:").append(targetName)
        .append(", base64String:")
        // do not pollute logs
        .append(base64String.length() > 20 ? (base64String.substring(0, 20) + "...") : base64String)
        .append(", Similarity:").append(similarity);
        return builder.toString();
    }
}
