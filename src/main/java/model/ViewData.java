package model;

public class ViewData {
    private final String url;
    private final int redirectCode;
    private final int statusCode;
    private final String cookie;
    private final String title;

    private ViewData(ViewData.Builder builder) {
        this.url = builder.url;
        this.redirectCode = builder.redirectCode;
        this.statusCode = builder.statusCode;
        this.cookie = builder.cookie;
        this.title = builder.title;
    }

    public static class Builder {
        private String url;
        private int redirectCode = 0;
        private int statusCode = 0;
        private String cookie = "";
        private String title = "";

        public ViewData.Builder url(String url) {
            this.url = url;
            return this;
        }

        public ViewData.Builder redirectCode(int redirectCode) {
            this.redirectCode = redirectCode;
            return this;
        }

        public ViewData.Builder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public ViewData.Builder cookie(String sessionId) {
            this.cookie = sessionId;
            return this;
        }

        public ViewData.Builder title(String title) {
            this.title = title;
            return this;
        }

        public ViewData build() {
            return new ViewData(this);
        }
    }

    public String getUrl() {
        return this.url;
    }

    public int getRedirectCode() {
        return this.redirectCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getCookie() {
        return this.cookie;
    }

    public String getTitle() {
        return this.title;
    }
}
