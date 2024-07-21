package webserver.mapping.mapper.get;

import db.ConnectionPool;
import model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.annotation.LoginCheck;
import webserver.enums.HtmlFlag;
import webserver.enums.HttpStatus;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;
import webserver.mapping.mapper.HttpMapper;
import webserver.util.FileContentReader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

@LoginCheck
public class ArticleDetailMapper implements HttpMapper {
    private static final Logger logger = LoggerFactory.getLogger(ArticleDetailMapper.class);
    private final FileContentReader fileContentReader = FileContentReader.getInstance();
    ConnectionPool databaseConnections = ConnectionPool.getInstance();

    @Override
    public MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException {
        try {
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

            if (article == null) {
                MyHttpResponse response = new MyHttpResponse(HttpStatus.NOT_FOUND);
                fileContentReader.readStaticResource("/404.html", response);
                return response;
            }

            logger.debug("Article detail: {}", article);

            String articleDetail = getArticleDetail(article);

            return new MyHttpResponse(HttpStatus.OK, Map.of(), articleDetail.getBytes());
        } catch (Exception e) {
            FileContentReader fileContentReader = FileContentReader.getInstance();
            String errorBody = fileContentReader.readStaticResourceToString("/error.html").replaceAll(HtmlFlag.CONTENT.getFlag(), e.getMessage());

            return new MyHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, Map.of(), errorBody.getBytes());
        }
    }

    private String getArticleDetail(Article article) throws IOException {
        String imgPath = "<img src=\"/" + article.getImgPath() + "\" width=\"600\" height=\"400\"/>";

        return fileContentReader.readStaticResourceToString("/articleDetail.html")
                .replaceAll(HtmlFlag.CONTENT.getFlag(), "<div style=\"white-space: pre-wrap; word-wrap: break-word; solid #000; padding: 10px; width: 600px;\">" + article.getContent() + "</div>")
                .replaceAll(HtmlFlag.ARTICLE_IMAGE.getFlag(), imgPath);
    }
}


