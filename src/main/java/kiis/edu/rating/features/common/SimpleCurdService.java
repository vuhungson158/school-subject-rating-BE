package kiis.edu.rating.features.common;

import kiis.edu.rating.exception.RecordNotFoundException;
import kiis.edu.rating.exception.VersionException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RequiredArgsConstructor
public class SimpleCurdService<T extends BaseEntity> implements SimpleCurd<T> {
    private final JpaRepository<T, Long> repository;

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    @PreAuthorize("hasAuthority('FILE_UPDATE')")
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
        if (old.version != entity.version) {
            throw new VersionException();
        }
        BeanUtils.copyProperties(entity, old);
        return repository.save(old);
    }

    @Override
    public T delete(long id) {
        final T entity = findById(id);
        entity.disable = false;
        return repository.save(entity);
    }
}
