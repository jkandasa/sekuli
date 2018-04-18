package com.redhat.qe.rest.sekuli;

import com.redhat.qe.rest.core.ClientInfo;
import com.redhat.qe.rest.core.RestHeader;
import com.redhat.qe.rest.core.RestHttpClient.TRUST_HOST_TYPE;
import com.redhat.qe.rest.sekuli.SekuliClient.CONNECTION_TYPE;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class SekuliClientBuilder {
    private String hostname;
    private int port;
    private String sessionId;
    private String username;
    private String password;
    private CONNECTION_TYPE connectionType;
    private TRUST_HOST_TYPE trustHostType;
    private RestHeader header;

    public SekuliClientBuilder hostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public SekuliClientBuilder port(int port) {
        this.port = port;
        return this;
    }

    public SekuliClientBuilder sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public SekuliClientBuilder basicAuthentication(String username, String password) {
        this.username = username;
        this.password = password;
        return this;
    }

    public SekuliClientBuilder addHeader(RestHeader header) {
        this.header = header;
        return this;
    }

    public SekuliClientBuilder trustHostType(TRUST_HOST_TYPE trustHostType) {
        this.trustHostType = trustHostType;
        return this;
    }

    public SekuliClientBuilder connectionType(CONNECTION_TYPE connectionType) {
        this.connectionType = connectionType;
        return this;
    }

    public SekuliClient build() {
        ClientInfo clientInfo = new ClientInfo(hostname, port, sessionId, username, password,
                connectionType == null ? CONNECTION_TYPE.GRID : connectionType,
                        trustHostType == null ? TRUST_HOST_TYPE.DEFAULT : trustHostType, header);
        return new SekuliClient(clientInfo);
    }
}
