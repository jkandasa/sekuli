package com.redhat.qe.sekuli.common.model;

import java.util.HashMap;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */
public class SekuliRemoteResponse {
    private RESULT result;
    private String error;
    private String throwable;
    private Long timeTaken;
    private Object response;

    public enum RESULT {
        SUCCESS,
        FAILED,
        ERROR,
        UNKNOWN_COMMAND;
    }

    public RESULT getResult() {
        return result;
    }

    public void setResult(RESULT result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = ExceptionUtils.getStackTrace(throwable);
    }

    public void setThrowable(String throwable) {
        this.throwable = throwable;
    }

    public Long getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Long timeTaken) {
        this.timeTaken = timeTaken;
    }

    public Object getResponse() {
        if (response != null) {
            response = new HashMap<String, Object>();
        }
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
