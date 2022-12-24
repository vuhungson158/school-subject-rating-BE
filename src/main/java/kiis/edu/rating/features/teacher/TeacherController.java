package kiis.edu.rating.features.teacher;

import kiis.edu.rating.features.common.RequestDTO;
import kiis.edu.rating.features.common.enums.Gender;
import kiis.edu.rating.features.teacher.rating.TeacherRatingEntity;
import kiis.edu.rating.features.teacher.rating.TeacherRatingRepository;
import kiis.edu.rating.features.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static kiis.edu.rating.helper.Constant.PATH;

@SuppressWarnings("unused")
@RestController
@AllArgsConstructor
@RequestMapping(path = PATH + "/teacher")
public class TeacherController {
    private final String RATING_PATH = "/rating";
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final TeacherRatingRepository teacherRatingRepository;

    @GetMapping("/{id}")
    public TeacherEntityWithRating getById(@PathVariable long id) {
        if (!teacherRepository.existsById(id))
            throw new IllegalArgumentException("No teacher with id : " + id);
        return teacherRepository.findTeacherEntityWithRatingById(id);
    }

    @GetMapping("")
    public List<TeacherEntity> getAll() {
        return teacherRepository.findAllByDisable(false);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('SUBJECT_CREATE')")
    public void create(@RequestBody TeacherRequest request) {
        teacherRepository.save(request.toEntity());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TEACHER_UPDATE')")
    public void update(@PathVariable long id, @RequestBody TeacherRequest request) {
        if (!teacherRepository.existsById(id))
            throw new IllegalArgumentException("No Teacher with ID:" + id);
        TeacherEntity teacherEntity = request.toEntity();
        teacherEntity.id = id;
        teacherRepository.save(teacherEntity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TEACHER_DELETE')")
    public void delete(@PathVariable long id) {
        Optional<TeacherEntity> optionalTeacher = teacherRepository.findById(id);
        if (!optionalTeacher.isPresent()) throw new IllegalArgumentException("No Teacher with Id: " + id);
        TeacherEntity teacherEntity = optionalTeacher.get();
        teacherEntity.disable = true;
        teacherRepository.save(teacherEntity);
    }

    @GetMapping(RATING_PATH + "/{id}")
    public TeacherRatingEntity getOneRatingById(@PathVariable long id) {
        Optional<TeacherRatingEntity> optionalRating = teacherRatingRepository.findById(id);
        if (!optionalRating.isPresent())
            throw new IllegalArgumentException("No rating with id : " + id);
        return optionalRating.get();
    }

    @GetMapping(RATING_PATH + "/teacherId/{id}")
    public List<TeacherRatingEntity> getRatingsByTeacherId(@PathVariable("id") long teacherId) {
        return teacherRatingRepository.findAllByTeacherId(teacherId);
    }

    @GetMapping(RATING_PATH + "/userId/{id}")
    public List<TeacherRatingEntity> getRatingsByUserId(@PathVariable("id") long userId) {
        return teacherRatingRepository.findAllByUserId(userId);
    }

//    @GetMapping(RATING_PATH + "/userId/average/{id}")
//    public AverageScore getRatingsAverageByUserId(@PathVariable("id") long userId) {
//        List<TeacherRatingEntity> allRating = teacherRatingRepository.findAllByUserId(userId);
//        return getAverageScore(allRating);
//    }

    @PostMapping(RATING_PATH + "")
    @PreAuthorize("hasAuthority('TEACHER_RATING_CREATE')")
    public void createRating(@RequestBody @Valid TeacherRatingRequest request) {
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

    @PutMapping(RATING_PATH + "/{id}")
    @PreAuthorize("hasAuthority('TEACHER_RATING_UPDATE')")
    public void updateRating(@PathVariable long id, @RequestBody @Valid TeacherRatingRequest request) {
        if (!teacherRatingRepository.existsById(id))
            throw new IllegalArgumentException("No Rating with Id: " + id);
        TeacherRatingEntity ratingEntity = request.toEntity();
        ratingEntity.id = id;
        teacherRatingRepository.save(ratingEntity);
    }

    @DeleteMapping(RATING_PATH + "/{id}")
    @PreAuthorize("hasAuthority('TEACHER_RATING_DELETE')")
    public void deleteRating(@PathVariable long id) {
        teacherRatingRepository.deleteById(id);
    }

    @AllArgsConstructor
    private static class TeacherRequest implements RequestDTO {
        public String name, nationality;
        public Gender gender;
        public Instant dob;

        @Override
        public TeacherEntity toEntity() {
            return new TeacherEntity(name, nationality, gender, dob, false);
        }
    }

    @AllArgsConstructor
    private static class TeacherRatingRequest implements RequestDTO {
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

        public TeacherRatingEntity toEntity() {
            return new TeacherRatingEntity(userId, teacherId, enthusiasm, friendly, nonConservatism, erudition, pedagogicalLevel);
        }
    }
}
