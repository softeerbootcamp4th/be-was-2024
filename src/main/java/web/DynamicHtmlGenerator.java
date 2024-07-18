package web;

import common.FileUtils;
import common.ResponseUtils;
import db.ArticleH2Database;
import facade.SessionFacade;
import model.Article;

import java.io.*;
import java.util.Collection;

public class DynamicHtmlGenerator {

    public static void responseDynamicStringHtml(HttpRequest request, OutputStream dos, String contentType) throws IOException {

        String htmlTemplate = getStringFromFilepath(FileUtils.STATIC_DIR_PATH+ViewPath.DEFAULT.getFilePath());

        String loginHtml, imgHtml;
        String userId = SessionFacade.getUserIdFromSession(request);
        if (SessionFacade.isAuthenticatedRequest(request)) {
            loginHtml = "<li class=\"header__menu__item\"><form id=\"logout\" action=\"/logout\" method=\"POST\"><button type=\"submit\">로그아웃</button></form></li>" +
                    "<li class=\"header__menu__item\"><p class=\"comment__item__article\"> 안녕하세요, "+userId+"님.</p></li>";
        } else {
            loginHtml = "<li class=\"header__menu__item\"><a class=\"btn btn_contained btn_size_s\" href=\"/login\">로그인</a></li>" +
                    "<li class=\"header__menu__item\"><a class=\"btn btn_ghost btn_size_s\" href=\"/registration\">회원 가입</a></li>";
        }

        String imagePath = "";
        Collection<Article> articleList = ArticleH2Database.getArticleList();
        for (Article article : articleList) {
            imagePath = "./eckrin/"+article.getImagePath();
        }
        imgHtml = "<img class=\"post__img\" src=\""+imagePath+"\"/>";

        String finalHtml = htmlTemplate.replace("<!-- LOGIN_PLACEHOLDER -->", loginHtml);
        finalHtml = finalHtml.replace("<!-- IMG_PLACEHOLDER -->", imgHtml);
        byte[] body = finalHtml.getBytes();
        HttpResponse response = ResponseUtils.responseSuccessWithFile(contentType, body);
        response.writeInBytes(dos);
    }

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
