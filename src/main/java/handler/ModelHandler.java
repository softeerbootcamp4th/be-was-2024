package handler;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ModelHandler<T> {
    Optional<T> create(Map<String, String> fields);
    Optional<T> findById(String id);
    List<T> findAll();
}
