package exception;

/**
 * 동적 파일 생성 중에 발생하는 예외 클래스
 */
public class DynamicFileBuildException extends RuntimeException {
    public DynamicFileBuildException(String message) {
        super(message);
    }
}
