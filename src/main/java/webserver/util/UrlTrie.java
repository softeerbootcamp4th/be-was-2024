package webserver.util;

import webserver.enums.HttpMethod;
import webserver.mapping.mapper.HttpMapper;
import webserver.mapping.mapper.error.MethodNotAllowedMapper;
import webserver.mapping.mapper.error.NotFoundMapper;

import java.util.HashMap;
import java.util.Map;

public class UrlTrie {
    private UrlTrieNode root;

    public UrlTrie() {
        this.root = new UrlTrieNode();
    }

    public void insert(String path, HttpMethod method, HttpMapper mapper) {
        String[] pathParts = path.split("/");
        UrlTrieNode currentNode = root;

        for (String part : pathParts) {
            if (part.isEmpty()) {
                continue; // 빈 문자열은 무시
            }

            if (!currentNode.getChildren().containsKey(part)) {
                currentNode.getChildren().put(part, new UrlTrieNode());
            }

            currentNode = currentNode.getChildren().get(part);
        }

        currentNode.setEndpoint(true);
        currentNode.setMapper(method, mapper);
    }

    public HttpMapper search(String path, HttpMethod method) {
        String[] pathParts = path.split("/");
        UrlTrieNode currentNode = root;

        for (String part : pathParts) {
            if (part.isEmpty()) {
                continue; // 빈 문자열은 무시
            }

            if (!currentNode.getChildren().containsKey(part)) {
                // 404 Error
                return new NotFoundMapper();
            }

            currentNode = currentNode.getChildren().get(part);
        }

        if (currentNode.getMapper(method) == null) {
            // Path Variable 처리

            // 405 Error
            return new MethodNotAllowedMapper();
        }

        return currentNode.getMapper(method);
    }
}

class UrlTrieNode {
    private Map<String, UrlTrieNode> children;
    private Map<HttpMethod, HttpMapper> mappers = new HashMap<>();
    private boolean isEndpoint;

    public UrlTrieNode() {
        this.children = new HashMap<>();
        this.isEndpoint = false;
    }

    public Map<String, UrlTrieNode> getChildren() {
        return children;
    }

    public HttpMapper getMapper(HttpMethod method) {
        return mappers.get(method);
    }

    public boolean isEndpoint() {
        return isEndpoint;
    }

    public void setEndpoint(boolean endpoint) {
        isEndpoint = endpoint;
    }

    public void setMapper(HttpMethod method, HttpMapper mapper) {
        mappers.put(method, mapper);
    }
}
