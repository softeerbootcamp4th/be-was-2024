package util;

import db.ArticleDatabase;
import db.StringIdDatabase;
import db.UserDB;
import model.Article;
import model.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static util.constant.StringConstants.*;


public class FileMapper {
    private final static StringIdDatabase<User> userDatabase = UserDB.getInstance();

    public static byte[] getByteConvertedFile(String path,String userId) throws IOException {
        User user = userDatabase.findById(userId).orElse(null);
//        User user = UserDatabase.findUserById(userId).orElse(null);
        File file = new File(RESOURCE_PATH + path);
        InputStream fileInputStream = new FileInputStream(file);
        byte[] allBytes = fileInputStream.readAllBytes();
        String htmlContent = new String(allBytes, StandardCharsets.UTF_8);

        return htmlContent.getBytes(StandardCharsets.UTF_8);
    }


    public static byte[] getIndexPageByteConvertedFile(String userId) throws IOException {
        User user = userDatabase.findById(userId).orElse(null);
//        User user = UserDatabase.findUserById(userId).orElse(null);
        File file = new File(RESOURCE_PATH + "/index.html");
        InputStream fileInputStream = new FileInputStream(file);
        byte[] allBytes = fileInputStream.readAllBytes();

        String htmlContent = new String(allBytes, StandardCharsets.UTF_8);
        htmlContent = htmlContent.replace(DYNAMIC_ARTICLE_CONTENT, makeIndexPageArticleList());
        // 유저가 로그인 된 상태인 경우와 안된 상태 분리
        if (user!=null) {
            htmlContent = htmlContent.replace(DYNAMIC_CONTENT_IS_LOGIN, makeDynamicContentIsLoginContentWithName(user.getName()));
        }
        else{
            htmlContent = htmlContent.replace(DYNAMIC_CONTENT_IS_NOT_LOGIN, DYNAMIC_CONTENT_IS_NOT_LOGIN_CONTENT);

        }
        return htmlContent.getBytes(StandardCharsets.UTF_8);
    }
    public static byte[] getArticlePageByteConvertedFile(Map<String, String> queryParams) throws IOException {
        // queryParams로부터 article 정보 확인하기
        Long articleId = Long.parseLong(queryParams.get("id"));
        Article article = ArticleDatabase.findArticleById(articleId).orElseThrow(() -> new RuntimeException("해당 articleId로 글을 찾을 수 없습니다."));


        File file = new File(RESOURCE_PATH + "/article.html");
        InputStream fileInputStream = new FileInputStream(file);
        byte[] allBytes = fileInputStream.readAllBytes();

        String htmlContent = new String(allBytes, StandardCharsets.UTF_8);
        htmlContent = htmlContent.replace(DYNAMIC_ARTICLE_CONTENT, makeArticlePage(article.getTitle(),article.getContent()));
        // 유저가 로그인 된 상태인 경우와 안된 상태 분리
        return htmlContent.getBytes(StandardCharsets.UTF_8);
    }
}
