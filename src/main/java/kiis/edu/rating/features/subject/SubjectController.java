package kiis.edu.rating.features.subject;

import kiis.edu.rating.features.common.RequestDTO;
import kiis.edu.rating.features.common.enums.Specialize;
import kiis.edu.rating.features.subject.rating.SubjectRatingEntity;
import kiis.edu.rating.features.subject.rating.SubjectRatingRepository;
import kiis.edu.rating.features.teacher.TeacherRepository;
import kiis.edu.rating.features.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

import static kiis.edu.rating.helper.Constant.PATH;

@SuppressWarnings("unused")
@RestController
@AllArgsConstructor
@RequestMapping(path = PATH + "/subject")
public class SubjectController {
    private final String RATING_PATH = "/rating";
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRatingRepository subjectRatingRepository;
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public SubjectWithAverageScore getById(@PathVariable long id) {
        Optional<SubjectEntity> optionalSubject = subjectRepository.findById(id);
        if (!optionalSubject.isPresent())
            throw new IllegalArgumentException("No subject with id : " + id);
        List<SubjectRatingEntity> allRating = getRatingsBySubjectId(id);
        return new SubjectWithAverageScore(optionalSubject.get(), getAverageScore(allRating));
    }

    @GetMapping("")
    public List<SubjectEntity> getAll() {
        return subjectRepository.findAllByDisable(false);
    }

    @PostMapping("")
    public void create(@RequestBody @Valid SubjectRequest request) {
        if (!teacherRepository.existsById(request.teacherId))
            throw new IllegalArgumentException("No teacher with id : " + request.teacherId);
        subjectRepository.save(request.toEntity());
    }

    @PutMapping("/{id}")
    public void update(@PathVariable long id, @RequestBody @Valid SubjectRequest request) {
        if (!subjectRepository.existsById(id))
            throw new IllegalArgumentException("No Subject with Id: " + id);
        SubjectEntity subjectEntity = request.toEntity();
        subjectEntity.id = id;
        subjectRepository.save(subjectEntity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        Optional<SubjectEntity> optionalSubject = subjectRepository.findById(id);
        if (!optionalSubject.isPresent()) throw new IllegalArgumentException("No Subject with Id: " + id);
        SubjectEntity subjectEntity = optionalSubject.get();
        subjectEntity.disable = true;
        subjectRepository.save(subjectEntity);
    }

    @GetMapping(RATING_PATH + "/{id}")
    public SubjectRatingEntity getOneRatingById(@PathVariable long id) {
        Optional<SubjectRatingEntity> optionalRating = subjectRatingRepository.findById(id);
        if (!optionalRating.isPresent())
            throw new IllegalArgumentException("No rating with id : " + id);
        return optionalRating.get();
    }

    @GetMapping(RATING_PATH + "/subjectId/{id}")
    public List<SubjectRatingEntity> getRatingsBySubjectId(@PathVariable("id") long subjectId) {
        if (!subjectRepository.existsById(subjectId))
            throw new IllegalArgumentException("No subject with id : " + subjectId);
        return subjectRatingRepository.findAllBySubjectId(subjectId);
    }

    @GetMapping(RATING_PATH + "/userId/{id}")
    public List<SubjectRatingEntity> getRatingsByUserId(@PathVariable("id") long userId) {
        return subjectRatingRepository.findAllByUserId(userId);
    }

    @GetMapping(RATING_PATH + "/userId/average/{id}")
    public AverageScore getRatingsAverageByUserId(@PathVariable("id") long userId) {
        List<SubjectRatingEntity> allRating = subjectRatingRepository.findAllByUserId(userId);
        return getAverageScore(allRating);
    }

    @PostMapping(RATING_PATH + "")
    public void createRating(@RequestBody @Valid RatingRequest request) {
        long subjectId = request.subjectId;
        long userId = request.userId;
        if (!subjectRepository.existsById(subjectId))
            throw new IllegalArgumentException("No Subject with id : " + subjectId);
        if (!userRepository.existsById(userId))
            throw new IllegalArgumentException("No User with id : " + userId);
        if (subjectRatingRepository.findBySubjectIdAndUserId(subjectId, userId).isPresent())
            throw new IllegalArgumentException("This User has already rated this subject");
        subjectRatingRepository.save(request.toEntity());
    }

    @PutMapping(RATING_PATH + "/{id}")
    public void updateRating(@PathVariable long id, @RequestBody @Valid RatingRequest request) {
        if (!subjectRatingRepository.existsById(id))
            throw new IllegalArgumentException("No Rating with Id: " + id);
        SubjectRatingEntity subjectRatingEntity = request.toEntity();
        subjectRatingEntity.id = id;
        subjectRatingRepository.save(subjectRatingEntity);
    }

    @DeleteMapping(RATING_PATH + "/{id}")
    public void deleteRating(@PathVariable long id) {
        subjectRatingRepository.deleteById(id);
    }

    private AverageScore getAverageScore(List<SubjectRatingEntity> allRating) {
        int practicality = 0, difficult = 0, homework = 0, testDifficult = 0, teacherPedagogical = 0;
        for (SubjectRatingEntity rating : allRating) {
            practicality += rating.practicality;
            difficult += rating.difficult;
            homework += rating.homework;
            testDifficult += rating.testDifficult;
            teacherPedagogical += rating.teacherPedagogical;
        }
        int listSize = allRating.size();
        practicality = practicality / listSize;
        difficult = difficult / listSize;
        homework = homework / listSize;
        testDifficult = testDifficult / listSize;
        teacherPedagogical = teacherPedagogical / listSize;
        return new AverageScore(practicality, difficult, homework, testDifficult, teacherPedagogical, listSize);
    }

    @AllArgsConstructor
    private static class AverageScore {
        public double practicality, difficult, homework, testDifficult, teacherPedagogical, size;
    }

    @AllArgsConstructor
    private static class SubjectWithAverageScore {
        public SubjectEntity subject;
        public AverageScore averageScore;
    }

    @AllArgsConstructor
    private static class SubjectRequest implements RequestDTO {
        public long teacherId;
        @Min(value = 1, message = "Min = 1")
        @Max(value = 6, message = "Max = 6")
        public int unit;
        @Min(value = 1, message = "Min = 1")
        @Max(value = 4, message = "Max = 4")
        public int formYear;
        public String name;
        public Specialize specialize;
        public boolean disable;

        public SubjectEntity toEntity() {
            return new SubjectEntity(teacherId, unit, formYear, name, specialize, disable);
        }
    }

    @AllArgsConstructor
    private static class RatingRequest implements RequestDTO {
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
            return new SubjectRatingEntity(userId, subjectId, practicality, difficult, homework, testDifficult, teacherPedagogical);
        }
    }
}
