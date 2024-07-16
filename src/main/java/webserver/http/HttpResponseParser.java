package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.enums.HttpStatus;
import webserver.mapping.MappingHandler;
import webserver.util.FileContentReader;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponseParser {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponseParser.class);

    private final FileContentReader fileContentReader;
    private final MappingHandler mappingHandler;

    public HttpResponseParser(FileContentReader fileContentReader, MappingHandler mappingHandler) {
        this.fileContentReader = fileContentReader;
        this.mappingHandler = mappingHandler;
    }


    public void parseResponse(DataOutputStream dos, MyHttpRequest httpRequest) throws IOException {
        MyHttpResponse httpResponse;


        if (fileContentReader.isStaticResource(httpRequest.getPath())) {
            httpResponse = new MyHttpResponse(HttpStatus.OK);

            fileContentReader.readStaticResource(httpRequest.getPath(), httpResponse);
        } else {
            httpResponse = mappingHandler.mapping(httpRequest);
        }

        sendResponse(dos, httpResponse);
    }

    public void sendResponse(DataOutputStream dos, MyHttpResponse httpResponse) {
        try {
            dos.writeBytes(httpResponse.getVersion() + " " + httpResponse.getHttpStatus().value() + " " + httpResponse.getHttpStatus().getReasonPhrase() + " \r\n");
            for (String key : httpResponse.getHeaders().keySet()) {
                dos.writeBytes(key + ": " + httpResponse.getHeaders().get(key) + "\r\n");
            }
            dos.writeBytes("\r\n");
            dos.write(httpResponse.getBody(), 0, httpResponse.getBody().length);
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
