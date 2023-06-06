package kiis.edu.rating.features.subject.plan;

import kiis.edu.rating.features.subject.base.SubjectRepository;
import kiis.edu.rating.features.subject.condition.SubjectConditionRepository;
import kiis.edu.rating.features.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@RestController
@AllArgsConstructor
@RequestMapping(path = "/subject-plan")
public class SubjectPlanController {
    private final SubjectPlanRepository subjectPlanRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final SubjectConditionRepository subjectConditionRepository;

    private final SubjectPlanService subjectPlanService;

    @GetMapping("/{id}")
    public SubjectPlanEntity getById(@PathVariable long id) {
        return subjectPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Rating with id : " + id));
    }

    @GetMapping("")
    public List<SubjectPlanEntity> getAll() {
        return subjectPlanRepository.findAll();
    }

    @GetMapping("/group")
    public List<BigGroup> getAllByGroup() {

        return subjectPlanService.createList();
    }

    @PostMapping("")
    public void create(@RequestBody @Valid SubjectPlanRequest request) {
        validateRequest(request);

        if (subjectPlanRepository.existsByUserId(request.userId))
            throw new IllegalArgumentException("Existed, please use Update Method");

        subjectPlanRepository.save(request.toEntity());
    }

    @PutMapping("/{id}")
    public void update(@PathVariable long id, @RequestBody @Valid SubjectPlanRequest request) {
        validateRequest(request);

        SubjectPlanEntity subjectRatingEntity = request.toEntity();
        subjectRatingEntity.id = id;
        subjectPlanRepository.save(subjectRatingEntity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        subjectPlanRepository.deleteById(id);
    }

    //------------------------------------------------------------------------------------------------
    // private
    //------------------------------------------------------------------------------------------------

    private void validateRequest(SubjectPlanRequest request) {
        List<Long> idList = request.subjectIdList;

        if (!userRepository.existsById(request.userId))
            throw new IllegalArgumentException("No User with id : " + request.userId);

        List<Long> existIds = subjectRepository.findByIdList(idList);
        List<Long> remainIdList = idList.stream().filter(id -> !existIds.contains(id)).collect(Collectors.toList());
        if (remainIdList.size() > 0)
            throw new IllegalArgumentException("Some Subject are not exist, ID: " + remainIdList);
    }

    @RequiredArgsConstructor
    private static class SubjectPlanRequest extends SubjectPlanEntity {
        private final long id;
        private final Instant createdAt, updatedAt;
        private final boolean disable;

        public final List<Long> subjectIdList;

        public SubjectPlanEntity toEntity() {
            SubjectPlanEntity entity = new SubjectPlanEntity(this.userId);
            entity.setSubjectIds(subjectIdList);
            return entity;
        }
    }
}
