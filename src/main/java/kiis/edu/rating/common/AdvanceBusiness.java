package kiis.edu.rating.common;

import org.springframework.data.domain.Page;

public interface AdvanceBusiness<T extends BaseEntity> extends SimpleCurd<T> {
    Page<T> findAll(int page, int limit);

    Page<T> findAll(T exampleEntity, int page, int limit);
}
