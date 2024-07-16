package chain;

import http.MyHttpRequest;
import http.MyHttpResponse;

import http.enums.HttpStatusType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class NotFoundHandleChainTest {
    @Test
    @DisplayName("응답 상태가 설정되어 있지 않다면 404 예외 처리")
    void handleIfNoStatusSet() {
        NotFoundHandleChain chain = new NotFoundHandleChain();
        MyHttpRequest request = new MyHttpRequest("GET / HTTP/1.1", new ArrayList<>(), new byte[0]);
        MyHttpResponse response = new MyHttpResponse(request.getVersion());

        chain.act(request, response);

        HttpStatusType status = response.getStatusType();

        Assertions.assertThat(status).isEqualTo(HttpStatusType.NOT_FOUND);
    }

    @Test
    @DisplayName("응답 상태가 설정되어 있다면 그대로 유지")
    void doNothingIfStatusSet() {
        NotFoundHandleChain chain = new NotFoundHandleChain();
        MyHttpRequest request = new MyHttpRequest("GET / HTTP/1.1", new ArrayList<>(), new byte[0]);
        MyHttpResponse response = new MyHttpResponse(request.getVersion());
        response.setStatusInfo(HttpStatusType.OK);
        chain.act(request, response);

        HttpStatusType status = response.getStatusType();

        Assertions.assertThat(status).isEqualTo(HttpStatusType.OK);
    }
}