package com.redhat.qe.sekuli.grid;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.common.primitives.Ints;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class SekuliRequestProcessingClient {

    private static final Logger _logger = Logger.getLogger(SekuliRequestProcessingClient.class.getName());

    private static final String SIKULI_NODE = "http://{0}:{1}/extra";

    private final String endpoint;

    public SekuliRequestProcessingClient(String host, int port) {
        this(MessageFormat.format(SIKULI_NODE, host, String.valueOf(port)));
    }

    public SekuliRequestProcessingClient(String endpoint) {
        this.endpoint = endpoint;
    }

    private CloseableHttpClient provideHttpClient() {
        return HttpClients.createDefault();
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (CloseableHttpClient httpClient = provideHttpClient()) {
            HttpRequestBase httpRequest = createHttpRequest(request);
            CloseableHttpResponse extensionResponse = httpClient.execute(httpRequest);
            copy(extensionResponse, response);
        }
    }

    private void copy(HttpResponse source, HttpServletResponse target) {
        int statusCode = source.getStatusLine().getStatusCode();
        target.setStatus(statusCode);
        _logger.log(Level.FINE, String.format("Response from extension returned [%d] status code", statusCode));

        HttpEntity entity = source.getEntity();

        Header contentType = entity.getContentType();
        if (contentType != null) {
            target.setContentType(contentType.getValue());
            _logger.log(Level.FINE,
                    String.format("Response from extension returned [{%s}] content type", contentType.getValue()));
        }

        long contentLength = entity.getContentLength();
        target.setContentLength(Ints.checkedCast(contentLength));
        _logger.log(Level.FINE, String.format("Response from extension has [{%d}] content length", contentLength));

        _logger.info("Copying body content to original servlet response");
        try (InputStream content = entity.getContent();
                OutputStream response = target.getOutputStream()) {
            IOUtils.copy(content, response);
        } catch (IOException ex) {
            _logger.log(Level.SEVERE, "Failed to copy response body content", ex);
        }
    }

    private HttpRequestBase createHttpRequest(HttpServletRequest request) throws IOException {
        String method = request.getMethod();
        _logger.log(Level.FINE, String.format("Creating [{%s}] request to forward", method));
        HttpRequestBase httpRequestBase = HttpPost.METHOD_NAME.equals(method) ? createPostRequest(request) :
            HttpGet.METHOD_NAME.equals(method) ? new HttpGet() :
                HttpPut.METHOD_NAME.equals(method) ? new HttpPut() :
                    HttpDelete.METHOD_NAME.equals(method) ? new HttpDelete() : null;

                    if (httpRequestBase == null) {
                        throw new RuntimeException(MessageFormat.format("Method {0} is not supported.", method));
                    }
                    URI uri = URI.create(endpoint + SekuliGridHelper.trimSessionPath(request.getPathInfo()));
                    _logger.log(Level.FINE, String.format("Trimming session id from path, new path:[%s]", uri.toString()));
                    httpRequestBase.setURI(uri);
                    return httpRequestBase;
    }

    private HttpRequestBase createPostRequest(HttpServletRequest request) throws IOException {
        HttpPost httpPost = new HttpPost();
        InputStreamEntity entity = new InputStreamEntity(request.getInputStream(),
                request.getContentLength(),
                // some requests contain ContentType;Encoding and fails in validation.
                // So striping Encoding and retaining only ContentType
                ContentType.create((request.getContentType().split(";")[0])));
        httpPost.setEntity(entity);
        return httpPost;
    }

}
