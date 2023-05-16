package kiis.edu.rating.features.subject.commentReact;

import kiis.edu.rating.features.common.RequestDTO;
import kiis.edu.rating.features.subject.comment.SubjectCommentRepository;
import kiis.edu.rating.features.user.UserRepository;
import kiis.edu.rating.features.user.UserRole;
import kiis.edu.rating.features.user.UserRole.SubjectCommentReact;
import kiis.edu.rating.helper.Util;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("unused")
@RestController
@AllArgsConstructor
@RequestMapping(path = "/subject-comment-react")
public class SubjectCommentReactController {
    private final UserRepository userRepository;
    private final SubjectCommentRepository subjectCommentRepository;
    private final SubjectCommentReactRepository subjectCommentReactRepository;

    @GetMapping("/{id}")
    public SubjectCommentReactEntity getById(@PathVariable long id) {
        return subjectCommentReactRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Comment React with Id: " + id));
    }

    @GetMapping("/commentId/{id}")
    public List<SubjectCommentReactEntity> getByCommentId(@PathVariable("id") long commentId) {
        if (!subjectCommentRepository.existsById(commentId))
            throw new IllegalArgumentException("No Comment with Id: " + commentId);
        return subjectCommentReactRepository.findByCommentId(commentId);
    }
    @GetMapping("/my")
    public List<SubjectCommentReactEntity> getByUserIdAndCommentIdList(
            @RequestParam long userId, @RequestParam List<Long> idList) {
        if (!userRepository.existsById(userId))
            throw new IllegalArgumentException("No User with Id: " + userId);
        return subjectCommentReactRepository.findByUserIdAndCommentIdList(userId, idList);
    }

    @PostMapping("")
    public void create(@RequestBody SubjectCommentReactRequest request) {
        UserRole.requirePermission(SubjectCommentReact.CREATE);

        if (!subjectCommentRepository.existsById(request.commentId))
            throw new IllegalArgumentException("No Comment with Id: " + request.commentId);
        if (!userRepository.existsById(request.userId))
            throw new IllegalArgumentException("No User with Id: " + request.userId);
        if (subjectCommentReactRepository.existsByUserIdAndCommentId(request.userId, request.commentId))
            throw new IllegalArgumentException("This User has already react this comment ");
        subjectCommentReactRepository.save(request.toEntity());
    }

    @PutMapping("/{id}")
    public void update(@PathVariable long id, @RequestBody SubjectCommentReactRequest request) {
        UserRole.requirePermission(SubjectCommentReact.UPDATE);

        if (!subjectCommentReactRepository.existsById(id))
            throw new IllegalArgumentException("No comment React with id : " + id);
        SubjectCommentReactEntity subjectCommentReactEntity = request.toEntity();
        subjectCommentReactEntity.id = id;
        subjectCommentReactRepository.save(subjectCommentReactEntity);
    }

    @DeleteMapping("/{id}")
    public void deleteRating(@PathVariable long id) {
        UserRole.requirePermission(SubjectCommentReact.DELETE);

        subjectCommentReactRepository.deleteById(id);
    }

    @AllArgsConstructor
    private static class SubjectCommentReactRequest implements RequestDTO {
        public long userId, commentId;
        public boolean react;

        public SubjectCommentReactEntity toEntity() {
            return Util.mapping(this, SubjectCommentReactEntity.class);
        }
    }
}
