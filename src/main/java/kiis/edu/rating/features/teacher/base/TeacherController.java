package kiis.edu.rating.features.teacher.base;

import kiis.edu.rating.aop.AllowFeature;
import kiis.edu.rating.aop.AllowMethod;
import kiis.edu.rating.features.subject.base.SubjectRepository;
import kiis.edu.rating.features.user.UserRepository;
import kiis.edu.rating.helper.Util;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

import static kiis.edu.rating.features.user.UserRole.Feature.TEACHER;
import static kiis.edu.rating.features.user.UserRole.Method.GET_ALL;

@SuppressWarnings({"unused", "ClassEscapesDefinedScope"})
@RestController
@AllArgsConstructor
@RequestMapping(path = "/teacher")
@AllowFeature(TEACHER)
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
    @AllowMethod(GET_ALL)
    public List<TeacherEntity> getAll() {
        return teacherRepository.findAll();
    }

    @PostMapping("")
    public void create(@RequestBody TeacherRequest request) {
        teacherRepository.save(request.toEntity());
    }

    @PutMapping("/{id}")
    public void update(@PathVariable long id, @RequestBody TeacherRequest request) {
        if (!teacherRepository.existsById(id))
            throw new IllegalArgumentException("No Teacher with ID:" + id);
        TeacherEntity teacherEntity = request.toEntity();
        teacherEntity.id = id;
        teacherRepository.save(teacherEntity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
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
