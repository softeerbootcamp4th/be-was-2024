package utils;

import java.io.IOException;

@FunctionalInterface
public interface Handler {
    String handle(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler, Model model) throws IOException;
}
