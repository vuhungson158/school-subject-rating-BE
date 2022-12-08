package kiis.edu.rating.features.teacher;

import kiis.edu.rating.features.common.RequestDTO;
import kiis.edu.rating.features.common.enums.Gender;
import kiis.edu.rating.features.teacher.rating.TeacherRatingEntity;
import kiis.edu.rating.features.teacher.rating.TeacherRatingRepository;
import kiis.edu.rating.features.user.UserRepository;
import lombok.AllArgsConstructor;
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
    public TeacherWithAverageScore getById(@PathVariable long id) {
        Optional<TeacherEntity> optionalTeacher = teacherRepository.findById(id);
        if (!optionalTeacher.isPresent())
            throw new IllegalArgumentException("No teacher with id : " + id);
        List<TeacherRatingEntity> allRating = getRatingsByTeacherId(id);
        return new TeacherWithAverageScore(optionalTeacher.get(), getAverageScore(allRating));
    }

    @GetMapping("")
    public List<TeacherEntity> getAll() {
        return teacherRepository.findAllByDisable(false);
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

    @GetMapping(RATING_PATH + "/userId/average/{id}")
    public AverageScore getRatingsAverageByUserId(@PathVariable("id") long userId) {
        List<TeacherRatingEntity> allRating = teacherRatingRepository.findAllByUserId(userId);
        return getAverageScore(allRating);
    }

    @PostMapping(RATING_PATH + "")
    public void createRating(@RequestBody @Valid RatingRequest request) {
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
    public void updateRating(@PathVariable long id, @RequestBody @Valid RatingRequest request) {
        if (!teacherRatingRepository.existsById(id))
            throw new IllegalArgumentException("No Rating with Id: " + id);
        TeacherRatingEntity ratingEntity = request.toEntity();
        ratingEntity.id = id;
        teacherRatingRepository.save(ratingEntity);
    }

    @DeleteMapping(RATING_PATH + "/{id}")
    public void deleteRating(@PathVariable long id) {
        teacherRatingRepository.deleteById(id);
    }

    private AverageScore getAverageScore(List<TeacherRatingEntity> allRating) {
        int enthusiasm = 0, friendly = 0, nonConservatism = 0, erudition = 0, pedagogicalLevel = 0;
        for (TeacherRatingEntity rating : allRating) {
            enthusiasm += rating.enthusiasm;
            friendly += rating.friendly;
            nonConservatism += rating.nonConservatism;
            erudition += rating.erudition;
            pedagogicalLevel += rating.pedagogicalLevel;
        }
        int listSize = allRating.size();
        enthusiasm = enthusiasm / listSize;
        friendly = friendly / listSize;
        nonConservatism = nonConservatism / listSize;
        erudition = erudition / listSize;
        pedagogicalLevel = pedagogicalLevel / listSize;
        return new AverageScore(enthusiasm, friendly, nonConservatism, erudition, pedagogicalLevel, listSize);
    }

    @AllArgsConstructor
    private static class AverageScore {
        public double enthusiasm, friendly, nonConservatism, erudition, pedagogicalLevel, size;
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
    private static class RatingRequest implements RequestDTO {
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

    @AllArgsConstructor
    private static class TeacherWithAverageScore {
        public TeacherEntity subject;
        public AverageScore averageScore;
    }

}
