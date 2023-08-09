package kiis.edu.rating.common;

import kiis.edu.rating.exception.RecordNotFoundException;
import kiis.edu.rating.exception.VersionException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public abstract class SimpleCurdService<T extends BaseEntity> implements SimpleCurd<T> {
    private final JpaRepository<T, Long> mainRepository;

    @Override
    public List<T> findAll() {
        return mainRepository.findAll();
    }

    @Override
    public T findById(long id) {
        return mainRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Record", id));
    }

    @Override
    public T create(T entity) {
        return mainRepository.save(entity);
    }

    @Override
    public T update(T entity, long id) {
        final T old = findById(id);
        if (!Objects.equals(old.version, entity.version)) {
            throw new VersionException();
        }
        BeanUtils.copyProperties(entity, old);
        return mainRepository.save(old);
    }

    @Override
    public T delete(long id) {
        final T entity = findById(id);
        entity.isDeleted = false;
        return mainRepository.save(entity);
    }
}
