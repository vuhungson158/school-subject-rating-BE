package kiis.edu.rating.features.subject.registration.condition;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kiis.edu.rating.features.common.RequestDTO;
import kiis.edu.rating.features.subject.base.SubjectRepository;
import kiis.edu.rating.features.subject.registration.plan.SubjectRegistrationPlanController;
import kiis.edu.rating.helper.Util;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@SuppressWarnings("unused")
@RestController
@AllArgsConstructor
@RequestMapping(path = "/subject-registration-condition")
public class SubjectRegistrationConditionController {
    private final SubjectRegistrationConditionRepository subjectRegistrationConditionRepository;
    private final SubjectRepository subjectRepository;

    @GetMapping("/{id}")
    public SubjectRegistrationCondition getById(@PathVariable long id) {
        return subjectRegistrationConditionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Rating with id : " + id));
    }

    @GetMapping("")
    public List<SubjectRegistrationCondition> getAll() {
        return subjectRegistrationConditionRepository.findAll();
    }

    @PostMapping("")
    public void create(@RequestBody @Valid SubjectRegistrationConditionRequest request) {
        validateRequest(request);

        subjectRegistrationConditionRepository.save(request.toEntity());
    }

    @PutMapping("/{id}")
    public void update(@PathVariable long id, @RequestBody @Valid SubjectRegistrationConditionRequest request) {
        validateRequest(request);

        SubjectRegistrationCondition subjectRatingEntity = request.toEntity();
        subjectRatingEntity.id = id;
        subjectRegistrationConditionRepository.save(subjectRatingEntity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        subjectRegistrationConditionRepository.deleteById(id);
    }

    //------------------------------------------------------------------------------------------------
    // private
    //------------------------------------------------------------------------------------------------

    private void validateRequest(SubjectRegistrationConditionRequest request){
        Long fromId = request.fromId;
        Long toId = request.toId;
        if (!subjectRepository.existsById(fromId))
            throw new IllegalArgumentException("No Subject with ID: " + fromId);
        if (!subjectRepository.existsById(toId))
            throw new IllegalArgumentException("No Subject with ID: " + toId);
        if ((long) fromId == toId)
            throw new IllegalArgumentException("From ID and To ID can not be same");

    }

    @AllArgsConstructor
    private static class SubjectRegistrationConditionRequest implements RequestDTO {
        public Long fromId, toId;

        public SubjectRegistrationCondition toEntity() {
            return Util.mapping(this, SubjectRegistrationCondition.class);
        }
    }
}
