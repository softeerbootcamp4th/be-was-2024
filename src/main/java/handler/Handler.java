package handler;


import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

@FunctionalInterface
public interface Handler {
    void handle(DataOutputStream dos, Map<String, String> queryParams) throws IOException;
}
