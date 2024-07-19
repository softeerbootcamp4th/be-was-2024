package db;

import java.util.List;
import java.util.Optional;

public interface Database<T> {

    public List<T> findAll();

    public void save(T t);
}
