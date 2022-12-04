package kiis.edu.rating.features.teacher;

import kiis.edu.rating.features.subject.SubjectController;
import kiis.edu.rating.features.subject.SubjectEntity;
import kiis.edu.rating.helper.Util;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static kiis.edu.rating.helper.Constant.PATH;

@RestController
@AllArgsConstructor
@RequestMapping(path = PATH + "/teacher")
public class TeacherController {
    private final TeacherRepository teacherRepository;

    @GetMapping("/{id}")
    public TeacherEntity getById(@PathVariable long id) {
        Optional<TeacherEntity> optionalTeacher = teacherRepository.findById(id);
        if (!optionalTeacher.isPresent())
            throw new IllegalArgumentException("No teacher with id : " + id);
        return optionalTeacher.get();
    }

    @GetMapping("")
    public List<TeacherEntity> getAll() {
        return teacherRepository.findAll();
    }

    @PostMapping("")
    public boolean create(@RequestBody TeacherEntity teacherEntity) {
        Util.makeSureBaseEntityEmpty(teacherEntity.id, teacherEntity.createdAt, teacherEntity.updatedAt);
        teacherRepository.save(teacherEntity);
        return true;
    }

    @PutMapping("/{id}")
    public boolean update(@PathVariable long id, @RequestBody TeacherEntity teacherEntity) {
        if (!teacherRepository.findById(id).isPresent())
            throw new IllegalArgumentException("Teacher not exist");
        Util.makeSureBaseEntityEmpty(teacherEntity.id, teacherEntity.createdAt, teacherEntity.updatedAt);
        teacherEntity.id = id;
        teacherRepository.save(teacherEntity);
        return true;
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable long id) {
        teacherRepository.deleteById(id);
        return true;
    }
}
