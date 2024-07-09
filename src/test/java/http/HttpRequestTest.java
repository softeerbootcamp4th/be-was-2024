package http;

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

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTest {

    @ParameterizedTest
    @CsvSource({
            "GET / HTTP/1.1",
            "GET        /           HTTP/1.1",
    })
    void testValidRequest(String requestString) throws IOException {
        HttpRequest httpRequest =
                HttpRequestParser.parse(new ByteArrayInputStream(requestString.getBytes()));

        assertEquals(httpRequest.getMethod(), "GET");
        assertEquals(httpRequest.getUrl(), "/");
        assertEquals(httpRequest.getPath(), "/");
        assertEquals(httpRequest.getViewPath(), "/index.html");
        assertEquals(httpRequest.getHttpVersion(), "HTTP/1.1");
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
        assertThrows(InvalidHttpRequestException.class,
                () -> HttpRequestParser.parse(new ByteArrayInputStream(requestString.getBytes())));
    }

    @ParameterizedTest
    @CsvSource({
            "GET /showAll HTTP/0.9", //valid but unsupported http version
            "POST /showAll HTTP/1.0",
            "PUT /showAll HTTP/2",
            "PATCH /showAll HTTP/3"
    })
    void testUnsupportedRequest(String requestString) {
        assertThrows(UnsupportedHttpVersionException.class,
                () -> HttpRequestParser.parse(new ByteArrayInputStream(requestString.getBytes())));
    }


    @Test
    void testFindingQueryParams() throws IOException {
        String requestString = "GET /create?name=junha&age=27&sex=male HTTP/1.1";

        HttpRequest httpRequest =
                HttpRequestParser.parse(new ByteArrayInputStream(requestString.getBytes()));

        assertThrows(QueryParameterNotFoundException.class,
                () -> httpRequest.getQueryParameterValue("namee"));
        assertThrows(QueryParameterNotFoundException.class,
                () -> httpRequest.getQueryParameterValue("name "));
        assertThrows(QueryParameterNotFoundException.class,
                () -> httpRequest.getQueryParameterValue(" name"));
        assertThrows(QueryParameterNotFoundException.class,
                () -> httpRequest.getQueryParameterValue("AGE"));
        assertThrows(QueryParameterNotFoundException.class,
                () -> httpRequest.getQueryParameterValue("se x"));

        try{
            String name = httpRequest.getQueryParameterValue("name");
            String age = httpRequest.getQueryParameterValue("age");
            String sex = httpRequest.getQueryParameterValue("sex");

            assertEquals(name, "junha");
            assertEquals(age, "27");
            assertEquals(sex, "male");
        } catch(QueryParameterNotFoundException qe){
            fail();
        }
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
        assertNotEquals(findValue, encodedValue);
        assertEquals(findValue, realValue);
    }

    @Test
    void testParsingParameters() throws IOException {
        //given
        String requestString = "GET /show?map=key=value&proposition=a==b&name=jun+ha HTTP/1.1";

        //when
        HttpRequest httpRequest =
                HttpRequestParser.parse(new ByteArrayInputStream(requestString.getBytes()));

        //then
        assertEquals(httpRequest.getQueryParameterValue("map"), "key=value");
        assertEquals(httpRequest.getQueryParameterValue("proposition"), "a==b");
        assertEquals(httpRequest.getQueryParameterValue("name"), "jun ha");

    }


}