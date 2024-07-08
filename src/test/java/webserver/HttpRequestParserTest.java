package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpRequestParserTest {

    private final HttpRequestParser httpRequestParser = HttpRequestParser.getInstance();

    @ParameterizedTest
    @CsvSource({"/favicon.ico", "/index.html", "/helloworld"})
    @DisplayName("Uri Parsing")
    void HttpRequestURIParserTest(String uri) {
        // Given
        String requestFirstLine = "GET " + uri + " HTTP/1.1";

        // When
        String[] path = httpRequestParser.parseRequestFirstLine(requestFirstLine);

        // Then
        assertEquals(uri, path[1]);
    }


}