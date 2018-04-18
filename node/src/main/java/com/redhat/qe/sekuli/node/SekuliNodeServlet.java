package com.redhat.qe.sekuli.node;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openqa.grid.internal.GridRegistry;
import org.openqa.grid.web.servlet.RegistryBasedServlet;

import com.redhat.qe.sekuli.common.SekuliCommonUtils;
import com.redhat.qe.sekuli.common.model.SekuliCommand;
import com.redhat.qe.sekuli.common.model.SekuliRemoteResponse;
import com.redhat.qe.sekuli.common.model.SekuliRemoteResponse.RESULT;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class SekuliNodeServlet extends RegistryBasedServlet {
    /**  */
    private static final long serialVersionUID = -1613993657693189067L;
    private static final Logger _logger = Logger.getLogger(SekuliNodeServlet.class.getName());

    public SekuliNodeServlet() {
        this(null);
    }

    public SekuliNodeServlet(GridRegistry registry) {
        super(registry);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
    IOException {
        SekuliCommand command = SekuliCommand.get(request);
        _logger.log(Level.FINE, "Received a command:[" + command.toString() + "]");

        RemoteCommandExecuter commandExecuter = new RemoteCommandExecuter(command);
        SekuliRemoteResponse cmdResponse = commandExecuter.execute();

        if (cmdResponse.getResult() != RESULT.SUCCESS) {
            response.setStatus(400);// bad request
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(SekuliCommonUtils.toJson(cmdResponse));
        out.flush();
    }

}
