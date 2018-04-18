package com.redhat.qe.sekuli.common.model;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.redhat.qe.sekuli.common.SekuliCommonUtils;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class SekuliCommand {
    private String module;
    private String command;
    private Parameters parameters;

    public static SekuliCommand get(HttpServletRequest request) throws IOException {
        return SekuliCommonUtils.GSON.fromJson(request.getReader(), SekuliCommand.class);
    }

    public static SekuliCommand get(String data) throws IOException {
        return SekuliCommonUtils.GSON.fromJson(data, SekuliCommand.class);
    }

    public String getJson() {
        return SekuliCommonUtils.GSON.toJson(this);
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Parameters getParameters() {
        if (parameters == null) {
            parameters = new Parameters();
        }
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Module: ").append(module)
        .append(", Command: ").append(command)
        .append(", Parameters: ").append(getParameters());
        return builder.toString();
    }

}
