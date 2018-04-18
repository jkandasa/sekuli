package com.redhat.qe.rest.sekuli;

import java.util.ArrayList;
import java.util.List;

import com.redhat.qe.rest.core.ClientInfo;
import com.redhat.qe.rest.sekuli.model.Screen;
import com.redhat.qe.rest.sekuli.model.SekuliCommandModel;
import com.redhat.qe.sekuli.common.model.ImageBase64;
import com.redhat.qe.sekuli.common.model.Parameters;
import com.redhat.qe.sekuli.common.model.RemoteScreenRegion;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class ScreenClient extends SekuliBase {

    public ScreenClient(ClientInfo clientInfo) {
        super(clientInfo);
    }

    private SekuliCommandModel getCommand(String command, Object parameters) {
        return getCommand("screen", command, parameters);
    }

    public Screen setScreen(int screenId) {
        return (Screen) doPostReturnCustom(getCommand("setScreen", Screen.builder().screenId(screenId).build()),
                Screen.class);
    }

    public Screen getScreen() {
        return (Screen) doPostReturnCustom(getCommand("getScreen", null), Screen.class);
    }

    public int getScreens() {
        return doPostReturnInt(getCommand("getScreens", null), "count");
    }

    @SuppressWarnings("unchecked")
    public List<String> listTargets() {
        return (List<String>) doPostReturnCollection(getCommand("listTargets", null), String.class);
    }

    public int addTarget(ImageBase64 imageBase64) {
        return doPostReturnInt(getCommand("addTarget", imageBase64), "count");
    }

    public int clearAllTargets() {
        return doPostReturnInt(getCommand("clearAllTargets", null), "count");
    }

    public RemoteScreenRegion find(String targetName) {
        ImageBase64 base64 = new ImageBase64();
        base64.setTargetName(targetName);
        SekuliCommandModel cmd = getCommand("find", base64);
        Parameters parameters = (Parameters) doPostReturnCustom(cmd, Parameters.class);
        RemoteScreenRegion region = RemoteScreenRegion.get(parameters);
        return region;
    }

    @SuppressWarnings("unchecked")
    public List<RemoteScreenRegion> findAll(String targetName) {
        ImageBase64 base64 = new ImageBase64();
        base64.setTargetName(targetName);
        SekuliCommandModel cmd = getCommand("findAll", base64);
        List<Parameters> parametersList = (List<Parameters>) doPostReturnCollection(cmd, Parameters.class);
        List<RemoteScreenRegion> regions = new ArrayList<RemoteScreenRegion>();
        for (Parameters parameters : parametersList) {
            regions.add(RemoteScreenRegion.get(parameters));
        }
        return regions;
    }

    public void click(String targetName) {
        ImageBase64 base64 = new ImageBase64();
        base64.setTargetName(targetName);
        doPostReturnVoid(getCommand("click", base64));
    }

    public ImageBase64 capture() {
        return capture(null);
    }

    public ImageBase64 capture(RemoteScreenRegion screenRegion) {
        return (ImageBase64) doPostReturnCustom(getCommand("captureBase64", screenRegion), ImageBase64.class);
    }

}
