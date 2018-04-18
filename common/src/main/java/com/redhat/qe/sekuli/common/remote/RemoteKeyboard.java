package com.redhat.qe.sekuli.common.remote;

import org.sikuli.api.robot.desktop.DesktopKeyboard;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class RemoteKeyboard extends DesktopKeyboard {

    // key codes
    // https://www.cambiaresearch.com/articles/15/javascript-char-codes-key-codes
    // https://github.com/sikuli/sikuli/blob/develop/sikuli-script/src/main/java/org/sikuli/script/Key.java
    public void selectAll() {
        this.keyDown(17); // CTRL
        this.keyDown(65); // a
        this.keyUp(65);
        this.keyUp(17);
    }
}
