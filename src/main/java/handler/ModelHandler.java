package handler;

import java.util.Collection;
import java.util.Map;

public interface ModelHandler<T> {
    T create(Map<String, String> fields);
    T findById(String id);
    Collection<T> findAll();
}
