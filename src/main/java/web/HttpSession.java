package web;

import common.StringUtils;

/**
 * HTTP 세션에 대한 정보를 담는 POJO
 */
public class HttpSession {

    private String name;
    private String id;
    private long maxAge;
    private String path;
    private String domain;
    private Boolean secure;
    private Boolean httpOnly;

    public HttpSession() {}

    private HttpSession(
            String name,
            long maxAge,
            String path,
            String domain,
            Boolean secure,
            Boolean httpOnly
    ) {
        this.name = name;
        this.id = StringUtils.createRandomUUID();
        this.maxAge = maxAge;
        this.path = path;
        this.domain = domain;
        this.secure = secure;
        this.httpOnly = httpOnly;
    }

    public static class HttpSessionBuilder {
        private String name;
        private String id;
        private long maxAge = 1800*1000; // 세션의 기본 지속 유효시간을 1800s(30min)으로 설정
        private String path = "/";
        private String domain = "localhost"; //TODO("프로퍼리 파일로 관리")
        private Boolean secure = true;
        private Boolean httpOnly = true;

        public HttpSessionBuilder name(String name) {
            this.name = name;
            return this;
        }

        public HttpSessionBuilder id(String id) {
            this.id = id;
            return this;
        }

        public HttpSessionBuilder maxAge(long maxAge) {
            this.maxAge = maxAge;
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
                name, maxAge, path, domain, secure, httpOnly
            );
        }
    }

    public String getId() {
        return id;
    }

    public long getMaxAge() {
        return maxAge;
    }

    public String getString() {
        StringBuilder session = new StringBuilder();
        session.append(name).append("=").append(id).append(" ");

        if(maxAge!=0) {
            session.append("; Max-Age=").append(maxAge).append(" ");
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

    public void setName(String name) {
        this.name = name;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setHttpOnly(Boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public void setMaxAge(long maxAge) {
        this.maxAge = maxAge;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSecure(Boolean secure) {
        this.secure = secure;
    }

    public void setId(String id) {
        this.id = id;
    }

}
