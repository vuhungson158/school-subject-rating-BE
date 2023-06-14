package kiis.edu.rating.features.teacher.commentReact;

import kiis.edu.rating.features.teacher.comment.TeacherCommentRepository;
import kiis.edu.rating.features.user.UserRepository;
import kiis.edu.rating.helper.Util;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@SuppressWarnings({"unused", "ClassEscapesDefinedScope"})
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
    public void create(@RequestBody TeacherCommentReactRequest request) {
        if (!teacherCommentRepository.existsById(request.commentId))
            throw new IllegalArgumentException("No Comment with Id: " + request.commentId);
        if (!userRepository.existsById(request.userId))
            throw new IllegalArgumentException("No User with Id: " + request.userId);
        if (teacherCommentReactRepository.existsByUserIdAndCommentId(request.userId, request.commentId))
            throw new IllegalArgumentException("This User has already react this comment ");
        teacherCommentReactRepository.save(request.toEntity());
    }

    @PutMapping("/{id}")
    public void update(@PathVariable long id, @RequestBody TeacherCommentReactRequest request) {
        if (!teacherCommentReactRepository.existsById(id))
            throw new IllegalArgumentException("No comment React with id : " + id);
        TeacherCommentReactEntity teacherCommentReactEntity = request.toEntity();
        teacherCommentReactEntity.id = id;
        teacherCommentReactRepository.save(teacherCommentReactEntity);
    }

    @DeleteMapping("/{id}")
    public void deleteRating(@PathVariable long id) {
        teacherCommentReactRepository.deleteById(id);
    }

    private static class TeacherCommentReactRequest extends TeacherCommentReactEntity {
        private long id;
        private Instant createdAt, updatedAt;
        private boolean disable;

        public TeacherCommentReactEntity toEntity() {
            return Util.mapping(this, TeacherCommentReactEntity.class);
        }
    }
}
