package kiis.edu.rating.features.subject.base;

import kiis.edu.rating.features.common.RequestDTO;
import kiis.edu.rating.features.common.enums.Specialize;
import kiis.edu.rating.features.teacher.base.TeacherRepository;
import kiis.edu.rating.features.user.UserRepository;
import kiis.edu.rating.helper.Util;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@SuppressWarnings("unused")
@RestController
@AllArgsConstructor
@RequestMapping(path = "/subject")
public class SubjectController {
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    @GetMapping("/{id}")
    public SubjectEntity getById(@PathVariable long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No subject with id : " + id));
    }

    @GetMapping("")
    public List<SubjectEntity> getAll() {
        return subjectRepository.findAllByDisable(false);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('SUBJECT_CREATE')")
    public void create(@RequestBody @Valid Request request) {
        if (!teacherRepository.existsById(request.teacherId))
            throw new IllegalArgumentException("No teacher with id : " + request.teacherId);
        subjectRepository.save(request.toEntity());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SUBJECT_UPDATE')")
    public void update(@PathVariable long id, @RequestBody @Valid Request request) {
        if (!subjectRepository.existsById(id))
            throw new IllegalArgumentException("No Subject with Id: " + id);
        SubjectEntity subjectEntity = request.toEntity();
        subjectEntity.id = id;
        subjectRepository.save(subjectEntity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SUBJECT_DELETE')")
    public void delete(@PathVariable long id) {
        SubjectEntity subjectEntity = subjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No subject with id : " + id));
        subjectEntity.disable = true;
        subjectRepository.save(subjectEntity);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    private static class Request implements RequestDTO {
        public long teacherId;
        @Min(value = 1, message = "Min = 1")
        @Max(value = 6, message = "Max = 6")
        public int unit;
        @Min(value = 1, message = "Min = 1")
        @Max(value = 4, message = "Max = 4")
        public int formYear;
        public String name;
        public Specialize specialize;

        public SubjectEntity toEntity() {
            return Util.mapping(this, SubjectEntity.class);
        }
    }
}
