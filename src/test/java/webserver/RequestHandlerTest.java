package webserver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class RequestHandlerTest {

    private final String extensions = "css";

    private Socket connection;

    public RequestHandler requestHandler = new RequestHandler(connection) ;

    @DisplayName("match테스트")
    @Test
    public void matchTest()
    {
        Assertions.assertEquals("text/css",requestHandler.match(extensions));
    }

}