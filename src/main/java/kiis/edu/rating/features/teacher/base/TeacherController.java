package kiis.edu.rating.features.teacher.base;

import kiis.edu.rating.features.subject.base.SubjectRepository;
import kiis.edu.rating.features.user.UserRepository;
import kiis.edu.rating.features.user.UserRole;
import kiis.edu.rating.features.user.UserRole.Teacher;
import kiis.edu.rating.helper.Util;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
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
    public List<TeacherEntity> getAllEnable() {
        return teacherRepository.findAllByDisable(false);
    }

    @GetMapping("/all")
    public List<TeacherEntity> getAll() {
        UserRole.requirePermission(Teacher.GET_ALL);

        return teacherRepository.findAll();
    }

    @PostMapping("")
    public void create(@RequestBody TeacherRequest request) {
        UserRole.requirePermission(Teacher.CREATE);

        teacherRepository.save(request.toEntity());
    }

    @PutMapping("/{id}")
    public void update(@PathVariable long id, @RequestBody TeacherRequest request) {
        UserRole.requirePermission(Teacher.UPDATE);

        if (!teacherRepository.existsById(id))
            throw new IllegalArgumentException("No Teacher with ID:" + id);
        TeacherEntity teacherEntity = request.toEntity();
        teacherEntity.id = id;
        teacherRepository.save(teacherEntity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        UserRole.requirePermission(Teacher.DELETE);

        TeacherEntity teacherEntity = teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Teacher with Id: " + id));
        subjectRepository.updateDisableSubjectByTeacherId(id, true);
        teacherEntity.disable = true;
        teacherRepository.save(teacherEntity);
    }

    @AllArgsConstructor
    private static class TeacherRequest extends TeacherEntity {
        private long id;
        private Instant createdAt, updatedAt;
        private boolean disable;

        public TeacherEntity toEntity() {
            return Util.mapping(this, TeacherEntity.class);
        }
    }
}
