package util;

public class HttpStatus {
    public static final int SC_OK = 200;
    public static final int SC_CREATED = 201;
    public static final int SC_ACCEPTED = 202;
    public static final int SC_NON_AUTHORITATIVE_INFORMATION = 203;
    public static final int SC_NO_CONTENT = 204;
    public static final int SC_RESET_CONTENT = 205;

    public static final int SC_MULTIPLE_CHOICES = 300;
    public static final int SC_MOVED_PERMANENTLY = 301;
    public static final int SC_FOUND = 302;
    public static final int SC_SEE_OTHER = 303;
    public static final int SC_NOT_MODIFIED = 304;
    public static final int SC_USE_PROXY = 305;
    public static final int SC_UNUSED = 306;
    public static final int SC_TEMPORARY_REDIRECT = 307;
    public static final int SC_PERMANENT_REDIRECT = 308;

    public static final int SC_BAD_REQUEST = 400;
    public static final int SC_UNAUTHORIZED = 401;
    public static final int SC_FORBIDDEN = 403;
    public static final int SC_NOT_FOUND = 404;
    public static final int SC_METHOD_NOT_ALLOWED = 405;
    public static final int SC_NOT_ACCEPTABLE = 406;
    public static final int SC_PROXY_AUTHENTICATION_REQUIRED = 407;
    public static final int SC_REQUEST_TIMEOUT = 408;
    public static final int SC_CONFLICT = 409;
    public static final int SC_GONE = 410;
    public static final int SC_LENGTH_REQUIRED = 411;
    public static final int SC_PRECONDITION_FAILED = 412;
    public static final int SC_REQUEST_ENTITY_TOO_LARGE = 413;
    public static final int SC_REQUEST_URI_TOO_LONG = 414;
    public static final int SC_UNSUPPORTED_MEDIA_TYPE = 415;
    public static final int SC_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    public static final int SC_EXPECTATION_FAILED = 417;

    public static final int SC_INTERNAL_SERVER_ERROR = 500;
    public static final int SC_NOT_IMPLEMENTED = 501;
    public static final int SC_BAD_GATEWAY = 502;
    public static final int SC_SERVICE_UNAVAILABLE = 503;
    public static final int SC_GATEWAY_TIMEOUT = 504;
    public static final int SC_HTTP_VERSION_NOT_SUPPORTED = 505;
    public static final int SC_INSUFFICIENT_STORAGE = 506;
    public static final int SC_LOOP_DETECTED = 507;
    public static final int SC_NOT_EXTENDED = 510;
    public static final int SC_NETWORK_AUTHENTICATION_REQUIRED = 511;


    public static String getStautusCodeString(int statusCode) throws IllegalStateException {
        return switch (statusCode) {
            case SC_OK -> "OK";
            case SC_CREATED -> "Created";
            case SC_ACCEPTED -> "Accepted";
            case SC_NON_AUTHORITATIVE_INFORMATION -> "Non-Authoritative Information";
            case SC_NO_CONTENT -> "No Content";
            case SC_RESET_CONTENT -> "Reset Content";

            case SC_MULTIPLE_CHOICES -> "Multiple Choices";
            case SC_MOVED_PERMANENTLY -> "Moved Permanently";
            case SC_FOUND -> "Found";
            case SC_SEE_OTHER -> "See Other";
            case SC_NOT_MODIFIED -> "Not Modified";
            case SC_USE_PROXY -> "Use Proxy";
            case SC_UNUSED -> "Unused";
            case SC_TEMPORARY_REDIRECT -> "Temporary Redirect";
            case SC_PERMANENT_REDIRECT -> "Permanent Redirect";

            case SC_BAD_REQUEST -> "Bad Request";
            case SC_UNAUTHORIZED -> "Unauthorized";
            case SC_FORBIDDEN -> "Forbidden";
            case SC_NOT_FOUND -> "Not Found";
            case SC_METHOD_NOT_ALLOWED -> "Method Not Allowed";
            case SC_NOT_ACCEPTABLE -> "Not Acceptable";
            case SC_PROXY_AUTHENTICATION_REQUIRED -> "Proxy Authentication Required";
            case SC_REQUEST_TIMEOUT -> "Request Timeout";
            case SC_CONFLICT -> "Conflict";
            case SC_GONE -> "Gone";
            case SC_LENGTH_REQUIRED -> "Length Required";
            case SC_PRECONDITION_FAILED -> "Precondition Failed";
            case SC_REQUEST_ENTITY_TOO_LARGE -> "Request Entity Too Large";
            case SC_REQUEST_URI_TOO_LONG -> "Request URI Too Long";
            case SC_UNSUPPORTED_MEDIA_TYPE -> "Unsupported Media Type";
            case SC_REQUESTED_RANGE_NOT_SATISFIABLE -> "Requested Range Not Satisfiable";
            case SC_EXPECTATION_FAILED -> "Expectation Failed";

            case SC_INTERNAL_SERVER_ERROR -> "Internal Server Error";
            case SC_NOT_IMPLEMENTED -> "Not Implemented";
            case SC_BAD_GATEWAY -> "Bad Gateway";
            case SC_SERVICE_UNAVAILABLE -> "Service Unavailable";
            case SC_GATEWAY_TIMEOUT -> "Gateway Timeout";
            case SC_HTTP_VERSION_NOT_SUPPORTED -> "HTTP Version Not Supported";
            case SC_INSUFFICIENT_STORAGE -> "Insufficient Storage";
            case SC_LOOP_DETECTED -> "Loop Detected";
            case SC_NOT_EXTENDED -> "Not Extended";
            case SC_NETWORK_AUTHENTICATION_REQUIRED -> "Network Authentication Required";

            default -> throw new IllegalStateException("Unexpected Status Code" + statusCode);
        };
    }
}
