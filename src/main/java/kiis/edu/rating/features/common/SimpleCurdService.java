package kiis.edu.rating.features.common;

import kiis.edu.rating.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@RequiredArgsConstructor
public class SimpleCurdService<T extends BaseEntity> implements SimpleCurd<T> {
    private final JpaRepository<T, Long> repository;

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<T> findAll(Example<T> example, Pageable pageable) {
        return repository.findAll(example, pageable);
    }

    @Override
    public T findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Record", id));
    }

    @Override
    public T create(T entity) {
        return repository.save(entity);
    }

    @Override
    public T update(T entity, long id) {
        final T old = findById(id);
        BeanUtils.copyProperties(entity, old);
        return repository.save(old);
    }

    @Override
    public void delete(long id) {
        repository.delete(findById(id));
    }
}
