package com.redhat.qe.rest.sekuli;

import com.google.common.collect.ImmutableMap;
import com.redhat.qe.rest.core.ClientInfo;
import com.redhat.qe.rest.sekuli.model.SekuliCommandModel;
import com.redhat.qe.sekuli.common.model.FileBase64;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class FileClient extends SekuliBase {

    public FileClient(ClientInfo clientInfo) {
        super(clientInfo);
    }

    private SekuliCommandModel getCommand(String command, Object parameters) {
        return getCommand("file", command, parameters);
    }

    public FileBase64 read(String filepath) {
        return (FileBase64) doPostReturnCustom(getCommand("read", ImmutableMap.of("path", filepath)), FileBase64.class);
    }

    public String save(FileBase64 fileBase64) {
        return doPostReturnString(getCommand("save", fileBase64), "path");
    }

    public String saveZip(String base64Zip) {
        return doPostReturnString(getCommand("saveZip", ImmutableMap.of("base64String", base64Zip)), "path");
    }
}
