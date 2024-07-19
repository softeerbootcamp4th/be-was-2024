package db;

import java.util.Optional;

public interface LongIdDatabase<T> extends Database<T>{
    Optional<T> findById(Long id);
}
