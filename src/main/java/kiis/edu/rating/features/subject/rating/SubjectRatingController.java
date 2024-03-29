package kiis.edu.rating.features.subject.rating;

import kiis.edu.rating.features.subject.base.SubjectRepository;
import kiis.edu.rating.features.user.UserRepository;
import kiis.edu.rating.features.user.UserRole;
import kiis.edu.rating.features.user.UserRole.SubjectRating;
import kiis.edu.rating.helper.Util;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
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
    public void create(@RequestBody @Valid SubjectRatingRequest request) {
        UserRole.requirePermission(SubjectRating.CREATE);

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
    public void update(@PathVariable long id, @RequestBody @Valid SubjectRatingRequest request) {
        UserRole.requirePermission(SubjectRating.UPDATE);

        if (!subjectRatingRepository.existsById(id))
            throw new IllegalArgumentException("No Rating with Id: " + id);
        SubjectRatingEntity subjectRatingEntity = request.toEntity();
        subjectRatingEntity.id = id;
        subjectRatingRepository.save(subjectRatingEntity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        UserRole.requirePermission(SubjectRating.DELETE);

        subjectRatingRepository.deleteById(id);
    }

    private static class SubjectRatingRequest extends SubjectRatingEntity {
        private long id;
        private Instant createdAt, updatedAt;
        private boolean disable;

        public SubjectRatingEntity toEntity() {
            return Util.mapping(this, SubjectRatingEntity.class);
        }
    }
}
