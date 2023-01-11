package kiis.edu.rating.features.teacher.rating;

import kiis.edu.rating.features.common.RequestDTO;
import kiis.edu.rating.features.subject.rating.SubjectRatingEntity;
import kiis.edu.rating.features.teacher.base.TeacherRepository;
import kiis.edu.rating.features.user.UserRepository;
import kiis.edu.rating.helper.Util;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@SuppressWarnings("unused")
@RestController
@AllArgsConstructor
@RequestMapping(path = "/teacher-rating")
public class TeacherRatingController {
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final TeacherRatingRepository teacherRatingRepository;
    private final TeacherRatingAverageRepository teacherRatingAverageRepository;

    @GetMapping("/{id}")
    public TeacherRatingEntity findById(@PathVariable long id) {
        return teacherRatingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Rating with Id: " + id));
    }

    @GetMapping("/teacherId/{id}")
    public List<TeacherRatingEntity> findAllByTeacherId(@PathVariable("id") long teacherId) {
        return teacherRatingRepository.findAllByTeacherId(teacherId);
    }

    @GetMapping("/userId/{id}")
    public List<TeacherRatingEntity> findAllByUserId(@PathVariable("id") long userId) {
        return teacherRatingRepository.findAllByUserId(userId);
    }

    @GetMapping("/userId/average/{id}")
    public TeacherRatingAverage getAverageByUserId(@PathVariable("id") long userId) {
        return teacherRatingAverageRepository.findTeacherRatingAverageByTeacherId(userId);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('TEACHER_RATING_CREATE')")
    public void create(@RequestBody @Valid Request request) {
        long teacherId = request.teacherId;
        long userId = request.userId;

        if (!teacherRepository.existsById(teacherId))
            throw new IllegalArgumentException("No Teacher with id : " + teacherId);
        if (!userRepository.existsById(userId))
            throw new IllegalArgumentException("No User with id : " + userId);
        if (teacherRatingRepository.existsByTeacherIdAndUserId(teacherId, userId).isPresent())
            throw new IllegalArgumentException("This User has already rated this Teacher");
        teacherRatingRepository.save(request.toEntity());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TEACHER_RATING_UPDATE')")
    public void update(@PathVariable long id, @RequestBody @Valid Request request) {
        if (!teacherRatingRepository.existsById(id))
            throw new IllegalArgumentException("No Rating with Id: " + id);
        TeacherRatingEntity ratingEntity = request.toEntity();
        ratingEntity.id = id;
        teacherRatingRepository.save(ratingEntity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TEACHER_RATING_DELETE')")
    public void delete(@PathVariable long id) {
        teacherRatingRepository.deleteById(id);
    }

    @AllArgsConstructor
    private static class Request implements RequestDTO {
        public long userId, teacherId;
        @Min(value = 0, message = "Min = 0")
        @Max(value = 100, message = "Max = 100")
        public int enthusiasm;
        @Min(value = 0, message = "Min = 0")
        @Max(value = 100, message = "Max = 100")
        public int friendly;
        @Min(value = 0, message = "Min = 0")
        @Max(value = 100, message = "Max = 100")
        public int nonConservatism;
        @Min(value = 0, message = "Min = 0")
        @Max(value = 100, message = "Max = 100")
        public int erudition;
        @Min(value = 0, message = "Min = 0")
        @Max(value = 100, message = "Max = 100")
        public int pedagogicalLevel;

        @Override
        public TeacherRatingEntity toEntity() {
            return Util.mapping(this, TeacherRatingEntity.class);
        }
    }


    @AllArgsConstructor
    private static class SubjectRatingRequest implements RequestDTO {
        public long userId, subjectId;
        @Min(value = 0, message = "Min = 0")
        @Max(value = 100, message = "Max = 100")
        public int practicality;
        @Min(value = 0, message = "Min = 0")
        @Max(value = 100, message = "Max = 100")
        public int difficult;
        @Min(value = 0, message = "Min = 0")
        @Max(value = 100, message = "Max = 100")
        public int homework;
        @Min(value = 0, message = "Min = 0")
        @Max(value = 100, message = "Max = 100")
        public int testDifficult;
        @Min(value = 0, message = "Min = 0")
        @Max(value = 100, message = "Max = 100")
        public int teacherPedagogical;

        public SubjectRatingEntity toEntity() {
            return Util.mapping(this, SubjectRatingEntity.class);
        }
    }
}
