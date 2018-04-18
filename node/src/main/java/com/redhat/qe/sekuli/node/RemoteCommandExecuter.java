package com.redhat.qe.sekuli.node;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.sikuli.api.robot.desktop.DesktopScreen;

import com.google.common.collect.ImmutableMap;
import com.redhat.qe.sekuli.common.exceptions.MissingMandatoryFieldsException;
import com.redhat.qe.sekuli.common.exceptions.TargetImageNotAvailableException;
import com.redhat.qe.sekuli.common.model.FileBase64;
import com.redhat.qe.sekuli.common.model.ImageBase64;
import com.redhat.qe.sekuli.common.model.RemoteScreenLocation;
import com.redhat.qe.sekuli.common.model.RemoteScreenRegion;
import com.redhat.qe.sekuli.common.model.SekuliCommand;
import com.redhat.qe.sekuli.common.model.SekuliRemoteResponse;
import com.redhat.qe.sekuli.common.model.SekuliRemoteResponse.RESULT;
import com.redhat.qe.sekuli.common.model.Value;
import com.redhat.qe.sekuli.common.remote.RemoteFile;
import com.redhat.qe.sekuli.common.remote.RemoteKeyboard;
import com.redhat.qe.sekuli.common.remote.RemoteMouse;
import com.redhat.qe.sekuli.common.remote.RemoteScreen;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class RemoteCommandExecuter {
    // parameters
    public static final String TARGET_NAME = "targetName";
    public static final String BASE_64_STRING = "base64String";
    public static final String VALUE = "value";
    public static final String FILE_PATH = "path";
    public static final String EXTENSION = "extension";
    public static final String SCREEN_ID = "screenId";
    public static final String COUNT = "count";

    // remote resources
    private static final RemoteKeyboard KEYBOARD = new RemoteKeyboard();
    private static final RemoteMouse MOUSE = new RemoteMouse();
    private static final RemoteScreen SCREEN_REGION = new RemoteScreen(MOUSE);
    private static final RemoteFile FILE = new RemoteFile();

    private static final Logger _logger = Logger.getLogger(RemoteCommandExecuter.class.getName());

    private SekuliCommand cmd;
    private SekuliRemoteResponse cmdResponse = new SekuliRemoteResponse();

    public RemoteCommandExecuter(SekuliCommand command) {
        this.cmd = command;
    }

    public SekuliRemoteResponse execute() {
        long startTime = System.currentTimeMillis();
        try {
            switch (cmd.getModule()) {
                case "screen":
                    executeScreen();
                    break;
                case "mouse":
                    executeMouse();
                    break;
                case "keyboard":
                    executeKeyboard();
                    break;
                case "file":
                    executeFile();
                    break;
                default:
                    updateCommandNotAvailable();
                    break;
            }
        } catch (TargetImageNotAvailableException ex) {
            _logger.log(Level.WARNING, "Error: [" + ex.getMessage() + "], Command:[" + cmd.toString() + "]");
            cmdResponse.setResult(RESULT.FAILED);
            cmdResponse.setError(ex.getMessage());
        } catch (MissingMandatoryFieldsException ex) {
            _logger.log(Level.WARNING, "Error: [" + ex.getMessage() + "], Command:[" + cmd.toString() + "]");
            cmdResponse.setResult(RESULT.FAILED);
            cmdResponse.setError(ex.getMessage() + " [" + cmd.getParameters() + "]");
        } catch (Exception ex) {
            _logger.log(Level.SEVERE, "Command[" + cmd.toString() + "]", ex);
            cmdResponse.setResult(RESULT.ERROR);
            cmdResponse.setError(ex.getMessage());
            cmdResponse.setThrowable(ex);
        }
        if (cmdResponse.getResult() == null) {
            cmdResponse.setResult(RESULT.SUCCESS);
        }
        cmdResponse.setTimeTaken(System.currentTimeMillis() - startTime);
        return cmdResponse;
    }

    private void executeScreen() throws IOException {
        switch (cmd.getCommand()) {
            case "setScreen":
                SCREEN_REGION.setScreen(new DesktopScreen(cmd.getParameters().getInt(SCREEN_ID)));
                cmdResponse.setResponse(SCREEN_REGION.getScreen());
                break;
            case "addTarget":
                cmdResponse.setResponse(ImmutableMap.of(COUNT,
                        SCREEN_REGION.addTarget(ImageBase64.get(cmd.getParameters()))));
                break;
            case "listTargets":
                cmdResponse.setResponse(SCREEN_REGION.listTargets());
                break;
            case "clearAllTargets":
                cmdResponse.setResponse(ImmutableMap.of(COUNT, SCREEN_REGION.clearAllTargets()));
                break;
            case "find":
                cmdResponse.setResponse(SCREEN_REGION.find(cmd.getParameters().getString(TARGET_NAME)));
                break;
            case "findAll":
                cmdResponse.setResponse(SCREEN_REGION.findAll(cmd.getParameters().getString(TARGET_NAME)));
                break;
            case "captureBase64":
                cmdResponse.setResponse(ImmutableMap.of(BASE_64_STRING,
                        SCREEN_REGION.captureBase64(RemoteScreenRegion.get(cmd.getParameters()))));
                break;
            case "click":
                SCREEN_REGION.click(cmd.getParameters().getString(TARGET_NAME));
                break;
            case "getScreen":
                cmdResponse.setResponse(SCREEN_REGION.getScreen());
                break;
            case "getScreens":
                cmdResponse.setResponse(ImmutableMap.of(COUNT, DesktopScreen.getNumberScreens()));
                break;
            default:
                updateCommandNotAvailable();
                break;
        }
    }

    private void executeMouse() {
        switch (cmd.getCommand()) {
            case "clickRegion":
                MOUSE.click(RemoteScreenRegion.get(cmd.getParameters()));
                break;
            case "clickLocation":
                MOUSE.click(RemoteScreenLocation.get(cmd.getParameters()));
                break;
            case "location":
                cmdResponse.setResponse(MOUSE.getLocation());
                break;
            default:
                updateCommandNotAvailable();
                break;
        }
    }

    private void executeKeyboard() {
        switch (cmd.getCommand()) {
            case "copy":
                cmdResponse.setResponse(new Value(KEYBOARD.copy()));
                break;
            case "paste":
                KEYBOARD.paste(cmd.getParameters().getString(VALUE));
                break;
            case "type":
                KEYBOARD.type(cmd.getParameters().getString(VALUE));
                break;
            case "selectAll":
                KEYBOARD.selectAll();
                break;
            default:
                updateCommandNotAvailable();
                break;
        }
    }

    private void executeFile() throws IOException {
        switch (cmd.getCommand()) {
            case "read":
                cmdResponse.setResponse(FILE.read(cmd.getParameters().getString(FILE_PATH)));
                break;
            case "save":
                cmdResponse.setResponse(ImmutableMap.of(FILE_PATH, FILE.save(FileBase64.get(cmd.getParameters()))));
                break;
            case "saveZip":
                cmdResponse.setResponse(ImmutableMap.of(FILE_PATH,
                        FILE.saveZip(cmd.getParameters().getString(BASE_64_STRING))));
                break;
            default:
                updateCommandNotAvailable();
                break;
        }
    }

    private void updateCommandNotAvailable() {
        cmdResponse.setResult(RESULT.FAILED);
        cmdResponse.setError(String.format("Specified command not found! [%s]", cmd.toString()));
    }
}
