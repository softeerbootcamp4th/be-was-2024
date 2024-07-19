package webserver.mapping.mapper.post;

import db.ConnectionPool;
import db.SessionTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.annotation.LoginCheck;
import webserver.enums.HttpStatus;
import webserver.http.HttpRequestParser;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;
import webserver.http.multipart.Part;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@LoginCheck
public class CreateArticleMapper implements webserver.mapping.mapper.HttpMapper {
    private static final Logger logger = LoggerFactory.getLogger(CreateArticleMapper.class);

    public static final String HEADER_COOKIE = "cookie";
    public static final String COOKIE_SESSION_ID = "sId";
    public static final String BODY_CONTENT = "content";

    HttpRequestParser httpRequestParser = HttpRequestParser.getInstance();
    ConnectionPool databaseConnections = ConnectionPool.getInstance();

    @Override
    public synchronized MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException, SQLException {
        UUID uuid = UUID.fromString(httpRequestParser.parseCookie(httpRequest.getHeaders().get(HEADER_COOKIE)).get(COOKIE_SESSION_ID));
        int userId = SessionTable.findUserIdBySessionId(uuid);
        Connection connection = databaseConnections.getConnection();

        if (httpRequest.getHeaders().get("content-type").startsWith("multipart/form-data")) {
            String boundary = httpRequest.getHeaders().get("content-type").split("boundary=")[1];
            ArrayList<Part> parts = httpRequestParser.parseMultipartFormData(httpRequest.getBody(), boundary);

            // 파일 로컬 저장
            Part body = parts.get(1);
            String filePath = body.write(body.getBody());

            String sql = "INSERT INTO articles (userId, content, imgPath) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, new String(parts.get(0).getBody()));
            preparedStatement.setString(3, filePath);
            preparedStatement.executeUpdate();

            logger.debug("Article created: {}", parts.get(0));

        }
//        else {
//            Map<String, String> body = httpRequestParser.parseQuery(new String(httpRequest.getBody()));
//            String content = body.get(BODY_CONTENT);
//
//            Article article = new Article(userId, content);
//            Database.addArticle(article);
//            logger.debug("Article created: {}", article);
//        }

        MyHttpResponse response = new MyHttpResponse(HttpStatus.FOUND, Map.of("Location", "/"), null);

        return response;
    }
}
