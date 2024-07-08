package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.FileContentReader;
import webserver.mapping.MappingHandler;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponseParser {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponseParser.class);
    private final FileContentReader fileContentReader = FileContentReader.getInstance();
    private final MappingHandler mappingHandler = MappingHandler.getInstance();

    private static final HttpResponseParser instance = new HttpResponseParser();

    private HttpResponseParser() {
    }

    public static HttpResponseParser getInstance() {
        return instance;
    }


    public void parseResponse(DataOutputStream dos, MyHttpRequest httpRequest) throws IOException {
        MyHttpResponse httpResponse;

        if (fileContentReader.isStaticResource(httpRequest.getPath())) {
            httpResponse = fileContentReader.readStaticResource(httpRequest.getPath());
        } else {
            httpResponse = mappingHandler.mapping(httpRequest);
        }

        responseHeader(dos, httpResponse);
        responseBody(dos, httpResponse.getBody());
    }

    public void responseHeader(DataOutputStream dos, MyHttpResponse httpResponse) {
        try {
            dos.writeBytes(httpResponse.getVersion() + " " + httpResponse.getStatusCode() + " " + httpResponse.getStatusMessage() + " \r\n");
            for (String key : httpResponse.getHeaders().keySet()) {
                dos.writeBytes(key + ": " + httpResponse.getHeaders().get(key) + "\r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
