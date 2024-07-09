package model;

import model.enums.HttpStatus;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class HttpRequestTest {

    @Test
    void createHttpResponse() throws IOException {
        //given
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "localhost:8080");
        headers.put("Connection", "keep-alive");
        HttpRequest httpRequest =  HttpRequest.of("GET", "/index.html", "HTTP/1.1", headers);

        //when
        HttpResponse httpResponse = httpRequest.createHttpResponse();

        //then
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.OK);



    }
}