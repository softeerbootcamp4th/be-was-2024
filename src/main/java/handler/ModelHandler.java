package handler;

import java.util.Map;

public interface ModelHandler<T> {
    T create(Map<String, String> fields);
}
