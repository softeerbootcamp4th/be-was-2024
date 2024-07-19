package webserver.http.path;

import webserver.api.*;
import webserver.http.HttpRequest;
import webserver.http.enums.Methods;
import webserver.session.SessionDAO;

import java.util.HashMap;
import java.util.Map;

/**
 * web의 모든 path에 대한 정보를 가지고 있는 클래스
 * <p>
 *     각 path에 대한 node를 만들어서 trie 형식으로 저장한다.
 * </p>
 */
public class PathMap {
    private static final PathNode root = PathMapInfo.root;


    /**
     * path node 클래스
     * <p>
     *     path에서 / 로 구분된 각 파트를 하나의 노드로 취급한다.
     * <p>
     *     ex) user/list -> 이 경우는 user path node의 자식에 list path node가 있음
     * </p>
     */
    static class PathNode {
        /**
         * 자식 노드들에 대한 map
         * <p>
         *     key : 자식노드의 노드이름
         * <p>
         *     value : 자식 노드
         * </p>
         */
        private final Map<String, PathNode> children;

        /**
         * 현대 노드에 대한 fucntion
         * <p>
         *     key : request의 method
         * <p>
         *     value : function handler
         * </p>
         */
        private final Map<Methods, FunctionHandler> methods;
        /**
         * 현재 노드의 이름
         */
        private final String path;
        /**
         * 현재 노드에 대한 path variable
         * <p>
         *     만약 null이라면 path variable이 자식에 없음
         * <p>
         *     pathvariable이 HttpRequest에 path variable map의 key값이 된다.
         * </p>
         * @see HttpRequest#addPathVariable(String, String)
         */
        private String pathvariable;
        /**
         * 이 노드에 대한 접근이 로그인이 필요한지에 대한 여부
         */
        private boolean secured;


        public PathNode(String path) {
            this.methods = new HashMap<>();
            this.children = new HashMap<>();
            this.path = path;
            this.secured = false;
            this.pathvariable = null;
        }

        PathNode secured(){
            this.secured = true;
            return this;
        }

        PathNode setPathVariable(String pathvariable){
            this.pathvariable = pathvariable;
            return this;
        }

        PathNode addChild(PathNode node){
            if(this.secured) node.secured();
            children.put(node.getPath(), node);
            return this;
        }

         PathNode Get(FunctionHandler getMethod) {
            methods.put(Methods.GET, getMethod);
            return this;
        }
         PathNode Post(FunctionHandler postMethod) {
            methods.put(Methods.POST, postMethod);
            return this;
        }
         PathNode Put(FunctionHandler putMethod) {
            methods.put(Methods.PUT, putMethod);
            return this;
        }
         PathNode Delete(FunctionHandler deleteMethod) {
            methods.put(Methods.DELETE, deleteMethod);
            return this;
        }

        //getter
        public FunctionHandler getMethod(Methods method) {
            return methods.get(method);
        }

        public String getPath() { return path; }

        public String getPathvariable() {
            return pathvariable;
        }

        public PathNode findChild(String pathname) {
            return children.get(pathname);
        }
    }

    /**
     * 해당 request에 대한 function hanlder 매칭
     * <p>
     *     이 과정에서 path에 path variable이 있으면 request class에 설정한다
     * </p>
     * @param request 매칭할 request class
     * @return 매칭된 function handler
     */
    public static FunctionHandler getPathMethod(HttpRequest request) {
        String[] routes = request.getUrl().getPath().split("/");
        PathNode current = root;
        if(routes.length > 1){
            for(int i = 1; i < routes.length; i++){
                PathNode next = current.findChild(routes[i]);
                if(next == null) {
                    // 하단에 path variable이  있다면
                    if(current.getPathvariable()!=null ){
                        //path variable 추가
                        request.addPathVariable(current.getPathvariable(), routes[i]);
                        if(++i >= routes.length){
                            break;
                        }
                    }else break;
                }
                current = next;
            }
        }

        //secured but no session
        SessionDAO sessionDAO = new SessionDAO();
        if(current.secured){
            if(request.getSessionid() == null || sessionDAO.findSession(request.getSessionid()) == null)
                return Unauthorized.getInstance();
        }

        return current.getMethod(request.getMethod());
    }
}
