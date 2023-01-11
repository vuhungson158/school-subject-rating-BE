package kiis.edu.rating.features.subject.comment;

import kiis.edu.rating.features.common.RequestDTO;
import kiis.edu.rating.features.subject.base.SubjectRepository;
import kiis.edu.rating.features.user.UserRepository;
import kiis.edu.rating.helper.Util;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
        return subjectCommentWithLikeCountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Comment with Id: " + id));
    }

    @GetMapping("/{id}/top-comment/{limit}/{page}/")
    public List<SubjectCommentWithLikeCount> getTopRatingById(@PathVariable long id, @PathVariable int limit, @PathVariable int page) {
        return subjectCommentWithLikeCountRepository
                .findTopRatingComment(limit, page, id);
    }

    @GetMapping("")
    public List<SubjectCommentEntity> getAll() {
        return subjectCommentRepository.findAllByDisable(false);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('COMMENT_CREATE')")
    public void create(@RequestBody @Valid Request request) {
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
    @PreAuthorize("hasAuthority('COMMENT_UPDATE')")
    public void update(@PathVariable long id, @RequestBody @Valid Request request) {
        if (!subjectCommentRepository.existsById(id))
            throw new IllegalArgumentException("No comment with id : " + id);
        SubjectCommentEntity commentEntity = request.toEntity();
        commentEntity.id = id;
        subjectCommentRepository.save(commentEntity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('COMMENT_DELETE')")
    public void delete(@PathVariable long id) {
        subjectCommentRepository.deleteById(id);
    }

    @AllArgsConstructor
    private static class Request implements RequestDTO {
        public long userId, subjectId;
        public String comment;

        @Override
        public SubjectCommentEntity toEntity() {
            return Util.mapping(this, SubjectCommentEntity.class);
        }
    }
}
