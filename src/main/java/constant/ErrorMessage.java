package constant;

/**
 * Error 응답 메시지 Enum
 */
public enum ErrorMessage {
    ERROR_MESSAGE_400("400 Bad Request"),
    ERROR_MESSAGE_404("404 Not Found"),
    ERROR_MESSAGE_405("405 Method Not Allowed"),
    ERROR_MESSAGE_500("500 Internal Server Error");

    final String errorMessage;

    ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return "<html>" +
                "<head><title>" + errorMessage + "</title></head>" +
                "<body><h1>" + errorMessage + "</h1></body>" +
                "</html>";
    }
}
