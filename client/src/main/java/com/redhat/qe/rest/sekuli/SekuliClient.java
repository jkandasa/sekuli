package com.redhat.qe.rest.sekuli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import com.redhat.qe.rest.core.ClientInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

@Slf4j
public class SekuliClient extends SekuliBase {
    public static final String SIKULI_GRID_PATH = "grid/admin/SekuliGridServlet/session/{0}/SekuliNodeServlet";
    public static final String SIKULI_NODE_PATH = "extra/SekuliNodeServlet";

    public enum CONNECTION_TYPE {
        GRID,
        NODE;
    }

    private ScreenClient screenClient;
    private MouseClient mouseClient;
    private KeyboardClient keyboardClient;
    private FileClient fileClient;

    public SekuliClient(ClientInfo clientInfo) {
        super(clientInfo);
        screenClient = new ScreenClient(clientInfo);
        mouseClient = new MouseClient(clientInfo);
        keyboardClient = new KeyboardClient(clientInfo);
        fileClient = new FileClient(clientInfo);
    }

    public ScreenClient screen() {
        return screenClient;
    }

    public MouseClient mouse() {
        return mouseClient;
    }

    public KeyboardClient keyboard() {
        return keyboardClient;
    }

    public FileClient file() {
        return fileClient;
    }

    public void saveToDisk(String base64Data, String path, String... more) {
        byte[] data = Base64.getDecoder().decode(base64Data);
        Path actualPath = Paths.get(path, more);
        try {
            Files.write(actualPath, data);
        } catch (IOException ex) {
            _logger.error("Exception,", ex);
            throw new RuntimeException(ex);
        }
    }

    public String loadFromDisk(String path, String... more) {
        try {
            return Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(path, more)));
        } catch (IOException ex) {
            _logger.error("Exception,", ex);
            throw new RuntimeException(ex);
        }
    }

}
