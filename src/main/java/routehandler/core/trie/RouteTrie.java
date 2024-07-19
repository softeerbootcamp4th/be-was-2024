package routehandler.core.trie;

import http.enums.HttpMethodType;
import routehandler.core.IRouteHandler;
import routehandler.core.exception.NoMatchedRouteException;
import routehandler.utils.RouteUtil;

/**
 * 경로를 매칭하여 핸들러를 반환하는 trie 자료구조
 */
public class RouteTrie {
    /**
     * TrieNode를 탐색하는 시작점. root는 "/" 경로가 아니다. "/" 경로는 ""에 매칭된다.
     */
    private final RouteTrieNode root;

    public RouteTrie() {
        root = new RouteTrieNode();
    }

    /**
     * 핸들러를 경로 및 http 메서드에 매칭한다.
     * @param pathname 매칭할 경로
     * @param method 메서드
     * @param handler 핸들러
     */
    public void insert(String pathname, HttpMethodType method, IRouteHandler handler) {
        String[] pathSegments = RouteUtil.getPathSegments(pathname);
        RouteTrieNode node = root;

        for(String pathSegment: pathSegments) {
            node = node.nextForInsert(pathSegment);
        }

        node.registerHandler(method, handler);
    }

    /**
     * 경로 및 http 메서드에 매칭되는 핸들러를 찾는다
     * @param pathname 찾는 경로
     * @param method 찾는 메서드
     * @return 매칭된 핸들러 ( 없으면 예외 발생 )
     */
    public IRouteHandler search(String pathname, HttpMethodType method) {
        String[] pathSegments = RouteUtil.getPathSegments(pathname);
        RouteTrieNode node = root;

        for(String pathSegment: pathSegments) {
            var newNode = node.nextForSearch(pathSegment);
            if(newNode == null) throw new NoMatchedRouteException( "no route found matching " + pathSegment);

            node = newNode;
        }

        return node.getHandler(method);
    }
}
