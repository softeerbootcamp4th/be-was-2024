package webserver.http.response;

import exception.NotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.PluginMapper;
import webserver.http.Session;
import webserver.http.request.Request;

import java.io.IOException;
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
        Response response = new Response.Builder(Status.NOT_FOUND).build();

        try {
            Optional<Object> returnValue = runPlugin(request.getMethod(), request.getPath(), request);
            if (returnValue.isPresent()) {
                if (returnValue.get() instanceof Response) response = (Response) returnValue.get();
            }
        } catch (NotExistException e) {

            if(request.getPath().equals("/index.html")){

                String body = new String(getFile(request.getPath()));
                String replacedBody;

                if(request.isLogin()){
                    replacedBody = body.replace("{USERNAME}", Session.get(request.getSessionId()).getName());
                    replacedBody = replacedBody.replace("{LOGINBTN}", "<form action=\"/logout\" method=\"post\"><button type=\"submit\" class=\"btn btn_contained btn_size_s\">로그아웃</button></form>");
                }else {
                    replacedBody = body.replace("{USERNAME}", "");
                    replacedBody = replacedBody.replace("{LOGINBTN}", "<a class=\"btn btn_contained btn_size_s\" href=\"/login\">로그인</a>");
                }
                response = new Response.Builder(Status.OK)
                        .addHeader("Content-Type", getContentType(request.getExtension()) + ";charset=utf-8")
                        .body(replacedBody.getBytes())
                        .build();
                return response;
            }

            response = new Response.Builder(Status.OK)
                    .addHeader("Content-Type", getContentType(request.getExtension()) + ";charset=utf-8")
                    .body(getFile(request.getPath()))
                    .build();
        }

        return response;
    }

    private String getContentType(String extension){
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
    public Optional<Object> runPlugin(webserver.http.request.Method httpMethod, String path, Object... args) {
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
                e.printStackTrace();
            }
        }
        throw new NotExistException();
    }

}
