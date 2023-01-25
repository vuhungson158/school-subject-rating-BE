package kiis.edu.rating.features.subject.rating;

import kiis.edu.rating.features.common.RequestDTO;
import kiis.edu.rating.features.subject.base.SubjectRepository;
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
@RequestMapping(path = "/subject-rating")
public class SubjectRatingController {
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final SubjectRatingRepository subjectRatingRepository;
    private final SubjectRatingAverageRepository subjectRatingAverageRepository;

    @GetMapping("/{id}")
    public SubjectRatingEntity getById(@PathVariable long id) {
        return subjectRatingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Rating with id : " + id));
    }

    @GetMapping("/subjectId/{id}")
    public List<SubjectRatingEntity> getAllBySubjectId(@PathVariable("id") long subjectId) {
        if (!subjectRepository.existsById(subjectId))
            throw new IllegalArgumentException("No subject with id : " + subjectId);
        return subjectRatingRepository.findAllBySubjectId(subjectId);
    }

    @GetMapping("/userId/{id}")
    public List<SubjectRatingEntity> getAllByUserId(@PathVariable("id") long userId) {
        return subjectRatingRepository.findAllByUserId(userId);
    }

    @GetMapping("/subjectId/{subjectId}/userId/{userId}")
    public SubjectRatingEntity getBySubjectIdAndUserId(
            @PathVariable("subjectId") long subjectId, @PathVariable("userId") long userId) {
        return subjectRatingRepository.findBySubjectIdAndUserId(subjectId, userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "This User hasn't rated this subject yet"));
    }

    @GetMapping("/average/userId/{id}")
    public SubjectRatingAverage getAverageByUserId(@PathVariable("id") long userId) {
        return subjectRatingAverageRepository.findSubjectRatingAverageByUserId(userId);
    }

    @GetMapping("/average/subjectId/{id}")
    public SubjectRatingAverage getAverageBySubjectId(@PathVariable("id") long subjectId) {
        return subjectRatingAverageRepository.findSubjectRatingAverageBySubjectId(subjectId);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('SUBJECT_RATING_CREATE')")
    public void create(@RequestBody @Valid Request request) {
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

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SUBJECT_RATING_UPDATE')")
    public void update(@PathVariable long id, @RequestBody @Valid Request request) {
        if (!subjectRatingRepository.existsById(id))
            throw new IllegalArgumentException("No Rating with Id: " + id);
        SubjectRatingEntity subjectRatingEntity = request.toEntity();
        subjectRatingEntity.id = id;
        subjectRatingRepository.save(subjectRatingEntity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SUBJECT_RATING_DELETE')")
    public void delete(@PathVariable long id) {
        subjectRatingRepository.deleteById(id);
    }

    @AllArgsConstructor
    private static class Request implements RequestDTO {
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
        @Min(value = 0, message = "Min = 0")
        @Max(value = 10, message = "Max = 10")
        public int star;

        public SubjectRatingEntity toEntity() {
            return Util.mapping(this, SubjectRatingEntity.class);
        }
    }
}
