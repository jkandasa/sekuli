package com.redhat.qe.sekuli.grid;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openqa.grid.internal.GridRegistry;
import org.openqa.grid.web.servlet.RegistryBasedServlet;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class SekuliGridServlet extends RegistryBasedServlet {

    /**  */
    private static final long serialVersionUID = 5243963014485014138L;
    private static final Logger _logger = Logger.getLogger(SekuliGridServlet.class.getName());

    public SekuliGridServlet() {
        this(null);
    }

    public SekuliGridServlet(GridRegistry registry) {
        super(registry);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SekuliRequestProcessingClient sekuliRequestProcessingClient;
        try {
            sekuliRequestProcessingClient = createExtensionClient(request.getPathInfo());
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return;
        }

        try {
            sekuliRequestProcessingClient.processRequest(request, response);
        } catch (IOException ex) {
            _logger.log(Level.SEVERE, "Exception during request forwarding", ex);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        }
    }

    private SekuliRequestProcessingClient createExtensionClient(String path) {
        _logger.info("Forwarding request with path: " + path);
        String sessionId = SekuliGridHelper.getSessionIdFromPath(path);
        _logger.info("Retrieving remote host for session: " + sessionId);

        SekuliGridHelper.refreshTimeout(getRegistry(), sessionId);

        URL remoteHost = SekuliGridHelper.getRemoteHostForSession(getRegistry(), sessionId);
        String host = remoteHost.getHost();
        int port = remoteHost.getPort();
        _logger.info("Remote host retrieved: " + host + ":" + port);
        return new SekuliRequestProcessingClient(host, port);
    }
}
