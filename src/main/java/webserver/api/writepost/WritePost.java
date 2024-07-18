package webserver.api.writepost;

import model.post.PostDAO;
import webserver.api.FunctionHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.enums.Extension;
import webserver.http.enums.StatusCode;
import webserver.http.response.PageBuilder;
import webserver.session.SessionDAO;
import webserver.util.BodyContentDecomposision;
import webserver.util.FileData;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Post를 업로드하는 클래스
 */
public class WritePost implements FunctionHandler {
    //singleton pattern
    private static FunctionHandler single_instance = null;
    public static synchronized FunctionHandler getInstance()
    {
        if (single_instance == null)
            single_instance = new WritePost();

        return single_instance;
    }

    static String root = "./src/main/resources/static/post/images/";

    /**
     * body에 있는 문자열, 사진을 파싱하여 db에 문자열은 그대로, 사진은 파일경로를 저장한다.
     * <p>
     *     사진파일명이 중복된 경우, 뒤에 (1) 등을 붙여서 저장한다.
     * </p>
     * @param request 해당 요청에 대한 Httprequest class
     * @return 반환할 HttpResponse class
     */
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
                if (partName.equals("image") && fileData.getData().length > 0) {
                    imgpath = root + fileData.getFileName();
                    int instantnum = 1;
                    int lastDotIndex = imgpath.lastIndexOf('.');
                    String namePart = fileData.getFileName().substring(0, lastDotIndex);
                    String extensionPart = fileData.getFileName().substring(lastDotIndex);
                    int namelengthlimit = 270 - root.length() - extensionPart.length();
                    if (namePart.length() > namelengthlimit){
                        namePart = namePart.substring(0, namelengthlimit);
                    }
                    while(new File(namePart +"("+ instantnum +")" + extensionPart).exists()){
                        instantnum++;
                    }
                    imgpath = root + namePart +"("+ instantnum +")" + extensionPart;
                    Files.write(Paths.get(imgpath), fileData.getData());
                }else if(partName.equals("text")){
                    text = new String(fileData.getData());
                    if(text.length() > 1000){
                        return new HttpResponse.ResponseBuilder(401)
                                .addheader("Content-Type", Extension.HTML.getContentType())
                                .setBody(PageBuilder.buildeWritePage("글자수는 1000자를 넘을 수 없습니다."))
                                .build();
                    }
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
