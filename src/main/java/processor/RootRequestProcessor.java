package processor;

import db.Database;
import exception.StatusCodeException;
import model.Article;
import model.User;
import type.HTTPStatusCode;
import utils.FileUtils;
import webserver.RequestInfo;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;

public class RootRequestProcessor extends RequestProcessor {

    public RootRequestProcessor(RequestInfo requestInfo) throws IOException, StatusCodeException {
        init(requestInfo);

        switch (methodPath()) {
            case "GET /" -> redirectToIndex();
            case "GET /index.html" -> index();
            default -> staticResource();
        }
    }

    private void redirectToIndex() throws IOException {
        insertToResponseHeader("Location", "/index.html");
        setResult(HTTPStatusCode.FOUND, getResponseHeader(), "");
    }

    private void index() throws IOException {
        String path = STATIC_PATH + getPath();
        byte[] body = FileUtils.readFileToBytes(path);

        HashMap<String, String> cookie = getCookie();

        HashMap<String, String> bodyParams = new HashMap<>();

        String sid = cookie.get("sid");
        if (sid != null) {
            if (Database.findUserIdBySessionId(sid) == null) {
                insertToResponseHeader("Set-Cookie", "sid=; Path=/; Max-Age=0");
            } else {
                bodyParams.put("hasSID", "true");
                String userId = Database.findUserIdBySessionId(sid);
                bodyParams.put("userId", userId);
                User user = Database.findUserById(userId);
                bodyParams.put("userName", user.getName());
            }
        }

        try {
            HashMap<String, String> queryParams = getQuery();
            String id = queryParams.get("id");
            int articleId = id != null && id.matches("\\d+") ? Integer.parseInt(id) : 1;
            Article article = Database.getArticleById(articleId);
            if (article == null) throw new Exception("has no article");
            bodyParams.put("hasArticle", "true");
            bodyParams.put("writer", article.getUserId());
            bodyParams.put("content", article.getContent().trim());
            bodyParams.put("image", "/img/post/" + article.getImage());
            if (Database.getArticleById(articleId + 1) != null) {
                bodyParams.put("nextHref", "/index.html?id=" + (articleId + 1));
            } else {
                bodyParams.put("nextHidden", "visibility: hidden;");
            }

            if (Database.getArticleById(articleId - 1) != null) {
                bodyParams.put("prevHref", "/index.html?id=" + (articleId - 1));
            } else {
                bodyParams.put("prevHidden", "visibility: hidden;");
            }
        } catch (Exception e) {
            // 의도적인 catch - 해당 articleId가 없으면
        }

        insertHTMLTypeToHeader();
        setResult(HTTPStatusCode.OK, getResponseHeader(), body, bodyParams);
    }

    private void staticResource() throws StatusCodeException, IOException {
        String path = URLDecoder.decode(getPath(), "UTF-8");
        String staticPath = STATIC_PATH + path;
        if (FileUtils.isFile(staticPath)) {
            byte[] body = FileUtils.readFileToBytes(staticPath);
            insertContentTypeToHeader(FileUtils.findMIME(path));
            insertToResponseHeader("Content-Length", String.valueOf(body.length));

            setResult(HTTPStatusCode.OK, getResponseHeader(), body);
        } else throw new StatusCodeException(HTTPStatusCode.NOT_FOUND);
    }
}
