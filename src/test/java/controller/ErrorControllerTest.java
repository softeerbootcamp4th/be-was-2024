package controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.HttpCode;
import util.HttpRequest;
import util.HttpRequestMapper;
import util.HttpResponse;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorControllerTest {

    ErrorController controller;

    @DisplayName("service: 404 Not Found 페이지 접근")
    @Test
    void notFound(){
        // given
        HttpRequest request = HttpRequest.from("GET " + HttpRequestMapper.NOT_FOUND + " HTTP/1.1");
        controller = new ErrorController(HttpRequestMapper.NOT_FOUND);

        // when
        HttpResponse response = controller.service(request);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpCode.NOT_FOUND.getStatus());
        assertThat(response.getHttpVersion()).isEqualTo(request.getHttpVersion());
        assertThat(new String(response.getBody(), StandardCharsets.UTF_8)).isEqualTo(HttpCode.NOT_FOUND.toString());
    }

    @DisplayName("service: 405 Method Not Allowed 페이지 접근")
    @Test
    void methodNotAllowed(){
        // given
        HttpRequest request = HttpRequest.from("GET " + HttpRequestMapper.METHOD_NOT_ALLOWED.getPath() + " HTTP/1.1");
        controller = new ErrorController(HttpRequestMapper.METHOD_NOT_ALLOWED);

        // when
        HttpResponse response = controller.service(request);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpCode.METHOD_NOT_ALLOWED.getStatus());
        assertThat(response.getHttpVersion()).isEqualTo(request.getHttpVersion());
        assertThat(new String(response.getBody(), StandardCharsets.UTF_8)).isEqualTo(HttpCode.METHOD_NOT_ALLOWED.toString());
    }

    @DisplayName("service: 기타 오류 페이지 접근 시 404 Not Found 페이지로 리다이렉트")
    @Test
    void otherError(){
        // given
        HttpRequest request = HttpRequest.from("GET /error HTTP/1.1");
        controller = new ErrorController(HttpRequestMapper.SERVER_ERROR);

        // when
        HttpResponse response = controller.service(request);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpCode.FOUND.getStatus());
        assertThat(response.getHttpVersion()).isEqualTo(request.getHttpVersion());
        assertThat(response.getTotalHeaders())
                .contains("Location: " + HttpRequestMapper.NOT_FOUND.getPath());
    }
}
