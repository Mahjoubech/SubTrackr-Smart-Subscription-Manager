package dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<T> {
    void save(T obj);
    Optional<T> findById(String id);
    List<T> findAll();
    void update(T obj);
    void delete(String id);
}
