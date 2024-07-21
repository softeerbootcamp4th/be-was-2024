package webserver.mapping.mapper.get;

import db.ConnectionPool;
import model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.annotation.LoginCheck;
import webserver.enums.HttpStatus;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;
import webserver.mapping.mapper.HttpMapper;
import webserver.util.FileContentReader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@LoginCheck
public class ArticleDetailMapper implements HttpMapper {
    private static final Logger logger = LoggerFactory.getLogger(ArticleDetailMapper.class);
    private final FileContentReader fileContentReader = FileContentReader.getInstance();
    ConnectionPool databaseConnections = ConnectionPool.getInstance();

    @Override
    public MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException, SQLException {
        String path = httpRequest.getPath();
        String[] pathSplit = path.split("/");

        String articleId = pathSplit[pathSplit.length - 1];
        Article article = null;

        Connection connection = databaseConnections.getConnection();
        String sql = "SELECT a.*, u.userId as userIdString FROM articles a " +
                "JOIN users u ON a.userId = u.id " +
                "WHERE a.id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, Integer.parseInt(articleId));

        try (ResultSet rs = preparedStatement.executeQuery()) {
            if (rs.next()) {
                article = new Article(
                        rs.getInt("id"),
                        rs.getString("userIdString"),
                        rs.getString("content"),
                        rs.getString("imgPath")
                );
            }
        }

        preparedStatement.close();
        databaseConnections.releaseConnection(connection);

        logger.debug("Article detail: {}", article);
        MyHttpResponse response = new MyHttpResponse(HttpStatus.OK);

        byte[] image = fileContentReader.readStaticResourceByAbsolute(article.getImgPath());
        String html = generateHtmlContent(article, image);
        response.setBody(html.getBytes());

        return response;
    }


    public String generateHtmlContent(Article article, byte[] image) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\"/>\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n");
        html.append("    <link href=\"/reset.css\" rel=\"stylesheet\"/>\n");
        html.append("    <link href=\"/global.css\" rel=\"stylesheet\"/>\n");
        html.append("    <link href=\"/main.css\" rel=\"stylesheet\"/>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("<div class=\"container\">\n");
        html.append("    <header class=\"header\">\n");
        html.append("        <a href=\"/main\"><img src=\"/img/signiture.svg\"/></a>\n");
        html.append("        <ul class=\"header__menu\">\n");
        html.append("            <li class=\"header__menu__item\">\n");
        html.append("                <a class=\"btn btn_contained btn_size_s\" href=\"/article\">글쓰기</a>\n");
        html.append("            </li>\n");
        html.append("            <li class=\"header__menu__item\">\n");
        html.append("                <form id=\"logout-form\" action=\"/user/logout\" method=\"POST\" style=\"display: inline;\">\n");
        html.append("                    <button type=\"submit\" id=\"logout-btn\" class=\"btn btn_ghost btn_size_s\">\n");
        html.append("                        로그아웃\n");
        html.append("                    </button>\n");
        html.append("                </form>\n");
        html.append("            </li>\n");
        html.append("        </ul>\n");
        html.append("    </header>\n");
        html.append("    <div class=\"wrapper\">\n");
        html.append("        <div class=\"post\">\n");
        html.append("            <div class=\"post__account\">\n");
//        html.append("                <img class=\"post__account__img\"/>\n");
//        html.append("                <p class=\"post__account__nickname\">").append(article.getUserId()).append("</p>\n");
        html.append("                <img id=\"image\" alt=\"Image\">\n");
        html.append("                <script>\n");
        html.append("                    let byteArray = new Uint8Array(" + image.toString() + ");\n");
        html.append("                    let base64String = byteArrayToBase64(byteArray);\n");
        html.append("                    let dataURL = getDataURL(base64String, 'image/jpeg'); // MIME 타입에 맞게 변경\n");
        html.append("\n");
        html.append("                    // 이미지 표시\n");
        html.append("                    document.getElementById('image').src = dataURL;\n");
        html.append("                </script>\n");
        html.append("            </div>\n");
        html.append("            <img class=\"post__img\" src=\"/").append(article.getImgPath()).append("\"/>\n");
        html.append("            <div class=\"post__menu\">\n");
        html.append("                <ul class=\"post__menu__personal\">\n");
        html.append("                    <li>\n");
        html.append("                        <button class=\"post__menu__btn\">\n");
        html.append("                            <img src=\"/img/like.svg\"/>\n");
        html.append("                        </button>\n");
        html.append("                    </li>\n");
        html.append("                    <li>\n");
        html.append("                        <button class=\"post__menu__btn\">\n");
        html.append("                            <img src=\"/img/sendLink.svg\"/>\n");
        html.append("                        </button>\n");
        html.append("                    </li>\n");
        html.append("                </ul>\n");
        html.append("                <button class=\"post__menu__btn\">\n");
        html.append("                    <img src=\"/img/bookMark.svg\"/>\n");
        html.append("                </button>\n");
        html.append("            </div>\n");
        html.append("            <p class=\"post__article\">\n");
        html.append("                ").append(article.getContent()).append("\n");
        html.append("            </p>\n");
        html.append("        </div>\n");
        html.append("        <nav class=\"nav\">\n");
        html.append("            <ul class=\"nav__menu\">\n");
        html.append("                <li class=\"nav__menu__item\">\n");
        html.append("                    <a class=\"nav__menu__item__btn\" href=\"\">\n");
        html.append("                        <img class=\"nav__menu__item__img\" src=\"../img/ci_chevron-left.svg\"/>\n");
        html.append("                        이전 글\n");
        html.append("                    </a>\n");
        html.append("                <li class=\"nav__menu__item\">\n");
        html.append("                    <a class=\"nav__menu__item__btn\" href=\"\">\n");
        html.append("                        다음 글\n");
        html.append("                        <img class=\"nav__menu__item__img\" src=\"../img/ci_chevron-right.svg\"/>\n");
        html.append("                    </a>\n");
        html.append("                </li>\n");
        html.append("            </ul>\n");
        html.append("        </nav>\n");
        html.append("    </div>\n");
        html.append("</div>\n");
        html.append("</body>\n");
        html.append("</html>");

        return html.toString();
    }
}


