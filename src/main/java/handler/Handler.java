package handler;


import dto.HttpRequest;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@FunctionalInterface
public interface Handler {
    void handle(DataOutputStream dos, HttpRequest httpRequest) throws IOException;
}
