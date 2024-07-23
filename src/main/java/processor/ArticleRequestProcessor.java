package processor;

import db.Database;
import exception.StatusCodeException;
import model.Article;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import type.HTTPStatusCode;
import utils.FileUtils;
import utils.StringUtils;
import webserver.RequestHandler;
import webserver.RequestInfo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class ArticleRequestProcessor extends RequestProcessor {
    private static final Logger logger = LoggerFactory.getLogger(ArticleRequestProcessor.class);

    public ArticleRequestProcessor(RequestInfo requestInfo) throws IOException, StatusCodeException {
        init(requestInfo);

        switch (methodPath()) {
            case "GET /article/write.html" -> index();

            case "POST /article/post" -> writeArticle();

            default -> throw new StatusCodeException(HTTPStatusCode.NOT_FOUND);
        }
    }

    private void index() throws IOException {
        HashMap<String, String> cookie = getCookie();
        String sid = cookie.get("sid");

        if (sid == null || Database.findUserIdBySessionId(sid) == null) {
            insertToResponseHeader("Location", "/user/login.html");
            setResult(HTTPStatusCode.FOUND, getResponseHeader(), "");
            return;
        }

        String path = STATIC_PATH + "/article/index.html";
        byte[] body = FileUtils.readFileToBytes(path);

        insertHTMLTypeToHeader();
        setResult(HTTPStatusCode.OK, getResponseHeader(), body);
    }

    private void writeArticle() throws IOException {
        // 리팩토링 필요
        HashMap<String, String> cookie = getCookie();
        String sid = cookie.get("sid");

        String contentType = getRequestHeaders().get("content-type");
        String[] list = contentType.split("boundary=");
        String boundary = list.length > 1 ? "--" + list[1] : null;

        String bodyToStr = new String(getBody(), StandardCharsets.ISO_8859_1);
        String[] parts = boundary != null ? bodyToStr.split(boundary) : new String[]{ bodyToStr };

        String userId = "";
        if (sid == null || (userId = Database.findUserIdBySessionId(sid)) == null) {
            insertToResponseHeader("Location", "/user/login.html");
            setResult(HTTPStatusCode.FOUND, getResponseHeader(), "");
            return;
        }

        User user = Database.findUserById(userId);
        String content = "";
        ArrayList<String> images = new ArrayList<>();
        try {
            for (String part : parts) {
                if (part.isEmpty()) continue;
                else if (part.startsWith("--")) break;
                String[] l1 = part.trim().split("\\r?\\n", 3);
                String[] l2 = l1[0].split("\\s?;\\s?", 2);
                if (!l2[0].endsWith("form-data")) continue;
                HashMap<String, String> param = StringUtils.paramToMap(l2[1], "\\s?;\\s?");
                String name = param.get("name");
                if (name.equals("\"content\"")) {
                    content = new String(l1[2].trim().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                } else if (name.equals("\"image\"")) {
                    String getFileName = new String(param.get("filename").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                    String fileName = getFileName.substring(1, getFileName.length() - 1);
                    String uploadedFileName = FileUtils.uploadFile(STATIC_PATH + "/img/post", fileName, l1[2].trim().getBytes(StandardCharsets.ISO_8859_1));
                    images.add(uploadedFileName);
                }
            }
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        if (images.isEmpty()) {
            insertHTMLTypeToHeader();
            setResult(HTTPStatusCode.BAD_REQUEST, getResponseHeader(), "" +
                    "<script>" +
                    "alert('내용 및 이미지를 모두 포함해주세요.');" +
                    "location.href='/article/write.html'" +
                    "</script>");

            return;
        }
        Article article = new Article(userId, content, images.get(0));
        Database.addArticle(article);

        insertToResponseHeader("Location", "/");
        setResult(HTTPStatusCode.FOUND, getResponseHeader(), "");
    }
}
