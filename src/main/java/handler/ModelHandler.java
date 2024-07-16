package handler;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * DB 테이블에 대응되는 Model을 관리할 때 공통적으로 필요한 CRUD를 추상화한 인터페이스
 * 이를 상속받아 각 Model에 맞게 구현한다.
 */
public interface ModelHandler<T> {
    Optional<T> create(Map<String, String> fields);
    Optional<T> findById(String id);
    List<T> findAll();
}
