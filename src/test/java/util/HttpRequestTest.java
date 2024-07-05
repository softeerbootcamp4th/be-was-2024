package util;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

class HttpRequestTest {

    @Test
    void testHttpRequestCreation() {
        String method = "GET";
        String url = "/index.html";
        String path = "/index.html";
        String httpVersion = "HTTP/1.1";
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "localhost:8080");
        Map<String, String> queryParams = new HashMap<>();

        HttpRequest request = new HttpRequest(method, url, path, httpVersion, headers, queryParams);

        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getUrl()).isEqualTo(url);
        assertThat(request.getPath()).isEqualTo(path);
        assertThat(request.getHttpVersion()).isEqualTo(httpVersion);
        assertThat(request.getHeader("Host")).isEqualTo("localhost:8080");
        assertThat(request.getContentType()).isEqualTo("text/html");
    }

    @Test
    void testContentTypeDetermination() {
        HttpRequest request = new HttpRequest("GET", "/style.css", "/style.css", "HTTP/1.1", new HashMap<>(), new HashMap<>());
        assertThat(request.getContentType()).isEqualTo("text/css");

        request.setUrl("/script.js");
        assertThat(request.getContentType()).isEqualTo("application/javascript");
    }
}