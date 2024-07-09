package http;

public enum HttpStatus {

    OK(200, "C001", "success"),
    FOUND(302, "C002", "redirect"),
    NOT_FOUND(404, "C003", "not found");

    private int status;
    private String code;
    private String message;

    HttpStatus(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus(){
        return status;
    }

    public String getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }
}
