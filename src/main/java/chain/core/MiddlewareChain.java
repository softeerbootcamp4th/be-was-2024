package chain.core;


import http.MyHttpRequest;
import http.MyHttpResponse;

/**
 * 체인의 기본 구현체.
 */
public abstract class MiddlewareChain {
    protected MiddlewareChain nextChain;

    public MiddlewareChain() {
        this.nextChain = null;
    }

    public MiddlewareChain(MiddlewareChain nextChain) {
        setNext(nextChain);
    }

    /**
     * 요청 & 응답을 기반으로 작업을 진행하는 영역
     * @param req 들어온 요청
     * @param res 들어온 응답
     */
    public abstract void act(MyHttpRequest req, MyHttpResponse res);

    /**
     * 다음 체인을 등록한다
     * @param next
     */
    public final void setNext(MiddlewareChain next) {
        this.nextChain = next;
    }

    /**
     * 다음 미들웨어로 요청을 넘긴다
     * @param req 들어온 요청
     * @param res 들어온 응답
     */
    protected final void next(MyHttpRequest req, MyHttpResponse res) {
        if (this.nextChain != null) nextChain.act(req, res);
    }
}
