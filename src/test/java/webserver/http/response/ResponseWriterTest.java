package webserver.http.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.*;

class ResponseWriterTest {

    @Test
    @DisplayName("404 응답에 대해 404 페이지로 이동하도록 구현한다.")
    void test404() throws IOException {

        //given
        Response response = new Response.Builder(Status.NOT_FOUND).build();
        OutputStream outputStream = new ByteArrayOutputStream();
        ResponseWriter responseWriter = new ResponseWriter(outputStream);

        //when
        responseWriter.write(response);

        //then
        assertTrue(outputStream.toString().contains("/error/404.html"));

    }

}