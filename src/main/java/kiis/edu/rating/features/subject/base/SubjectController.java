package kiis.edu.rating.features.subject.base;

import kiis.edu.rating.aop.AllowFeature;
import kiis.edu.rating.aop.AllowMethod;
import kiis.edu.rating.features.teacher.base.TeacherRepository;
import kiis.edu.rating.helper.Util;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

import static kiis.edu.rating.features.user.UserRole.Feature.SUBJECT;
import static kiis.edu.rating.features.user.UserRole.Method.*;

@SuppressWarnings("unused")
@RestController
@AllArgsConstructor
@RequestMapping(path = "/subject")
@AllowFeature(SUBJECT)
public class SubjectController {
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    @GetMapping("/{id}")
    public SubjectEntity getById(@PathVariable long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No subject with id : " + id));
    }

    @GetMapping("")
    public List<SubjectEntity> getAllEnable() {
        return subjectRepository.findAllByDisable(false);
    }

    @GetMapping("/all")
    @AllowMethod(GET_ALL)
    public List<SubjectEntity> getAll() {
        return subjectRepository.findAll();
    }

    @PostMapping("")
    @AllowMethod(CREATE)
    public void create(@RequestBody @Valid SubjectRequest request) {

        if (!teacherRepository.existsById(request.teacherId))
            throw new IllegalArgumentException("No teacher with id : " + request.teacherId);
        subjectRepository.save(request.toEntity());
    }

    @PutMapping("/{id}")
    @AllowMethod(UPDATE)
    public void update(@PathVariable long id, @RequestBody @Valid SubjectRequest request) {

        if (!subjectRepository.existsById(id))
            throw new IllegalArgumentException("No Subject with Id: " + id);
        SubjectEntity subjectEntity = request.toEntity();
        subjectEntity.id = id;
        subjectRepository.save(subjectEntity);
    }

    @DeleteMapping("/{id}")
    @AllowMethod(DELETE)
    public void delete(@PathVariable long id) {

        SubjectEntity subjectEntity = subjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No subject with id : " + id));
        subjectEntity.disable = true;
        subjectRepository.save(subjectEntity);
    }

    private static class SubjectRequest extends SubjectEntity {
        private long id;
        private Instant createdAt, updatedAt;
        private boolean disable;

        public SubjectEntity toEntity() {
            return Util.mapping(this, SubjectEntity.class);
        }
    }
}
