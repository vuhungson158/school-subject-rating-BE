package kiis.edu.rating.features.subject.condition;

import kiis.edu.rating.exception.RecordNotFoundException;
import kiis.edu.rating.features.common.RequestDTO;
import kiis.edu.rating.features.subject.base.SubjectRepository;
import kiis.edu.rating.helper.Util;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
@RestController
@AllArgsConstructor
@RequestMapping(path = "/subject-condition")
public class SubjectConditionController {
    private final SubjectConditionRepository subjectConditionRepository;
    private final SubjectRepository subjectRepository;

    @GetMapping("/{id}")
    public SubjectCondition getById(@PathVariable long id) {
        return subjectConditionRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Condition", id));
    }

    @GetMapping("")
    public ConditionGraph getAll() {
        return new ConditionGraph(subjectConditionRepository.findAll());
    }

    @PostMapping("")
    public void create(@RequestBody @Valid SubjectConditionRequest request) {
        validateRequest(request);

        subjectConditionRepository.save(request.toEntity());
    }

    @PutMapping("/{id}")
    public void update(@PathVariable long id, @RequestBody @Valid SubjectConditionRequest request) {
        validateRequest(request);

        SubjectCondition subjectRatingEntity = request.toEntity();
        subjectRatingEntity.id = id;
        subjectConditionRepository.save(subjectRatingEntity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        subjectConditionRepository.deleteById(id);
    }

    //------------------------------------------------------------------------------------------------
    // private
    //------------------------------------------------------------------------------------------------

    private void validateRequest(SubjectConditionRequest request){
        Long fromId = request.fromId;
        Long toId = request.toId;
        if (!subjectRepository.existsById(fromId))
            throw new RecordNotFoundException("Subject", fromId);
        if (!subjectRepository.existsById(toId))
            throw new RecordNotFoundException("Subject", toId);
        if ((long) fromId == toId)
            throw new IllegalArgumentException("From ID and To ID can not be same");

    }

    @AllArgsConstructor
    private static class SubjectConditionRequest implements RequestDTO {
        public Long fromId, toId;

        public SubjectCondition toEntity() {
            return Util.mapping(this, SubjectCondition.class);
        }
    }

    private static class ConditionGraph {
        public List<SubjectCondition> subjectConditionList;
        public Set<Long> subjectIds;

        public ConditionGraph(List<SubjectCondition> subjectConditionList) {
            this.subjectConditionList = subjectConditionList;
            Set<Long> subjectIdSet = new HashSet<>();
            subjectConditionList.forEach(condition ->{
                subjectIdSet.add(condition.fromId);
                subjectIdSet.add(condition.toId);
            });
            this.subjectIds = subjectIdSet;
        }
    }
}
