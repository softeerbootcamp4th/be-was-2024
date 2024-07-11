package webserver.http.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum StatusCode {
    CODE401(401, "Unauthorized"),
    CODE404(404, "not found"),
    CODE403(403, "Forbidden"),
    CODE405(405, "Method Not Allowed"),

    CODE300(300, "Multiple Choices"),
    CODE301(301, "Moved Permanently"),
    CODE302(302, "Found"),
    CODE303(303, "See Other"),
    CODE304(304, "Not Modified"),

    CODE200(200, "OK"),
    CODE201(201, "Created"),
    CODE202(202, "Accepted"),
    CODE203(203, "Non-Authoritative Information"),
    CODE204(204, "No Content"),
    CODE205(205, "Reset Content"),
    ;

    int code;
    String description;

    StatusCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    private int code (){
        return code;
    }

    private static final Map<Integer, StatusCode> BY_CODE =
            Stream.of(values()).collect(Collectors.toMap(StatusCode::code, e -> e));

    public static StatusCode valueOfCode(int code) {
        return BY_CODE.get(code);
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getStartline() {
        return "HTTP/1.1 " +code + " " + description + "\r\n";
    }
}
