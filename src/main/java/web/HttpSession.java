package web;

import java.util.UUID;

public class HttpSession {

    private String SID;
    private String expires;
    private String path;
    private String domain;
    private Boolean secure;
    private Boolean httpOnly;

    public HttpSession() {}

    private HttpSession(
            String expires,
            String path,
            String domain,
            Boolean secure,
            Boolean httpOnly
    ) {
        this.SID = createUID();
        this.expires = expires;
        this.path = path;
        this.domain = domain;
        this.secure = secure;
        this.httpOnly = httpOnly;
    }

    public static class HttpSessionBuilder {
        private String expires;
        private String path = "/";
        private String domain = "localhost"; //TODO("프로퍼리 파일로 관리")
        private Boolean secure = true;
        private Boolean httpOnly = true;

        public HttpSessionBuilder expires(String expires) {
            this.expires = expires;
            return this;
        }

        public HttpSessionBuilder path(String path) {
            this.path = path;
            return this;
        }

        public HttpSessionBuilder domain(String domain) {
            this.domain = domain;
            return this;
        }

        public HttpSessionBuilder secure(Boolean secure) {
            this.secure = secure;
            return this;
        }

        public HttpSessionBuilder httpOnly(Boolean httpOnly) {
            this.httpOnly = httpOnly;
            return this;
        }

        public HttpSession build() {
            return new HttpSession(
                expires, path, domain, secure, httpOnly
            );
        }
    }

    public String getString() {
        StringBuilder session = new StringBuilder();
        session.append("SID=").append(SID).append(" ");

        if(!expires.isEmpty()) {
            session.append("; Expires=").append(expires).append(" ");
        }
        if(!path.isEmpty()) {
            session.append("; Path=").append(path).append(" ");
        }
        if(!domain.isEmpty()) {
            session.append("; Domain=").append(path).append(" ");
        }
        if(secure) {
            session.append("; Secure");
        }
        if(httpOnly) {
            session.append("; HttpOnly");
        }

        return session.toString();
    }

    private String createUID() {
        return UUID.randomUUID().toString();
    }

}
