package com.redhat.qe.rest.core;

import static com.google.common.base.Preconditions.checkNotNull;

import com.redhat.qe.rest.core.RestHttpClient.TRUST_HOST_TYPE;
import com.redhat.qe.rest.sekuli.SekuliClient.CONNECTION_TYPE;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

@Getter
@Builder
@ToString
public class ClientInfo {
    private final String hostname;
    private final int port;
    private final String sessionId;
    private final String username;
    private final String password;
    private final CONNECTION_TYPE connectionType;
    private final TRUST_HOST_TYPE trustHostType;
    private final RestHeader header;

    public ClientInfo(String hostname, int port, String sessionId, String username, String password,
            CONNECTION_TYPE connectionType, TRUST_HOST_TYPE trustHostType, RestHeader header) {
        this.hostname = checkNotNull(hostname);
        this.port = port;
        this.sessionId = sessionId;
        this.username = username;
        this.password = password;
        this.connectionType = connectionType;
        this.trustHostType = trustHostType;
        this.header = header;
    }
}
