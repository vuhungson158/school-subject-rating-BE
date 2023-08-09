package kiis.edu.rating.features.teacher.comment;

import kiis.edu.rating.features.teacher.base.TeacherRepository;
import kiis.edu.rating.features.user.UserRepository;
import kiis.edu.rating.helper.Util;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

@SuppressWarnings({"unused", "ClassEscapesDefinedScope"})
@RestController
@AllArgsConstructor
@RequestMapping(path = "/teacher-comment")
public class TeacherCommentController {
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherCommentRepository teacherCommentRepository;
    private final TeacherCommentWithLikeCountRepository teacherCommentWithLikeCountRepository;

    @GetMapping("/{id}")
    public TeacherCommentWithLikeCount getById(@PathVariable long id) {
        return teacherCommentWithLikeCountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Comment with Id: " + id));
    }

    @GetMapping("/top-comment")
    public List<TeacherCommentWithLikeCount> getTopRatingById(
            @RequestParam long teacherId, @RequestParam int limit, @RequestParam int page) {
        return teacherCommentWithLikeCountRepository
                .findTopRatingComment(limit, page, teacherId);
    }

    @GetMapping("")
    public List<TeacherCommentEntity> getAll() {
        return teacherCommentRepository.findAllByIsDeleted(false);
    }

    @PostMapping("")
    public void create(@RequestBody @Valid TeacherCommentRequest request) {
        long userId = request.userId;
        long teacherId = request.teacherId;

        if (!userRepository.existsById(userId))
            throw new IllegalArgumentException("No User with id : " + userId);
        if (!teacherRepository.existsById(teacherId))
            throw new IllegalArgumentException("No teacher with id : " + teacherId);
        if (teacherCommentRepository.existsByTeacherIdAndUserId(teacherId, userId))
            throw new IllegalArgumentException("This User has already commented this " + teacherId);

        teacherCommentRepository.save(request.toEntity());
    }

    @PutMapping("/{id}")
    public void update(@PathVariable long id, @RequestBody @Valid TeacherCommentRequest request) {
        if (!teacherCommentRepository.existsById(id))
            throw new IllegalArgumentException("No comment with id : " + id);
        TeacherCommentEntity commentEntity = request.toEntity();
        commentEntity.id = id;
        teacherCommentRepository.save(commentEntity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        teacherCommentRepository.deleteById(id);
    }

    private static class TeacherCommentRequest extends TeacherCommentEntity {
        private long id;
        private Instant createdAt, updatedAt;
        private boolean disable;

        public TeacherCommentEntity toEntity() {
            return Util.mapping(this, TeacherCommentEntity.class);
        }
    }
}
