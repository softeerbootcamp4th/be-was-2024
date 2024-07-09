package chain.core;

import http.MyHttpRequest;
import http.MyHttpResponse;

public class ChainManager {
    private MiddlewareChain startChain;
    private MiddlewareChain endChain;

    public ChainManager(MiddlewareChain ... chains) {
        for(var chain : chains) {
            putChain(chain);
        }
    }

    public void putChain(MiddlewareChain chain) {
        if (startChain == null) {
            startChain = chain;
            endChain = chain;
        } else {
            endChain.setNext(chain);
            endChain = chain;
        }
    }

    public void execute(MyHttpRequest req, MyHttpResponse res) {
        startChain.act(req,res);
    }
}
