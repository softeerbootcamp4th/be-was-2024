package model;

import dto.HttpRequest;
import dto.HttpResponse;
import dto.enums.HttpMethod;
import dto.enums.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.HttpResponseConverter;
import util.constant.StringConstants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HttpRequestTest {

    @Test
    @DisplayName("/index.html로 이동하는 요청에 대해 200 응답코드가 오는지 테스트")
    void createHttpResponse() throws IOException {
        //given
        Map<String, String> headers = new HashMap<>();
        Map<String, String> quetyParams = new HashMap<>();
        headers.put("Host", "localhost:8080");
        headers.put("Connection", "keep-alive");
        HttpRequest httpRequest = HttpRequest.of(HttpMethod.GET, "/index.html", quetyParams,
                StringConstants.PROTOCOL_VERSION, headers, new byte[512]);

        HttpResponseConverter httpResponseConverter = new HttpResponseConverter();

        //when
        HttpResponse httpResponse = httpResponseConverter.with(httpRequest);

        //then
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.OK);

    }
}