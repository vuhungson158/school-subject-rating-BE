package kiis.edu.rating.features.teacher.comment;

import kiis.edu.rating.features.common.RequestDTO;
import kiis.edu.rating.features.teacher.base.TeacherRepository;
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

    @GetMapping("/{id}/top-comment/{limit}/{page}/")
    public List<TeacherCommentWithLikeCount> getTopRatingById(@PathVariable long id, @PathVariable int limit, @PathVariable int page) {
        return teacherCommentWithLikeCountRepository
                .findTopRatingComment(limit, page, id);
    }

    @GetMapping("")
    public List<TeacherCommentEntity> getAll() {
        return teacherCommentRepository.findAllByDisable(false);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('COMMENT_CREATE')")
    public void create(@RequestBody @Valid Request request) {
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
    @PreAuthorize("hasAuthority('COMMENT_UPDATE')")
    public void update(@PathVariable long id, @RequestBody @Valid Request request) {
        if (!teacherCommentRepository.existsById(id))
            throw new IllegalArgumentException("No comment with id : " + id);
        TeacherCommentEntity commentEntity = request.toEntity();
        commentEntity.id = id;
        teacherCommentRepository.save(commentEntity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('COMMENT_DELETE')")
    public void delete(@PathVariable long id) {
        teacherCommentRepository.deleteById(id);
    }

    @AllArgsConstructor
    private static class Request implements RequestDTO {
        public long userId, teacherId;
        public String comment;

        @Override
        public TeacherCommentEntity toEntity() {
            return Util.mapping(this, TeacherCommentEntity.class);
        }
    }
}
