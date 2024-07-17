package webserver.api.writepost;

import model.post.PostDAO;
import webserver.api.FunctionHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.enums.StatusCode;
import webserver.http.response.PageBuilder;
import webserver.session.SessionDAO;
import webserver.util.BodyContentDecomposision;
import webserver.util.FileData;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;


public class WritePost implements FunctionHandler {
    //singleton pattern
    private static FunctionHandler single_instance = null;
    public static synchronized FunctionHandler getInstance()
    {
        if (single_instance == null)
            single_instance = new WritePost();

        return single_instance;
    }

    static String root = "./src/main/resources/post/images/";

    @Override
    public HttpResponse function(HttpRequest request) throws IOException {
        String contentType = request.getHeaders().get("content-type");
        String boundaryPrefix = "boundary=";
        int boundaryIndex = contentType.indexOf(boundaryPrefix);
        if (boundaryIndex != -1) {
            String boundary = contentType.split(boundaryPrefix)[1].trim();
            String text = null;
            String imgpath = null;

            Map<String, FileData> decomposed = BodyContentDecomposision.decompose(request.getBody(), boundary);


            // Process the parts as needed (example: save image part to file)
            for (Map.Entry<String, FileData> part : decomposed.entrySet()) {
                String partName = part.getKey();
                FileData fileData = part.getValue();
                if (partName.equals("image")) {
                    imgpath = root + fileData.getFileName();
                    Files.write(Paths.get(imgpath), fileData.getData());
                }else if(partName.equals("text")){
                    text = new String(fileData.getData());
                }
            }

            PostDAO postDAO = new PostDAO();
            SessionDAO sessionDAO = new SessionDAO();
            if(text !=null)
                postDAO.insertPost(text, imgpath, sessionDAO.findSession(request.getSessionid()));

            return new HttpResponse.ResponseBuilder(302)
                    .addheader("Location", "http://localhost:8080/")
                    .addheader("Content-Type", "text/html; charset=utf-8")
                    .build();
        } else {
            // Return an empty string or handle the error appropriately
            return new HttpResponse.ResponseBuilder(400)
                    .addheader("Content-Type", "text/html; charset=utf-8")
                    .setBody(PageBuilder.buildErrorPage(StatusCode.CODE400))
                    .build();
        }
    }
}
