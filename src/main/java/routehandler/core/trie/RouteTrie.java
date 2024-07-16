package routehandler.core.trie;

import http.enums.HttpMethodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routehandler.core.IRouteHandler;
import routehandler.core.exception.NoMatchedMethodException;
import routehandler.core.exception.NoMatchedRouteException;
import routehandler.utils.RouteRecord;
import routehandler.utils.RouteUtil;

public class RouteTrie {
    /**
     * TrieNode를 탐색하는 시작점. root는 "/" 경로가 아니다. "/" 경로는 ""에 매칭된다.
     */
    private final RouteTrieNode root;

    public RouteTrie() {
        root = new RouteTrieNode();
    }

    public void insert(String pathname, HttpMethodType method, IRouteHandler handler) {
        String[] pathSegments = RouteUtil.getPathSegments(pathname);
        RouteTrieNode node = root;

        for(String pathSegment: pathSegments) {
            node = node.nextForInsert(pathSegment);
        }

        node.registerHandler(method, handler);
    }

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
