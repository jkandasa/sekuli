package com.redhat.qe.rest.sekuli.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.qe.sekuli.common.model.SekuliRemoteResponse.RESULT;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

@Data
@ToString
@NoArgsConstructor
public class SekuliResponseModel<T> {
    private RESULT result;
    private String error;
    private Throwable throwable;
    private Long timeTaken;

    @JsonProperty("response")
    private T entity;

}
