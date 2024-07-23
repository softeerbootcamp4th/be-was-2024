package chain.core;

import http.MyHttpRequest;
import http.MyHttpResponse;

/**
 * 체인 목록을 관리하는 클래스. 링크드 리스트처럼 동작한다.
 */
public class ChainManager {
    private MiddlewareChain startChain;
    private MiddlewareChain endChain;

    public ChainManager(MiddlewareChain ... chains) {
        for(var chain : chains) {
            putChain(chain);
        }
    }

    /**
     * 체인을 등록한다
     * @param chain 등록할 체인
     */
    public void putChain(MiddlewareChain chain) {
        if (startChain == null) {
            startChain = chain;
            endChain = chain;
        } else {
            endChain.setNext(chain);
            endChain = chain;
        }
    }

    /**
     * 요청 / 응답을 기반으로 작업을 수행한다. 실제 작업은 각 MiddlewareChain이 수행한다.
     */
    public void execute(MyHttpRequest req, MyHttpResponse res) {
        startChain.act(req,res);
    }
}
