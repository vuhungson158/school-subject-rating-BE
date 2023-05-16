package kiis.edu.rating.features.subject.registration.plan;

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
@RequestMapping(path = "/subject-registration-plan")
public class SubjectRegistrationPlanController {
    private final SubjectRegistrationPlanRepository subjectRegistrationPlanRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    @GetMapping("/{id}")
    public SubjectRegistrationPlan getById(@PathVariable long id) {
        return subjectRegistrationPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Rating with id : " + id));
    }

    @GetMapping("")
    public List<SubjectRegistrationPlan> getAll() {
        return subjectRegistrationPlanRepository.findAll();
    }

    @PostMapping("")
    public void create(@RequestBody @Valid SubjectRegistrationPlanRequest request) {
        validateRequest(request);

        if (subjectRegistrationPlanRepository.existsByUserId(request.userId))
            throw new IllegalArgumentException("Existed, please use Update Method");

        subjectRegistrationPlanRepository.save(request.toEntity());
    }

    @PutMapping("/{id}")
    public void update(@PathVariable long id, @RequestBody @Valid SubjectRegistrationPlanRequest request) {
        validateRequest(request);

        SubjectRegistrationPlan subjectRatingEntity = request.toEntity();
        subjectRatingEntity.id = id;
        subjectRegistrationPlanRepository.save(subjectRatingEntity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        subjectRegistrationPlanRepository.deleteById(id);
    }

    //------------------------------------------------------------------------------------------------
    // private
    //------------------------------------------------------------------------------------------------

    private void validateRequest(SubjectRegistrationPlanRequest request){
        List<Long> idList = request.subjectIdList;

        if (!userRepository.existsById(request.userId))
            throw new IllegalArgumentException("No User with id : " + request.userId);

        List<Long> existIds = subjectRepository.existsByIdList(idList);
        idList.removeAll(existIds);
        if (idList.size() > 0)
            throw new IllegalArgumentException("Some Subject are not exist, ID: " + idList);
    }

    @AllArgsConstructor
    private static class SubjectRegistrationPlanRequest implements RequestDTO {
        public long userId;
        public List<Long> subjectIdList;

        public SubjectRegistrationPlan toEntity() {
            SubjectRegistrationPlan entity = new SubjectRegistrationPlan();
            entity.userId = this.userId;
            entity.subjectIds = this.subjectIdList.stream().map(String::valueOf)
                    .collect(Collectors.joining("_"));;
            return entity;
        }
    }
}
