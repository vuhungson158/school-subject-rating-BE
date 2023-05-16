package kiis.edu.rating.features.subject.comment;

import kiis.edu.rating.features.common.RequestDTO;
import kiis.edu.rating.features.subject.base.SubjectRepository;
import kiis.edu.rating.features.user.UserRepository;
import kiis.edu.rating.features.user.UserRole;
import kiis.edu.rating.features.user.UserRole.SubjectComment;
import kiis.edu.rating.helper.Util;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@SuppressWarnings("unused")
@RestController
@AllArgsConstructor
@RequestMapping(path = "/subject-comment")
public class SubjectCommentController {
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final SubjectCommentRepository subjectCommentRepository;
    private final SubjectCommentWithLikeCountRepository subjectCommentWithLikeCountRepository;

    @GetMapping("/{id}")
    public SubjectCommentWithLikeCount getById(@PathVariable long id) {
        UserRole.requirePermission(SubjectComment.GET_BY_ID);

        return subjectCommentWithLikeCountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Comment with Id: " + id));
    }

    @GetMapping("")
    public List<SubjectCommentEntity> getAllEnable() {
        UserRole.requirePermission(SubjectComment.GET_ALL);

        return subjectCommentRepository.findAllByDisable(false);
    }

    @GetMapping("/top-comment")
    public ListWithTotal getTopBySubjectId
            (@RequestParam long subjectId, @RequestParam int limit, @RequestParam int page) {
        return new ListWithTotal(
                subjectCommentWithLikeCountRepository.countCommentBySubjectId(subjectId),
                subjectCommentWithLikeCountRepository.findTopBySubjectId(limit, page, subjectId));
    }

    @GetMapping(value = "/my")
    public SubjectCommentWithLikeCount getBySubjectIdAndUserId
            (@RequestParam long subjectId, @RequestParam long userId) {
        return subjectCommentWithLikeCountRepository
                .findBySubjectIdAndUserId(subjectId, userId);
    }

    @PostMapping("")
    public void create(@RequestBody @Valid SubjectCommentRequest request) {
        UserRole.requirePermission(SubjectComment.CREATE);

        long userId = request.userId;
        long subjectId = request.subjectId;

        if (!userRepository.existsById(userId))
            throw new IllegalArgumentException("No User with id : " + userId);
        if (!subjectRepository.existsById(subjectId))
            throw new IllegalArgumentException("No subject with id : " + subjectId);
        if (subjectCommentRepository.existsBySubjectIdAndUserId(subjectId, userId))
            throw new IllegalArgumentException("This User has already commented this " + subjectId);

        subjectCommentRepository.save(request.toEntity());
    }

    @PutMapping("/{id}")
    public void update(@PathVariable long id, @RequestBody @Valid SubjectCommentRequest request) {
        UserRole.requirePermission(SubjectComment.UPDATE);

        if (!subjectCommentRepository.existsById(id))
            throw new IllegalArgumentException("No comment with id : " + id);
        SubjectCommentEntity commentEntity = request.toEntity();
        commentEntity.id = id;
        subjectCommentRepository.save(commentEntity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        UserRole.requirePermission(SubjectComment.DELETE);

        subjectCommentRepository.deleteById(id);
    }

    @AllArgsConstructor
    private static class SubjectCommentRequest implements RequestDTO {
        public long userId, subjectId;
        public String comment;

        @Override
        public SubjectCommentEntity toEntity() {
            return Util.mapping(this, SubjectCommentEntity.class);
        }
    }

    @AllArgsConstructor
    private static class ListWithTotal {
        public int total;
        public List<SubjectCommentWithLikeCount> list;
    }
}
