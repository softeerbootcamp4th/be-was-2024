package webserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class HttpRequestParserTest {

    private HttpRequestParser httpRequestParser;

    @BeforeEach
    void setUp() {
        httpRequestParser = new HttpRequestParser();
    }

    @ParameterizedTest
    @CsvSource({"/favicon.ico", "/index.html", "/helloworld"})
    @DisplayName("Uri Parsing")
    void HttpRequestURIParserTest(String uri) {
        // Given
        String requestFirstLine = "GET " + uri + " HTTP/1.1";

        // When
        String path = HttpRequestParser.parseRequestURI(requestFirstLine);

        // Then
        assertEquals(uri, path);
    }


}