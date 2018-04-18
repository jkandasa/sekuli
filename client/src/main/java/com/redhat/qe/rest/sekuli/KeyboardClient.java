package com.redhat.qe.rest.sekuli;

import com.google.common.collect.ImmutableMap;
import com.redhat.qe.rest.core.ClientInfo;
import com.redhat.qe.rest.sekuli.model.SekuliCommandModel;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class KeyboardClient extends SekuliBase {

    public KeyboardClient(ClientInfo clientInfo) {
        super(clientInfo);
    }

    private SekuliCommandModel getCommand(String command, Object parameters) {
        return getCommand("keyboard", command, parameters);
    }

    public void paste(String text) {
        doPostReturnVoid(getCommand("paste", ImmutableMap.of("value", text)));
    }

    public void type(String text) {
        doPostReturnVoid(getCommand("type", ImmutableMap.of("value", text)));
    }

    public String copy() {
        return doPostReturnString(getCommand("copy", null), "value");
    }

    public void selectAll() {
        doPostReturnVoid(getCommand("selectAll", null));
    }
}
