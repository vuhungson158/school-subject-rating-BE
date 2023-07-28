package kiis.edu.rating.features.common;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
public class SimpleCurdController<T extends BaseEntity> implements SimpleCurd<T> {
    private final SimpleCurdService<T> service;

    @Override
    @GetMapping
    public List<T> findAll() {
        return service.findAll();
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return service.findAll(pageable);
    }

    @Override
    public Page<T> findAll(Example<T> example, Pageable pageable) {
        return service.findAll(example, pageable);
    }

    @Override
    @GetMapping("/{id}")
    public T findById(@PathVariable long id) {
        return service.findById(id);
    }

    @Override
    @PostMapping
    public T create(@RequestBody T entity) {
        return service.create(entity);
    }

    @Override
    @PutMapping("/{id}")
    public T update(@RequestBody T entity, @PathVariable long id) {
        return service.update(entity, id);
    }

    @Override
    @DeleteMapping("/{id}")
    public T delete(@PathVariable long id) {
        return service.delete(id);
    }
}
