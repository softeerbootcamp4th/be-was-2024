package web;

public enum RestUri {
    SIGN_UP("/signUp"),
    SIGN_IN("/signIn"),
    LOGOUT("/logout"),
    USER_LIST("/user/list"),
    DATABASE_INIT("/database/init"),
    ARTICLE("/article");

    private final String uri;

    RestUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
}
