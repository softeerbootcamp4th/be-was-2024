package chain.core;


import http.MyHttpRequest;
import http.MyHttpResponse;

public abstract class MiddlewareChain {
    protected MiddlewareChain nextChain;

    public MiddlewareChain() {
        this.nextChain = null;
    }

    public MiddlewareChain(MiddlewareChain nextChain) {
        setNext(nextChain);
    }

    /**
     * 실제로 작업을 진행하는 부분
     * @param req 들어온 요청
     * @param res 들어온 응답
     */
    public abstract void act(MyHttpRequest req, MyHttpResponse res);

    public final void setNext(MiddlewareChain next) {
        this.nextChain = next;
    }

    /**
     * 다음 미들웨어로 요청을 넘기는 메서드
     * @param req 들어온 요청
     * @param res 들어온 응답
     */
    protected final void next(MyHttpRequest req, MyHttpResponse res) {
        if (this.nextChain != null) nextChain.act(req, res);
    }
}
