package webserver.enums;

public enum HtmlFlag {
    ERROR_MESSAGE("<-- ERROR MESSAGE -->"),
    CONTENT("<-- CONTENT -->");

    private final String flag;

    private HtmlFlag(String flag) {
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }


}
