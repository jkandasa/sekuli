package com.redhat.qe.sekuli.common.model;

import org.sikuli.api.DefaultScreenRegion;
import org.sikuli.api.Screen;
import org.sikuli.api.robot.desktop.DesktopScreen;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class RemoteScreenRegion extends DefaultScreenRegion {

    public RemoteScreenRegion() {
        this(new DesktopScreen(0));
    }

    public RemoteScreenRegion(Screen screen) {
        super(screen);
    }

    public RemoteScreenRegion(Screen screen, int x, int y, int width, int height) {
        super(screen, x, y, width, height);
    }

    public static RemoteScreenRegion get(Parameters parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            RemoteScreenRegion region = new RemoteScreenRegion(
                    new DesktopScreen(parameters.getInt("screen.screenId", 0)),
                    parameters.getInt("x"),
                    parameters.getInt("y"),
                    parameters.getInt("width"),
                    parameters.getInt("height"));
            if (parameters.get("score") != null) {
                region.setScore(parameters.getFloat("score"));
            }
            return region;
        }
        return null;
    }

}
