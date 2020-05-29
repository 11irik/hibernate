package repository;

import java.io.Serializable;
import java.util.List;


public interface Dao<T> {
    public Serializable create(T entity);

    public void update(T entity, Long id);

    public T findById(Long id);

    public void delete(Long id);

    public List<T> findAll();
   }
