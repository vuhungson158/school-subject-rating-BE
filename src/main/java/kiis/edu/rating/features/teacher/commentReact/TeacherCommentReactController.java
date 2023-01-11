package kiis.edu.rating.features.teacher.commentReact;

import kiis.edu.rating.features.common.RequestDTO;
import kiis.edu.rating.features.teacher.comment.TeacherCommentRepository;
import kiis.edu.rating.features.user.UserRepository;
import kiis.edu.rating.helper.Util;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("unused")
@RestController
@AllArgsConstructor
@RequestMapping(path = "/teacher-comment-react")
public class TeacherCommentReactController {
    private final UserRepository userRepository;
    private final TeacherCommentRepository teacherCommentRepository;
    private final TeacherCommentReactRepository teacherCommentReactRepository;

    @GetMapping("/{id}")
    public TeacherCommentReactEntity getById(@PathVariable long id) {
        return teacherCommentReactRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Comment React with Id: " + id));
    }

    @GetMapping("/commentId/{id}")
    public List<TeacherCommentReactEntity> getByCommentId(@PathVariable("id") long commentId) {
        if (!teacherCommentRepository.existsById(commentId))
            throw new IllegalArgumentException("No Comment with Id: " + commentId);
        return teacherCommentReactRepository.findByCommentId(commentId);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('COMMENT_RATING_CREATE')")
    public void create(@RequestBody Request request) {
        if (!teacherCommentRepository.existsById(request.commentId))
            throw new IllegalArgumentException("No Comment with Id: " + request.commentId);
        if (!userRepository.existsById(request.userId))
            throw new IllegalArgumentException("No User with Id: " + request.userId);
        if (teacherCommentReactRepository.existsByUserIdAndCommentId(request.userId, request.commentId))
            throw new IllegalArgumentException("This User has already react this comment ");
        teacherCommentReactRepository.save(request.toEntity());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('COMMENT_RATING_UPDATE')")
    public void update(@PathVariable long id, @RequestBody Request request) {
        if (!teacherCommentReactRepository.existsById(id))
            throw new IllegalArgumentException("No comment React with id : " + id);
        TeacherCommentReactEntity teacherCommentReactEntity = request.toEntity();
        teacherCommentReactEntity.id = id;
        teacherCommentReactRepository.save(teacherCommentReactEntity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('COMMENT_RATING_DELETE')")
    public void deleteRating(@PathVariable long id) {
        teacherCommentReactRepository.deleteById(id);
    }

    @AllArgsConstructor
    private static class Request implements RequestDTO {
        public long userId, commentId;
        public boolean react;

        public TeacherCommentReactEntity toEntity() {
            return Util.mapping(this, TeacherCommentReactEntity.class);
        }
    }
}
