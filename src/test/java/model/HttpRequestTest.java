package model;

import model.enums.HttpStatus;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class HttpRequestTest {

    @Test
    void createHttpResponse() throws IOException {
        //given
        List<String> headers = new ArrayList<>();
        headers.add("Host: localhost:8080");
        headers.add("Connection: keep-alive");
        HttpRequest httpRequest = new HttpRequest("GET", "/index.html", "HTTP/1.1", headers);

        //when
        HttpResponse httpResponse = httpRequest.createHttpResponse();

        //then
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.OK);



    }
}