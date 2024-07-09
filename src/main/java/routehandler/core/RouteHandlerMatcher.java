package routehandler.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 * 매칭되는 경로를 반환하는 클래스
 *
 * 경로를 어떻게 설정하느냐에 따라 경로를 찾지 못할 수 있어 사용하지 않는다. RouteHandleChain을 대신 사용하자
 *
 * UserRouteHandler(/user)
 * AuthHandler(/)
 *   - /user/auth
 *
 * 정상적인 라우팅 방식에서는 /user/auth 경로를 탐색할 때,
 * UserRouteHandler에서 매칭되지 않으면 다음 핸들러인 AuthHandler로 넘어가야 하지만,
 * 현재 Matcher 클래스는 매칭되는 Route를 반환하는 방식으로 설계되어 있어 다음 경로를 반환하지 못한다.
 * 대응되는 메서드를 구현해도 되겠지만, 프로젝트 전반의 호환성을 위해 RouteHandleChain을 새로 만드는 방향을 채택한다.
 * </pre>
 */
@Deprecated(forRemoval = true)
public class RouteHandlerMatcher {
    List<IRouteHandler> routeHandlers;

    public RouteHandlerMatcher() {
        this.routeHandlers = new ArrayList<>();
    }

    public RouteHandlerMatcher(IRouteHandler... routeHandlers) {
        this();
        this.routeHandlers.addAll(Arrays.asList(routeHandlers));
    }

    public IRouteHandler getMatchedRouteHandler(String url) {
        IRouteHandler routeHandler = null;
        for (IRouteHandler handler : routeHandlers) {
            if(!handler.canMatch(url)) continue;

            routeHandler = handler;
            break;
        }

        return routeHandler;
    }
}
