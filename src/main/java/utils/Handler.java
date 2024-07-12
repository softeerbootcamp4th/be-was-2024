package utils;

import java.io.IOException;

@FunctionalInterface
public interface Handler {
    void handle(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler) throws IOException;
}
