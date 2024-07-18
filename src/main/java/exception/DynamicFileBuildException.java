package exception;

// 동적 파일 생성 중에 발생하는 예외
public class DynamicFileBuildException extends RuntimeException {
    public DynamicFileBuildException(String message) {
        super(message);
    }
}
