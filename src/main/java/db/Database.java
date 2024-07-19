package db;

import java.util.List;
import java.util.Optional;

public interface Database<T,ID> {


    public Optional<T> findById(ID id);

    public List<T> findAll();

    public void save(T t);

    public void delete(T t);
}
