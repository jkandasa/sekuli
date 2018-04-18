package com.redhat.qe.rest.sekuli;

import com.redhat.qe.rest.core.ClientInfo;
import com.redhat.qe.rest.sekuli.model.Location;
import com.redhat.qe.rest.sekuli.model.SekuliCommandModel;
import com.redhat.qe.sekuli.common.model.RemoteScreenRegion;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class MouseClient extends SekuliBase {

    public MouseClient(ClientInfo clientInfo) {
        super(clientInfo);
    }

    private SekuliCommandModel getCommand(String command, Object parameters) {
        return getCommand("mouse", command, parameters);
    }

    public Location location() {
        return (Location) doPostReturnCustom(getCommand("location", null), Location.class);
    }

    public void click(Location location) {
        doPostReturnVoid(getCommand("clickLocation", location));
    }

    public void click(RemoteScreenRegion region) {
        doPostReturnVoid(getCommand("clickRegion", region));
    }
}
