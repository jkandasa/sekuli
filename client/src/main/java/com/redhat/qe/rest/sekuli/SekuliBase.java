package com.redhat.qe.rest.sekuli;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import com.redhat.qe.rest.core.ClientInfo;
import com.redhat.qe.rest.core.RestHeader;
import com.redhat.qe.rest.core.RestHttpClient;
import com.redhat.qe.rest.core.RestHttpResponse;
import com.redhat.qe.rest.sekuli.SekuliClient.CONNECTION_TYPE;
import com.redhat.qe.rest.sekuli.model.SekuliCommandModel;
import com.redhat.qe.rest.sekuli.model.SekuliResponseModel;
import com.redhat.qe.sekuli.common.SekuliCommonUtils;
import com.redhat.qe.sekuli.common.model.FileBase64;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class SekuliBase extends RestHttpClient {

    protected String hostname;
    protected int port;
    protected String sessionId;
    protected RestHeader header;

    private String url;

    protected String api = null;

    public SekuliBase(ClientInfo clientInfo) {
        super(clientInfo.getTrustHostType());

        hostname = clientInfo.getHostname();
        port = clientInfo.getPort();
        sessionId = clientInfo.getSessionId();

        if (clientInfo.getConnectionType() == CONNECTION_TYPE.NODE) {
            api = SekuliClient.SIKULI_NODE_PATH;
        } else {
            api = MessageFormat.format(SekuliClient.SIKULI_GRID_PATH, sessionId);
        }

        url = MessageFormat.format("http://{0}:{1}/{2}", hostname, String.valueOf(port), api);

        if (clientInfo.getHeader() != null) {
            header = clientInfo.getHeader();
        } else {
            header = RestHeader.getDefault();
        }
        header.addJsonContentType();
        header.addAuthorization(clientInfo.getUsername(), clientInfo.getPassword());

    }

    protected String root() {
        return url;
    }

    protected SekuliCommandModel getCommand(String module, String command, Object parameters) {
        return SekuliCommandModel.builder()
                .module(module)
                .command(command)
                .parameters(parameters)
                .build();
    }

    @SuppressWarnings("unchecked")
    public Object doPostReturnCustom(SekuliCommandModel cmd, Class<?> parametrizedClazz) {
        RestHttpResponse response = doPost(root(), header, SekuliCommonUtils.toJson(cmd), STATUS_CODE.OK.getCode());
        SekuliResponseModel<FileBase64> cmdResponse = (SekuliResponseModel<FileBase64>) readValue(
                response.getEntity(), simpleResolver().get(SekuliResponseModel.class, parametrizedClazz));
        return cmdResponse.getEntity();
    }

    @SuppressWarnings("unchecked")
    public Object doPostReturnCollection(SekuliCommandModel cmd, Class<?> parametrizedClazz) {
        RestHttpResponse response = doPost(root(), header, SekuliCommonUtils.toJson(cmd), STATUS_CODE.OK.getCode());
        SekuliResponseModel<Object> cmdResponse = (SekuliResponseModel<Object>) readValue(
                response.getEntity(), simpleResolver().get(SekuliResponseModel.class, List.class, parametrizedClazz));
        return cmdResponse.getEntity();
    }

    @SuppressWarnings("unchecked")
    protected Object doPostReturnObject(SekuliCommandModel cmd, String key, Class<?> parametrizedClazz) {
        RestHttpResponse response = doPost(root(), header, SekuliCommonUtils.toJson(cmd), STATUS_CODE.OK.getCode());
        SekuliResponseModel<Map<String, Object>> cmdResponse = (SekuliResponseModel<Map<String, Object>>) readValue(
                response.getEntity(),
                simpleResolver().get(SekuliResponseModel.class, Map.class, String.class, parametrizedClazz));
        return cmdResponse.getEntity().get(key);
    }

    protected Integer doPostReturnInt(SekuliCommandModel cmd, String key) {
        return (Integer) doPostReturnObject(cmd, key, Integer.class);
    }

    protected String doPostReturnString(SekuliCommandModel cmd, String key) {
        return (String) doPostReturnObject(cmd, key, String.class);
    }

    protected void doPostReturnVoid(SekuliCommandModel cmd) {
        doPost(root(), header, SekuliCommonUtils.toJson(cmd), STATUS_CODE.OK.getCode());
    }

}
