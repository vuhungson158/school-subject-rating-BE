package kiis.edu.rating.features.subject;

import kiis.edu.rating.features.subject.rating.RatingEntity;
import kiis.edu.rating.features.subject.rating.RatingRepository;
import kiis.edu.rating.features.teacher.TeacherRepository;
import kiis.edu.rating.features.user.UserRepository;
import kiis.edu.rating.helper.Util;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static kiis.edu.rating.helper.Constant.PATH;

@RestController
@AllArgsConstructor
@RequestMapping(path = PATH + "/subject")
public class SubjectController {
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public SubjectEntity getById(@PathVariable long id) {
        Optional<SubjectEntity> optionalSubject = subjectRepository.findById(id);
        if (!optionalSubject.isPresent())
            throw new IllegalArgumentException("No subject with id : " + id);
        SubjectWithAverageScore subjectEntity = (SubjectWithAverageScore) optionalSubject.get();
        subjectEntity.averageScore = getAverageScore(id);
        return subjectEntity;
    }

    @GetMapping("")
    public List<SubjectEntity> getAll() {
        return subjectRepository.findAll();
    }

    @PostMapping("")
    public boolean create(@RequestBody @Valid SubjectEntity subjectEntity) {
        if (!teacherRepository.findById(subjectEntity.teacherId).isPresent())
            throw new IllegalArgumentException("No teacher with id : " + subjectEntity.teacherId);
        Util.makeSureBaseEntityEmpty(subjectEntity.id, subjectEntity.createdAt, subjectEntity.updatedAt);
        subjectRepository.save(subjectEntity);
        return true;
    }

    @PutMapping("/{id}")
    public boolean update(@PathVariable long id, @RequestBody @Valid SubjectEntity subjectEntity) {
        if (!subjectRepository.findById(id).isPresent())
            throw new IllegalArgumentException("No Subject with Id: " + id);
        Util.makeSureBaseEntityEmpty(subjectEntity.id, subjectEntity.createdAt, subjectEntity.updatedAt);
        subjectEntity.id = id;
        subjectRepository.save(subjectEntity);
        return true;
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable long id) {
        ratingRepository.deleteBySubjectId(id);
        subjectRepository.deleteById(id);
        return true;
    }

    @GetMapping("/rating/{id}")
    public RatingEntity getOneRatingById(@PathVariable long id) {
        Optional<RatingEntity> optionalRating = ratingRepository.findById(id);
        if (!optionalRating.isPresent())
            throw new IllegalArgumentException("No rating with id : " + id);
        return optionalRating.get();
    }

    @GetMapping("/rating/subjectId/{id}")
    public List<RatingEntity> getRatingsBySubjectId(@PathVariable long subjectId) {
        return ratingRepository.findBySubjectId(subjectId);
    }

    @GetMapping("/rating/userId/{id}")
    public List<RatingEntity> getRatingsByUserId(@PathVariable long userId) {
        return ratingRepository.findByUserId(userId);
    }

    @PostMapping("/rating")
    public boolean createRating(@RequestBody @Valid RatingEntity ratingEntity) {
        if (!subjectRepository.findById(ratingEntity.subjectId).isPresent())
            throw new IllegalArgumentException("No Subject with id : " + ratingEntity.subjectId);
        if (!userRepository.findById(ratingEntity.userId).isPresent())
            throw new IllegalArgumentException("No User with id : " + ratingEntity.userId);
        Util.makeSureBaseEntityEmpty(ratingEntity.id, ratingEntity.createdAt, ratingEntity.updatedAt);
        ratingRepository.save(ratingEntity);
        return true;
    }

    @PutMapping("/rating/{id}")
    public boolean updateRating(@PathVariable long id, @RequestBody @Valid RatingEntity ratingEntity) {
        if (!subjectRepository.findById(id).isPresent())
            throw new IllegalArgumentException("No Rating with Id: " + id);
        Util.makeSureBaseEntityEmpty(ratingEntity.id, ratingEntity.createdAt, ratingEntity.updatedAt);
        ratingEntity.id = id;
        ratingRepository.save(ratingEntity);
        return true;
    }

    @DeleteMapping("/rating/{id}")
    public boolean deleteRating(@PathVariable long id) {
        ratingRepository.deleteById(id);
        return true;
    }

    private AverageScore getAverageScore(long subjectId) {
        int practicality = 0, difficult = 0, homework = 0, testDifficult = 0, teacherPedagogical = 0;
        List<RatingEntity> allRating = getRatingsBySubjectId(subjectId);
        for (RatingEntity rating : allRating) {
            practicality += rating.practicality;
            difficult += rating.difficult;
            homework += rating.homework;
            testDifficult += rating.testDifficult;
            teacherPedagogical += rating.teacherPedagogical;
        }
        int listSize = allRating.size();
        practicality = Math.floorDiv(practicality, listSize);
        difficult = Math.floorDiv(difficult, listSize);
        homework = Math.floorDiv(homework, listSize);
        testDifficult = Math.floorDiv(testDifficult, listSize);
        teacherPedagogical = Math.floorDiv(teacherPedagogical, listSize);
        return new AverageScore(practicality, difficult, homework, testDifficult, teacherPedagogical);
    }

    @AllArgsConstructor
    private static class AverageScore {
        public int practicality, difficult, homework, testDifficult, teacherPedagogical;
    }

    private static class SubjectWithAverageScore extends SubjectEntity {
        public AverageScore averageScore;
    }
}
