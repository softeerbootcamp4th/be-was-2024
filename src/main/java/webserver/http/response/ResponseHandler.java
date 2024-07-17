package webserver.http.response;

import exception.NotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.PluginMapper;
import webserver.http.request.HttpMethod;
import webserver.http.request.Request;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import static util.Utils.getFile;

public class ResponseHandler {
    public final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);

    public final PluginMapper pluginMapper;

    public ResponseHandler(PluginMapper pluginMapper){
        this.pluginMapper = pluginMapper;
    }

    public Response response(Request request) throws IOException {
        try {
            if (pluginMapper.isExistOnlyPath(request.getMethod(), request.getPath())) return new Response.Builder(Status.METHOD_NOT_ALLOWED).build();
            if (pluginMapper.isExist(request.getMethod(), request.getPath())) {
                Optional<Object> returnValue = runPlugin(request.getMethod(), request.getPath(), request);
                if (returnValue.isPresent()) {
                    if (returnValue.get() instanceof Response) return (Response) returnValue.get();
                }
            } else {
                return new Response.Builder(Status.OK)
                        .addHeader("Content-Type", getContentType(request.getExtension()) + ";charset=utf-8")
                        .body(getFile(request.getPath()))
                        .build();
            }
        }catch (NotExistException e) {
            return new Response.Builder(Status.NOT_FOUND).build();
        }catch (Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String stackTraceString = sw.toString();
            return new Response.Builder(Status.INTERNAL_SERVER_ERROR)
                    .body(stackTraceString)
                    .build();
        }
        return new Response.Builder(Status.OK).build();
    }

    public static String getContentType(String extension){
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

    // 플러그인 실행 메소드
    public Optional<Object> runPlugin(HttpMethod httpMethod, String path, Object... args) {
        Method method = pluginMapper.get(httpMethod, path);
        logger.debug(path);
        if (method != null) {
            try {
                logger.debug(method.getName());
                Class<?> declaringClass = method.getDeclaringClass();
                Object instance = declaringClass.getConstructor().newInstance();
                Object returnValue = method.invoke(instance, args);
                if (method.getReturnType().equals(Void.TYPE)) {
                    return Optional.empty();
                }
                return Optional.ofNullable(returnValue);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        else throw new NotExistException();
    }

}