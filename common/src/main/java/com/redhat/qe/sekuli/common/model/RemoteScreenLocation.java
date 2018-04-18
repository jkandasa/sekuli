package com.redhat.qe.sekuli.common.model;

import org.sikuli.api.DefaultScreenLocation;
import org.sikuli.api.Screen;
import org.sikuli.api.robot.desktop.DesktopScreen;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class RemoteScreenLocation extends DefaultScreenLocation {
    public RemoteScreenLocation(Screen screenRef, int x, int y) {
        super(screenRef, x, y);
    }

    public static RemoteScreenLocation get(Parameters parameters) {
        if (!parameters.isEmpty()) {
            return new RemoteScreenLocation(
                    new DesktopScreen(parameters.getInt("screen.screenId", 0)),
                    parameters.getInt("x"),
                    parameters.getInt("y"));
        } else {
            return null;
        }
    }
}
