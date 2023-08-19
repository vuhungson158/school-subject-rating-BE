package kiis.edu.rating.common;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class AdvanceService<T extends BaseEntity>
        extends SimpleCurdService<T>
        implements AdvanceBusiness<T> {

    final JpaRepository<T, Long> mainRepository;

    public AdvanceService(JpaRepository<T, Long> mainRepository) {
        super(mainRepository);
        this.mainRepository = mainRepository;
    }

    @Override
    public Page<T> findAll(int page, int limit) {
        final Pageable pageable = PageRequest.of(page, limit);
        return mainRepository.findAll(pageable);
    }

    @Override
    public Page<T> findAll(T exampleEntity, int page, int limit) {
        final Pageable pageable = PageRequest.of(page, limit);
        final Example<T> example = Example.of(exampleEntity);
        return mainRepository.findAll(example, pageable);
    }
}
