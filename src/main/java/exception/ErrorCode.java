package exception;

public enum ErrorCode {
    USER_NOT_FOUND(404, "존재하지 않는 사용자입니다.");

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus(){
        return status;
    }

    public String getMessage(){
        return message;
    }
}
