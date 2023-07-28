package kiis.edu.rating.features.common;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SimpleCurd<T extends BaseEntity> {

    List<T> findAll();

    Page<T> findAll(Pageable pageable);

    Page<T> findAll(Example<T> example, Pageable pageable);

    T findById(long id);

    T create(T entity);

    T update(T entity, long id);

    T delete(long id);
}
