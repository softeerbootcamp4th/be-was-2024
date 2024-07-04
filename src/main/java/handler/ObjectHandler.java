package handler;

import java.util.Map;

public interface ObjectHandler<T> {
    T create(Map<String, String> fields);
}
