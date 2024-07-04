package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import java.net.Socket;


class RequestHandlerTest {

    private Socket connection;


    @DisplayName("파싱되어 나온 확장자가 MIME 타입으로 잘 변환 되는지 테스트")
    @Test
    public void matchTest()
    {
        //given
        String extensions = "css";

        //when
        RequestHandler requestHandler = new RequestHandler(connection);

        //then
        assertThat(requestHandler.match(extensions)).isEqualTo("text/css");
    }

}