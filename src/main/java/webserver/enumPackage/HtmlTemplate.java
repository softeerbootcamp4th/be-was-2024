package webserver.enumPackage;

public enum HtmlTemplate {
    USER_LIST("<html><head><title>User List</title></head><body>" +
            "<h1>User List</h1><ul>{{userList}}</ul></body></html>"),
    ERROR_PAGE("<html><head><title>Error</title></head><body>" +
            "<h1>Error</h1><p>{{errorMessage}}</p><script>" +
            "alert('{{errorMessage}}');window.location.href = '{{redirectUrl}}';</script>" +
            "</body></html>");

    private final String template;

    HtmlTemplate(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}