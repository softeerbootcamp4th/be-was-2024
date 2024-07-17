package web;

public enum HeaderKey {
    CONTENT_LENGTH("content-length"),
    CONTENT_TYPE("content-type"),
    LOCATION("location"),
    SET_COOKIE("set-cookie"),
    ACCEPT("accept"),
    COOKIE("cookie");

    private final String key;

    HeaderKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
