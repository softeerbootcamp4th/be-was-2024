package chain.core;

import http.MyHttpRequest;
import http.MyHttpResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ChainManagerTest {
    @Test
    @DisplayName("chain manager은 다양한 체인을 체이닝 가능")
    void canChainMultipleMiddleware() {
        TestChain chain1 = spy(TestChain.class);
        TestChain chain2 = spy(TestChain.class);

        ChainManager manager = new ChainManager(
                chain1,
                chain2
        );

        manager.execute(any(), any());

        // 체인들이 정상적으로 체이닝되는지 검사
        verify(chain1).act(any(), any());
        verify(chain2).act(any(), any());

        // private / final / equals / hashcode는 테스트 불가. 내부적으로 별도로 처리
        // 프록시 기반으로 동작하므로 final 메서드는 모킹할 수 없다.
    }

    public static class TestChain extends MiddlewareChain {

        @Override
        public void act(MyHttpRequest req, MyHttpResponse res) {
            next(req, res);
        }
    }
}