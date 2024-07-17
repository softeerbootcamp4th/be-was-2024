package http;

import exception.HeaderNotFoundException;
import exception.InvalidHttpRequestException;
import exception.QueryParameterNotFoundException;
import exception.UnsupportedHttpVersionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.*;
import static util.StringUtil.*;

class HttpRequestTest {

    @ParameterizedTest
    @CsvSource({
            "GET / HTTP/1.1",
            "GET        /           HTTP/1.1",
            "GET / HTTP/1.1\r\nContent-Length: 17",
    })
    void testValidRequest(String requestString) throws IOException {
        HttpRequest httpRequest =
                HttpRequestParser.parse(new ByteArrayInputStream(requestString.getBytes()));

        assertThat(httpRequest.getMethod()).isEqualTo("GET");
        assertThat(httpRequest.getUrl()).isEqualTo("/");
        assertThat(httpRequest.getPath()).isEqualTo("/");
        assertThat(httpRequest.getViewPath()).isEqualTo("/index.html");
        assertThat(httpRequest.getHttpVersion()).isEqualTo("HTTP/1.1");
    }


    @ParameterizedTest
    @CsvSource({
            "hello from client\r\nGET / HTTP/1.1", //starts with body
            "/index.html HTTP/1.1", //no method
            "GET HTTP/1.1", //no URL
            "GET /create ", //no http version
            "POSTT /create HTTP/1.1", //wrong method
            "get /hello HTTP/1.1", //wrong method
            "POST /create?name=junha&age", //wrong parameter
            "GET /showData MyVersion/2.2" //wrong http version
    })
    void testInvalidRequest(String requestString) {
        assertThatExceptionOfType(InvalidHttpRequestException.class)
                .isThrownBy(() -> HttpRequestParser.parse(new ByteArrayInputStream(requestString.getBytes())));
    }



    @ParameterizedTest
    @CsvSource({
            "GET /showAll HTTP/0.9", //valid but unsupported http version
            "POST /showAll HTTP/1.0",
            "PUT /showAll HTTP/2",
            "PATCH /showAll HTTP/3"
    })
    void testUnsupportedRequest(String requestString) {
        assertThatExceptionOfType(UnsupportedHttpVersionException.class)
                .isThrownBy(() -> HttpRequestParser.parse(new ByteArrayInputStream(requestString.getBytes())));
    }


    @Test
    void testFindingQueryParams() throws IOException {
        String requestString = "GET /create?name=junha&age=27&sex=male HTTP/1.1";

        HttpRequest httpRequest =
                HttpRequestParser.parse(new ByteArrayInputStream(requestString.getBytes()));

        assertThatExceptionOfType(QueryParameterNotFoundException.class)
                .isThrownBy(() -> httpRequest.getQueryParameterValue("namee"));
        assertThatExceptionOfType(QueryParameterNotFoundException.class)
                .isThrownBy(() -> httpRequest.getQueryParameterValue("name "));
        assertThatExceptionOfType(QueryParameterNotFoundException.class)
                .isThrownBy(() -> httpRequest.getQueryParameterValue(" name"));
        assertThatExceptionOfType(QueryParameterNotFoundException.class)
                .isThrownBy(() -> httpRequest.getQueryParameterValue("AGE"));
        assertThatExceptionOfType(QueryParameterNotFoundException.class)
                .isThrownBy(() -> httpRequest.getQueryParameterValue("se x"));

        assertThatCode(() -> {
            String name = httpRequest.getQueryParameterValue("name");
            String age = httpRequest.getQueryParameterValue("age");
            String sex = httpRequest.getQueryParameterValue("sex");

            assertThat(name).isEqualTo("junha");
            assertThat(age).isEqualTo("27");
            assertThat(sex).isEqualTo("male");
        }).doesNotThrowAnyException();
    }


    @ParameterizedTest
    @CsvSource({
            "한국어로써있는단어",
            "日本語で書く単語", //japanese
            "用中文写的字", //chinese
            "value that includes blanks",
            "value&that&includes&ampersand",
            "value=that=includes=equals",
            "================",
            "&&&&&&&&&&&&&&&&"
    })
    void testEncodedQueryParams(String realValue) throws IOException {
        //given
        String encodedValue = URLEncoder.encode(realValue, StandardCharsets.UTF_8);
        String requestString = "GET /create?key=" + encodedValue + " HTTP/1.1";

        //when
        HttpRequest httpRequest =
                HttpRequestParser.parse(new ByteArrayInputStream(requestString.getBytes()));

        //then
        //조회한 값은 realValue
        String findValue = httpRequest.getQueryParameterValue("key");
        assertThat(findValue).isNotEqualTo(encodedValue);
        assertThat(findValue).isEqualTo(realValue);
    }


    @Test
    void testParsingParameters() throws IOException {
        //given
        String requestString = "GET /show?map=key=value&proposition=a==b&name=jun+ha HTTP/1.1";

        //when
        HttpRequest httpRequest =
                HttpRequestParser.parse(new ByteArrayInputStream(requestString.getBytes()));

        //then
        assertThat(httpRequest.getQueryParameterValue("map")).isEqualTo("key=value");
        assertThat(httpRequest.getQueryParameterValue("proposition")).isEqualTo("a==b");
        assertThat(httpRequest.getQueryParameterValue("name")).isEqualTo("jun ha");
    }


    @ParameterizedTest
    @CsvSource({
            "Content-Length:17",
            "Content-Length: 17", //OWS - blank
            "Content-Length:    17" //OWS - tab
    })
    void testHeader(String headerString) throws IOException {
        //given
        String startLine = "POST /create HTTP/1.1\r\n";
        String bodyString = "hello from client";
        String requestString = startLine + headerString + CRLF + CRLF + bodyString;

        //when
        HttpRequest httpRequest =
                HttpRequestParser.parse(new ByteArrayInputStream(requestString.getBytes()));

        //then
        assertThat(httpRequest.getHeaders().size()).isEqualTo(1);
        assertThat(httpRequest.getHeaderValue("Content-Length")).isEqualTo("17");

        assertThatExceptionOfType(HeaderNotFoundException.class)
                .isThrownBy(() -> httpRequest.getHeaderValue("Content-Length "));
        assertThatExceptionOfType(HeaderNotFoundException.class)
                .isThrownBy(() -> httpRequest.getHeaderValue(" Content-Length"));
        assertThatExceptionOfType(HeaderNotFoundException.class)
                .isThrownBy(() -> httpRequest.getHeaderValue("CONTENT-LENGTH"));
        assertThatExceptionOfType(HeaderNotFoundException.class)
                .isThrownBy(() -> httpRequest.getHeaderValue("Content-Type"));
   }

    @Test
    void testRequestWithBody() throws IOException {
        //given
        String requestString = "POST /create HTTP/1.1\r\n" +
                                "Content-Length: 17\r\n\r\n" +
                                "hello from client";

        //when
        HttpRequest httpRequest =
                HttpRequestParser.parse(new ByteArrayInputStream(requestString.getBytes()));

        //then
        assertThat(httpRequest.getHeaderValue("Content-Length")).isEqualTo("17");
        assertThatExceptionOfType(HeaderNotFoundException.class)
                .isThrownBy(() -> httpRequest.getHeaderValue("hello"));

        byte[] body = httpRequest.getBody();

        assertThat(body.length).isEqualTo(17);
        assertThat(body).isEqualTo("hello from client".getBytes());
        assertThat(new String(body, StandardCharsets.UTF_8)).isEqualTo("hello from client");
    }



}