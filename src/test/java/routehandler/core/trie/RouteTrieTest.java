package routehandler.core.trie;

import http.MyHttpRequest;
import http.enums.HttpMethodType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import routehandler.core.IRouteHandler;
import route.routes.IndexPageHandler;
import static org.mockito.Mockito.*;

class RouteTrieTest {
    @Test
    @DisplayName("존재하는 경로 요청 시 정상 동작")
    void itWorksIfRouteExist() {
        IndexPageHandler handler = new IndexPageHandler();
        RouteTrie trie = new RouteTrie();
        MyHttpRequest request = mock(MyHttpRequest.class);
        trie.insert("/hello/world", HttpMethodType.GET , handler);
        trie.insert("/hello/{id}/world", HttpMethodType.GET , handler);
        trie.insert("/user/{id}/posts/{postId}", HttpMethodType.POST, handler);


        IRouteHandler handler1 = trie.search("/hello/world", HttpMethodType.GET, request);
        // path variable 테스트
        IRouteHandler handler2 = trie.search("/hello/pico/world", HttpMethodType.GET, request);
        IRouteHandler handler3 = trie.search("/user/test/posts/13", HttpMethodType.POST, request);

        Assertions.assertThat(handler1).isNotNull();
        Assertions.assertThat(handler2).isNotNull();
        Assertions.assertThat(handler3).isNotNull();

        // 경로는 매칭되는데 대응되는 http 메서드가 등록되지 않은 경우
        Assertions.assertThatThrownBy(() -> {
            IRouteHandler handler4 = trie.search("/user/test/posts/15", HttpMethodType.GET, request);
        });
        // 경로 매칭되지 않는 경우
        Assertions.assertThatThrownBy(() -> {
            IRouteHandler handler5 = trie.search("/no-match", HttpMethodType.GET, request);
        });

    }
}