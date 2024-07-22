package web;

import common.FileUtils;
import common.ResponseUtils;
import db.ArticleH2Database;
import facade.SessionFacade;
import model.Article;

import java.io.*;
import java.util.Collection;

/**
 * 순수 자바 코드로 직접 동적 html을 생성하기 위한 Generator
 * File을 읽어오고, String으로 변환하여 수정한 후 다시 File로 쓰는 방식으로 동작
 */
public class DynamicHtmlGenerator {

    private static String CUSTOM_PATH = "./eckrin/";

    private static String USERNAME_PLACEHOLDER = "<!-- USERNAME_PLACEHOLDER -->";
    private static String IMG_PLACEHOLDER = "<!-- IMG_PLACEHOLDER -->";
    private static String CONTENT_PLACEHOLDER = "<!-- CONTENT_PLACEHOLDER -->";
    private static String POST_PLACEHOLDER = "<!-- POST_PLACEHOLDER -->";
    private static String LOGIN_PLACEHOLDER = "<!-- LOGIN_PLACEHOLDER -->";

    public static void responseDynamicStringHtml(HttpRequest request, OutputStream dos, String contentType) throws IOException {

        String htmlTemplate = getStringFromFilepath(FileUtils.STATIC_DIR_PATH+ViewPath.DEFAULT.getFilePath());

        // 로그인 정보 동적 세팅
        String loginHtml;
        if (SessionFacade.isAuthenticatedRequest(request)) {
            String userId = SessionFacade.getUserIdFromSession(request);
            loginHtml = "<li class=\"header__menu__item\"><form id=\"logout\" action=\"/logout\" method=\"POST\"><button type=\"submit\">로그아웃</button></form></li>" +
                    "<li class=\"header__menu__item\"><p class=\"comment__item__article\"> 안녕하세요, "+userId+"님.</p></li>";
        } else {
            loginHtml = "<li class=\"header__menu__item\"><a class=\"btn btn_contained btn_size_s\" href=\"/login\">로그인</a></li>" +
                    "<li class=\"header__menu__item\"><a class=\"btn btn_ghost btn_size_s\" href=\"/registration\">회원 가입</a></li>";
        }

        // Article 정보 동적으로 세팅
        Collection<Article> articleList = ArticleH2Database.getArticleList();

        for (Article article : articleList) {
            String postTemplate = getStringFromFilepath(FileUtils.STATIC_DIR_PATH+"/partial/post.html");
            String articleUserId = article.getUserId();
            String imagePath = CUSTOM_PATH + article.getImagePath();
            String imgHtml = "<img class=\"post__img\" src=\""+imagePath+"\"/>";
            String contentHtml = article.getContent();
            postTemplate = postTemplate.replace(USERNAME_PLACEHOLDER, articleUserId);
            postTemplate = postTemplate.replace(IMG_PLACEHOLDER, imgHtml);
            postTemplate = postTemplate.replace(CONTENT_PLACEHOLDER, contentHtml);
            htmlTemplate = htmlTemplate.replace(POST_PLACEHOLDER, postTemplate + "\n" + POST_PLACEHOLDER);
        }

        String finalHtml = htmlTemplate.replace(LOGIN_PLACEHOLDER, loginHtml);
        byte[] body = finalHtml.getBytes();
        HttpResponse response = ResponseUtils.responseSuccessWithFile(contentType, body);
        response.writeInBytes(dos);
    }

    /**
     * filePath로부터 파일을 읽고, 파일을 String으로 변환한 뒤 반환한다.
     * @param filePath 읽을 파일의 경로
     * @return String 변환된 파일
     */
    private static String getStringFromFilepath(String filePath) throws IOException {
        FileInputStream fis = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fis));

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (fis != null) {
                fis.close();
            }
        }

        return stringBuilder.toString();
    }
}
