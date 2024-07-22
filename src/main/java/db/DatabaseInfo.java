package db;

/**
 * 데이터베이스 커넥션을 위한 정보를 저장하는 Enum 클래스
 * 프로퍼티 파일과 환경변수를 이용하여 관리하는 것이 더 좋아보인다
 */
public enum DatabaseInfo {
    DB_URL("jdbc:h2:tcp://localhost/~/test"),
    DB_USERNAME("sa"),
    DB_PASSWORD("");

    private final String key;

    DatabaseInfo(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
