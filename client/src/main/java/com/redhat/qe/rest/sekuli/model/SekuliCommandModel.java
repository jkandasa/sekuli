package com.redhat.qe.rest.sekuli.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

@Data
@Builder
@ToString
public class SekuliCommandModel {
    private String module;
    private String command;
    private Object parameters;
}
