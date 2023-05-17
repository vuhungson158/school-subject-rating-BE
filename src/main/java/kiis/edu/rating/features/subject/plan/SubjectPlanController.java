package kiis.edu.rating.features.subject.plan;

import kiis.edu.rating.features.common.RequestDTO;
import kiis.edu.rating.features.subject.base.SubjectRepository;
import kiis.edu.rating.features.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping("/{id}")
    public SubjectPlan getById(@PathVariable long id) {
        return subjectPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Rating with id : " + id));
    }

    @GetMapping("")
    public List<SubjectPlan> getAll() {
        return subjectPlanRepository.findAll();
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

        SubjectPlan subjectRatingEntity = request.toEntity();
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

    private void validateRequest(SubjectPlanRequest request){
        List<Long> idList = request.subjectIdList;

        if (!userRepository.existsById(request.userId))
            throw new IllegalArgumentException("No User with id : " + request.userId);

        List<Long> existIds = subjectRepository.existsByIdList(idList);
        idList.removeAll(existIds);
        if (idList.size() > 0)
            throw new IllegalArgumentException("Some Subject are not exist, ID: " + idList);
    }

    @AllArgsConstructor
    private static class SubjectPlanRequest implements RequestDTO {
        public long userId;
        public List<Long> subjectIdList;

        public SubjectPlan toEntity() {
            SubjectPlan entity = new SubjectPlan();
            entity.userId = this.userId;
            entity.subjectIds = this.subjectIdList.stream().map(String::valueOf)
                    .collect(Collectors.joining("_"));;
            return entity;
        }
    }
}
