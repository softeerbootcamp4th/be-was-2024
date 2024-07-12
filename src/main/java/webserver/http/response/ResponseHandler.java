package webserver.http.response;

import exception.NotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.PluginLoader;
import webserver.http.request.Request;

import java.io.IOException;
import java.util.Optional;

import static util.Utils.getFile;

public class ResponseHandler {
    public static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);

    public final PluginLoader pluginLoader;

    public ResponseHandler(PluginLoader pluginLoader){
        this.pluginLoader = pluginLoader;
    }

    public Response response(Request request) throws IOException {
        Response response = new Response.Builder(Status.NOT_FOUND).build();

        try {
            Optional<Object> returnValue = pluginLoader.runPlugin(request.getMethod().getMethodName() + " " + request.getPath(), request);
            if (returnValue.isPresent()) {
                if (returnValue.get() instanceof Response) response = (Response) returnValue.get();
            }
        } catch (NotExistException e) {
            response = new Response.Builder(Status.OK)
                    .addHeader("Content-Type", getContentType(request.getExtension()) + ";charset=utf-8")
                    .body(getFile(request.getPath()))
                    .build();
        }

        return response;
    }

    private static String getContentType(String extension){
        return switch (extension){
            case "html" -> "text/html";
            case "css" -> "text/css";
            case "js" -> "text/javascript";
            case "ico", "png" -> "image/png";
            case "jpg" -> "image/jpg";
            case "svg" -> "image/svg+xml";
            default -> throw new IllegalStateException("Unexpected value: " + extension);
        };
    }

}
