package routehandler.core.trie;

import http.enums.HttpMethodType;
import routehandler.core.IRouteHandler;
import routehandler.utils.RouteUtil;

import java.util.HashMap;
import java.util.Map;

public class RouteTrieNode {
    private Map<String, RouteTrieNode> children;
    private RouteTrieNode pathVariableNode;
    private String pathVariableName;


    public Map<HttpMethodType, IRouteHandler> handlers;

    public RouteTrieNode() {
        children = new HashMap<>();
        children.computeIfAbsent("key", k -> new RouteTrieNode());
    }

    /**
     * 다음 노드를 탐색할 때 ( url 존재하는지? ) 사용하는 메서드. 없으면 null
     * @param pathSegment / 을 기준으로 나눈 각 pathname의 부분
     * @return 다음 노드 (not null)
     */
    public RouteTrieNode nextForInsert(String pathSegment) {
        // path variable 아닌 경우. 일반 노드 반환
        if(!RouteUtil.isPathVariable(pathSegment))
            return children.computeIfAbsent(pathSegment, part -> new RouteTrieNode());

        // path variable 인 경우. pathvariable node 반환.
        if (pathVariableNode == null) {
            pathVariableNode = new RouteTrieNode();
            pathVariableName = RouteUtil.getPathVariable(pathSegment);
        }

        return pathVariableNode;
    }

    /**
     * url 경로를 삽입하기 위해 노드를 탐색할 때 사용하는 메서드. 없으면 새로운 노드 삽입
     * @param pathSegment / 을 기준으로 나눈 각 pathname의 부분
     * @return 다음 노드 (nullable)
     */
    public RouteTrieNode nextForSearch(String pathSegment) {
        // 구체적으로 매칭되는 노드가 있다면 반환하고, 없으면 pathVariableNode 반환
        return children.getOrDefault(pathSegment, pathVariableNode);
    }

    public void registerHandler(HttpMethodType method, IRouteHandler handler) {
        handlers.put(method, handler);
    }

    public IRouteHandler getHandler(HttpMethodType method) {
        IRouteHandler handler = handlers.get(method);
        // 핸들러가 없다면 예외 상황
        if(handler == null) throw new RuntimeException("No handler for method " + method);

        return handler;
    }
}
