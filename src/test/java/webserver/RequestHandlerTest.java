package webserver;

import db.Database;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayName("RequestHandler 테스트")
public class RequestHandlerTest {
    private RequestHandler requestHandler;
    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerTest.class);


    @BeforeEach
    public void setUp() throws IOException {
        Socket mockSocket = Mockito.mock(Socket.class);
        InputStream inputStream = new ByteArrayInputStream(("GET /user/create?userId=javajigi" +
                "&password=password" +
                "&name=%EB%B0%95%EC%9E%AC%EC%84%B1" +
                "&email=javajigi%40slipp.net HTTP/1.1" +
                "\r\n\r\n").getBytes());
        when(mockSocket.getInputStream()).thenReturn(inputStream);
        when(mockSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        requestHandler = new RequestHandler(mockSocket);
    }


    @Test
    @DisplayName("회원가입 성공")
    public void testHandleRequestCreate() {

        // When
        requestHandler.run();

        // Then
        User user = Database.findUserById("javajigi");
        assertEquals("javajigi", user.getUserId());
        assertEquals("password", user.getPassword());
        assertEquals("박재성", user.getName());
        assertEquals("javajigi@slipp.net", user.getEmail());
    }

}
