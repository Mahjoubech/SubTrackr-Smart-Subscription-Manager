package dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<T> {
    void create(T Obj);
    void update(T Obj);
    void delete(String id);
    Optional<T> findById(String id);
    List<T> findAll();
}
