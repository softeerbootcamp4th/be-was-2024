package handler;


import dto.HttpRequest;
import dto.HttpResponse;
import java.io.IOException;

/**
 * HttpRequest를 처리하는 handler의 함수형 인터페이스
 */
@FunctionalInterface
public interface Handler {
    void handle(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;
}
