package kiis.edu.rating.features.subject;

import kiis.edu.rating.features.subject.rating.SubjectRatingEntity;
import kiis.edu.rating.features.subject.rating.SubjectRatingRepository;
import kiis.edu.rating.features.teacher.TeacherRepository;
import kiis.edu.rating.features.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public boolean create(@RequestBody @Valid SubjectEntity subjectEntity) {
        if (!teacherRepository.findById(subjectEntity.teacherId).isPresent())
            throw new IllegalArgumentException("No teacher with id : " + subjectEntity.teacherId);
        subjectEntity.makeSureBaseEntityEmpty();
        subjectRepository.save(subjectEntity);
        return true;
    }

    @PutMapping("/{id}")
    public boolean update(@PathVariable long id, @RequestBody @Valid SubjectEntity subjectEntity) {
        if (!subjectRepository.findById(id).isPresent())
            throw new IllegalArgumentException("No Subject with Id: " + id);
        subjectEntity.makeSureBaseEntityEmpty();
        subjectEntity.id = id;
        subjectRepository.save(subjectEntity);
        return true;
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable long id) {
        Optional<SubjectEntity> optionalSubject = subjectRepository.findById(id);
        if (!optionalSubject.isPresent()) throw new IllegalArgumentException("No Subject with Id: " + id);
        SubjectEntity subjectEntity = optionalSubject.get();
        subjectEntity.disable = true;
        subjectRepository.save(subjectEntity);
        return true;
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
        if (!subjectRepository.findById(subjectId).isPresent())
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
    public boolean createRating(@RequestBody @Valid SubjectRatingEntity subjectRatingEntity) {
        if (!subjectRepository.findById(subjectRatingEntity.subjectId).isPresent())
            throw new IllegalArgumentException("No Subject with id : " + subjectRatingEntity.subjectId);
        if (!userRepository.findById(subjectRatingEntity.userId).isPresent())
            throw new IllegalArgumentException("No User with id : " + subjectRatingEntity.userId);
        if (subjectRatingRepository.findBySubjectIdAndUserId(subjectRatingEntity.subjectId, subjectRatingEntity.userId).isPresent())
            throw new IllegalArgumentException("This User has already rated this subject");
        subjectRatingEntity.makeSureBaseEntityEmpty();
        subjectRatingRepository.save(subjectRatingEntity);
        return true;
    }

    @PutMapping(RATING_PATH + "/{id}")
    public boolean updateRating(@PathVariable long id, @RequestBody @Valid SubjectRatingEntity subjectRatingEntity) {
        if (!subjectRatingRepository.findById(id).isPresent())
            throw new IllegalArgumentException("No Rating with Id: " + id);
        subjectRatingEntity.makeSureBaseEntityEmpty();
        subjectRatingEntity.id = id;
        subjectRatingRepository.save(subjectRatingEntity);
        return true;
    }

    @DeleteMapping(RATING_PATH + "/{id}")
    public boolean deleteRating(@PathVariable long id) {
        subjectRatingRepository.deleteById(id);
        return true;
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
        practicality = Math.floorDiv(practicality, listSize);
        difficult = Math.floorDiv(difficult, listSize);
        homework = Math.floorDiv(homework, listSize);
        testDifficult = Math.floorDiv(testDifficult, listSize);
        teacherPedagogical = Math.floorDiv(teacherPedagogical, listSize);
        return new AverageScore(practicality, difficult, homework, testDifficult, teacherPedagogical, listSize);
    }

    @AllArgsConstructor
    private static class AverageScore {
        public int practicality, difficult, homework, testDifficult, teacherPedagogical, size;
    }

    @AllArgsConstructor
    private static class SubjectWithAverageScore {
        public SubjectEntity subject;
        public AverageScore averageScore;
    }
}
