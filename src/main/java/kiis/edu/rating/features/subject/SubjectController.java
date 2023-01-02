package kiis.edu.rating.features.subject;

import kiis.edu.rating.features.common.RequestDTO;
import kiis.edu.rating.features.common.enums.Specialize;
import kiis.edu.rating.features.subject.rating.SubjectRatingEntity;
import kiis.edu.rating.features.subject.rating.SubjectRatingRepository;
import kiis.edu.rating.features.teacher.TeacherRepository;
import kiis.edu.rating.features.user.UserRepository;
import kiis.edu.rating.helper.Util;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final SubjectWithAvgRatingRepository subjectWithAvgRatingRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRatingRepository subjectRatingRepository;
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public SubjectWithAvgRating getById(@PathVariable long id) {
        if (!subjectRepository.existsById(id))
            throw new IllegalArgumentException("No subject with id : " + id);
        return subjectWithAvgRatingRepository.findSubjectEntityWithRatingById(id);
    }

    @GetMapping("")
    public List<SubjectEntity> getAll() {
        return subjectRepository.findAllByDisable(false);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('SUBJECT_CREATE')")
    public void create(@RequestBody @Valid SubjectRequest request) {
        if (!teacherRepository.existsById(request.teacherId))
            throw new IllegalArgumentException("No teacher with id : " + request.teacherId);
        subjectRepository.save(request.toEntity());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SUBJECT_UPDATE')")
    public void update(@PathVariable long id, @RequestBody @Valid SubjectRequest request) {
        if (!subjectRepository.existsById(id))
            throw new IllegalArgumentException("No Subject with Id: " + id);
        SubjectEntity subjectEntity = request.toEntity();
        subjectEntity.id = id;
        subjectRepository.save(subjectEntity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SUBJECT_DELETE')")
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

//    @GetMapping(RATING_PATH + "/userId/average/{id}")
//    public AverageScore getRatingsAverageByUserId(@PathVariable("id") long userId) {
//        List<SubjectRatingEntity> allRating = subjectRatingRepository.findAllByUserId(userId);
//        return new AverageScore(allRating);
//    }

    @PostMapping(RATING_PATH + "")
    @PreAuthorize("hasAuthority('SUBJECT_RATING_CREATE')")
    public void createRating(@RequestBody @Valid SubjectRatingRequest request) {
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
    @PreAuthorize("hasAuthority('SUBJECT_RATING_UPDATE')")
    public void updateRating(@PathVariable long id, @RequestBody @Valid SubjectRatingRequest request) {
        if (!subjectRatingRepository.existsById(id))
            throw new IllegalArgumentException("No Rating with Id: " + id);
        SubjectRatingEntity subjectRatingEntity = request.toEntity();
        subjectRatingEntity.id = id;
        subjectRatingRepository.save(subjectRatingEntity);
    }

    @DeleteMapping(RATING_PATH + "/{id}")
    @PreAuthorize("hasAuthority('SUBJECT_RATING_DELETE')")
    public void deleteRating(@PathVariable long id) {
        subjectRatingRepository.deleteById(id);
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

        public SubjectEntity toEntity() {
            return Util.mapping(this, SubjectEntity.class);
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
            return new SubjectRatingEntity(userId, subjectId, practicality, difficult, homework, testDifficult, teacherPedagogical);
        }
    }
}
