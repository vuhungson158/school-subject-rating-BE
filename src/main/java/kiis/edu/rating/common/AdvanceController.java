package kiis.edu.rating.common;

import kiis.edu.rating.aop.AllowMethod;
import kiis.edu.rating.features.user.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import static kiis.edu.rating.features.user.UserRole.Method.FIND_BY_FILTER;
import static kiis.edu.rating.features.user.UserRole.Method.FIND_BY_PAGEABLE;

public abstract class AdvanceController<T extends BaseEntity> extends SimpleCurdController<T> implements AdvanceBusiness<T> {

    final AdvanceService<T> mainService;

    public AdvanceController(AdvanceService<T> mainService) {
        super(mainService);
        this.mainService = mainService;
    }

    @Override
    @GetMapping("/pageable")
    @AllowMethod(FIND_BY_PAGEABLE)
    public Page<T> findAll(@RequestParam int page, @RequestParam int limit) {
        return mainService.findAll(page, limit);
    }

    @Override
    @PostMapping("/filter")
    @AllowMethod(FIND_BY_FILTER)
    public Page<T> findAll(@RequestBody T exampleEntity, @RequestParam int page, @RequestParam int limit) {
        return mainService.findAll(exampleEntity, page, limit);
    }
}
