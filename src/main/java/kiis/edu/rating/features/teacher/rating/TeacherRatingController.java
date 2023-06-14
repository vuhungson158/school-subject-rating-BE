package kiis.edu.rating.features.teacher.rating;

import kiis.edu.rating.features.teacher.base.TeacherRepository;
import kiis.edu.rating.features.user.UserRepository;
import kiis.edu.rating.helper.Util;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

@SuppressWarnings({"unused", "ClassEscapesDefinedScope"})
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

    @GetMapping("/teacherId/{teacherId}/userId/{userId}")
    public TeacherRatingEntity getByTeacherIdAndUserId(
            @PathVariable("teacherId") long teacherId, @PathVariable("userId") long userId) {
        return teacherRatingRepository.findByTeacherIdAndUserId(teacherId, userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "This User hasn't rated this teacher yet"));
    }

    @GetMapping("/average/userId/{id}")
    public TeacherRatingAverage getAverageByUserId(@PathVariable("id") long userId) {
        return teacherRatingAverageRepository.findTeacherRatingAverageByUserId(userId);
    }

    @GetMapping("/average/teacherId/{id}")
    public TeacherRatingAverage getAverageByTeacherId(@PathVariable("id") long teacherId) {
        return teacherRatingAverageRepository.findTeacherRatingAverageByTeacherId(teacherId);
    }

    @PostMapping("")
    public void create(@RequestBody @Valid TeacherRatingController.TeacherRatingRequest request) {
        long teacherId = request.teacherId;
        long userId = request.userId;

        if (!teacherRepository.existsById(teacherId))
            throw new IllegalArgumentException("No Teacher with id : " + teacherId);
        if (!userRepository.existsById(userId))
            throw new IllegalArgumentException("No User with id : " + userId);
        if (teacherRatingRepository.existsByTeacherIdAndUserId(teacherId, userId))
            throw new IllegalArgumentException("This User has already rated this Teacher");
        teacherRatingRepository.save(request.toEntity());
    }

    @PutMapping("/{id}")
    public void update(@PathVariable long id, @RequestBody @Valid TeacherRatingController.TeacherRatingRequest request) {
        if (!teacherRatingRepository.existsById(id))
            throw new IllegalArgumentException("No Rating with Id: " + id);
        TeacherRatingEntity ratingEntity = request.toEntity();
        ratingEntity.id = id;
        teacherRatingRepository.save(ratingEntity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        teacherRatingRepository.deleteById(id);
    }

    private static class TeacherRatingRequest extends TeacherRatingEntity {
        private long id;
        private Instant createdAt, updatedAt;
        private boolean disable;

        public TeacherRatingEntity toEntity() {
            return Util.mapping(this, TeacherRatingEntity.class);
        }
    }
}
