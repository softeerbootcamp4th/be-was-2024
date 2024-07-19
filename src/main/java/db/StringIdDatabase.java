package db;

import java.util.Optional;

public interface StringIdDatabase<T> extends Database<T>{
    Optional<T> findById(String id);
}
