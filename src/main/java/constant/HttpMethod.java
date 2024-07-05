package constant;

public enum HttpMethod {
    GET(0),
    POST(1),
    PATCH(2),
    PUT(3),
    DELETE(4);

    final int handlerMapIdx;

    HttpMethod(int handlerMapIdx) {
        this.handlerMapIdx = handlerMapIdx;
    }

    public int getHandlerMapIdx() {
        return handlerMapIdx;
    }
}
