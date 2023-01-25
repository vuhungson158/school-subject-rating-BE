package kiis.edu.rating.features.teacher.base;

import kiis.edu.rating.features.common.RequestDTO;
import kiis.edu.rating.features.common.enums.Gender;
import kiis.edu.rating.features.subject.base.SubjectRepository;
import kiis.edu.rating.features.user.UserRepository;
import kiis.edu.rating.helper.Util;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@SuppressWarnings("unused")
@RestController
@AllArgsConstructor
@RequestMapping(path = "/teacher")
public class TeacherController {
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public TeacherEntity getById(@PathVariable long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Teacher with Id: " + id));
    }

    @GetMapping("")
    public List<TeacherEntity> getAll() {
        return teacherRepository.findAllByDisable(false);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('SUBJECT_CREATE')")
    public void create(@RequestBody Request request) {
        teacherRepository.save(request.toEntity());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TEACHER_UPDATE')")
    public void update(@PathVariable long id, @RequestBody Request request) {
        if (!teacherRepository.existsById(id))
            throw new IllegalArgumentException("No Teacher with ID:" + id);
        TeacherEntity teacherEntity = request.toEntity();
        teacherEntity.id = id;
        teacherRepository.save(teacherEntity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TEACHER_DELETE')")
    public void delete(@PathVariable long id) {
        TeacherEntity teacherEntity = teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Teacher with Id: " + id));
        subjectRepository.updateDisableSubjectByTeacherId(id, true);
        teacherEntity.disable = true;
        teacherRepository.save(teacherEntity);
    }

    @AllArgsConstructor
    private static class Request implements RequestDTO {
        public String name, nationality;
        public Gender gender;
//        @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
        public Date dob;

        @Override
        public TeacherEntity toEntity() {
            return Util.mapping(this, TeacherEntity.class);
        }
    }
}
