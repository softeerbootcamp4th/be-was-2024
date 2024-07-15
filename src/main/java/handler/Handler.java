package handler;


import dto.HttpRequest;
import dto.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@FunctionalInterface
public interface Handler {
    void handle(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;
}
