package kiis.edu.rating.common;

import kiis.edu.rating.aop.AllowMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static kiis.edu.rating.features.user.UserRole.Method.FIND_ALL;

@RequiredArgsConstructor
public abstract class SimpleCurdController<T extends BaseEntity> implements SimpleCurd<T> {
    private final SimpleCurdService<T> mainService;

    @Override
    @GetMapping
    @AllowMethod(FIND_ALL)
    public List<T> findAll() {
        return mainService.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public T findById(@PathVariable long id) {
        return mainService.findById(id);
    }

    @Override
    @PostMapping
    public T create(@RequestBody T entity) {
        return mainService.create(entity);
    }

    @Override
    @PutMapping("/{id}")
    public T update(@RequestBody T entity, @PathVariable long id) {
        return mainService.update(entity, id);
    }

    @Override
    @DeleteMapping("/{id}")
    public T delete(@PathVariable long id) {
        return mainService.delete(id);
    }
}
