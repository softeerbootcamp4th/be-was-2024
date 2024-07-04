package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequest;
import util.HttpResponse;

import db.Database;
import model.User;

import java.net.URLDecoder.*;
public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private static final String ROOT_DIRECTORY = "src/main/resources/static";
    private static final String DEFAULT_PAGE = "/index.html";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = createHttpRequest(in);
            HttpResponse response = createHttpResponse(out);

            handleRequest(request, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private HttpRequest createHttpRequest(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        return new HttpRequest(reader);
    }

    private HttpResponse createHttpResponse(OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        return new HttpResponse(dos);
    }

    private void handleRequest(HttpRequest request, HttpResponse response) {
        String url = request.getUrl();
        url = switch (url) {
            case "/" -> "/index.html";
            case "/register.html" -> "/registration/index.html";
            case "/login.html" -> "/login/index.html";
            case "/article.html" -> "/article/index.html";
            case "/comment.html" -> "/comment/index.html";
            case "/main.html" -> "/main/index.html";
            default -> url;
        };

        if(isDynamicRequest(request.getPath())){
            handleDynamicRequest(request, response);
        }else {
            request.setUrl(url);
            String contentType = request.getContentType();
            handleFileRequest(url, contentType, response);
        }
    }

    private boolean isDynamicRequest(String path) {
        return path.startsWith("/create") || path.startsWith("/update") || path.startsWith("/delete");
    }

    private void handleDynamicRequest(HttpRequest request, HttpResponse response) {
        String path = request.getPath();
        Map<String, String> queryParams = request.getQueryParams();
        if (path.startsWith("/create")) {
//            String userId = queryParams.get("userId");
//            String password = queryParams.get("password");
//            String name = queryParams.get("name");
//            String email = queryParams.get("email");
            //decode the url
            try {
                String userId = URLDecoder.decode(queryParams.get("userId"), "UTF-8");
                String password = URLDecoder.decode(queryParams.get("password"), "UTF-8");
                String name = URLDecoder.decode(queryParams.get("name"), "UTF-8");
                String email = URLDecoder.decode(queryParams.get("email"), "UTF-8");
                Database.addUser(new User(userId, password, name, email));
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage());
            }

            for(User user : Database.findAll()){
                logger.debug("user: {}", user.getUserId());
                logger.debug("user: {}", user.getPassword());
                logger.debug("user: {}", user.getName());
                logger.debug("user: {}", user.getEmail());
            }
            response.sendRedirect("/index.html");
        }
    }

    private void handleFileRequest(String url, String contentType, HttpResponse response) {
        File file = new File(ROOT_DIRECTORY + url);
        if (file.exists() && !file.isDirectory()) {
            byte[] body = readFileToByteArray(file);
            response.sendResponse(200, "OK", contentType, body);
        } else {
            send404Response(response);
        }
    }

    private void send404Response(HttpResponse response) {
        String body = "<html><body><h1>404 Not Found</h1></body></html>";
        response.sendResponse(404, "Not Found", "text/html", body.getBytes());
    }

    private byte[] readFileToByteArray(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            return data;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }
}
