package kiis.edu.rating.common;

import java.util.List;

public interface SimpleCurd<T extends BaseEntity> {

    List<T> findAll();

    T findById(long id);

    T create(T entity);

    T update(T entity, long id);

    T delete(long id);
}
