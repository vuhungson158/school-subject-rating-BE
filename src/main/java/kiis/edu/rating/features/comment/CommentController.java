package kiis.edu.rating.features.comment;

import kiis.edu.rating.features.comment.rating.CommentRatingEntity;
import kiis.edu.rating.features.comment.rating.CommentRatingRepository;
import kiis.edu.rating.features.common.RequestDTO;
import kiis.edu.rating.features.common.enums.RefTable;
import kiis.edu.rating.features.subject.SubjectRepository;
import kiis.edu.rating.features.teacher.TeacherRepository;
import kiis.edu.rating.features.user.UserRepository;
import kiis.edu.rating.helper.Util;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static kiis.edu.rating.helper.Constant.PATH;

@SuppressWarnings("unused")
@RestController
@AllArgsConstructor
@RequestMapping(path = PATH + "/comment")
public class CommentController {
    private final String RATING_PATH = "/rating";
    private final CommentRepository commentRepository;
    private final CommentWithLikeCountRepository commentWithLikeCountRepository;
    private final CommentRatingRepository commentRatingRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public CommentWithLikeCount getById(@PathVariable long id) {
        Optional<CommentEntity> optionalComment = commentRepository.findById(id);
        if (!optionalComment.isPresent())
            throw new IllegalArgumentException("No comment with id : " + id);
        CommentWithLikeCount commentEntityWithLikeCount = Util.mapping(optionalComment.get(), CommentWithLikeCount.class);
        ReactCount reactCount = countReact(id);
        assert commentEntityWithLikeCount != null;
        commentEntityWithLikeCount.likeCount = reactCount.like;
        commentEntityWithLikeCount.dislikeCount = reactCount.dislike;
        return commentEntityWithLikeCount;
    }

    @PostMapping("/top-comment/")
    public List<CommentWithLikeCount> getTopRatingById(@RequestBody TopCommentRequest request) {
        return commentWithLikeCountRepository
                .findTopRatingComment(request.limit, request.page, request.refTable, request.refId);
    }

    @GetMapping("")
    public List<CommentEntity> getAll() {
        return commentRepository.findAllByDisable(false);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('COMMENT_CREATE')")
    public void create(@RequestBody @Valid CommentRequest request) {
        String refTable = request.refTable.name();
        long userId = request.userId;
        long refId = request.refId;

        if (!userRepository.existsById(userId))
            throw new IllegalArgumentException("No User with id : " + userId);
        if (Objects.equals(refTable, "subject")
                && !subjectRepository.existsById(refId))
            throw new IllegalArgumentException("No subject with id : " + refId);
        if (Objects.equals(refTable, "teacher")
                && !teacherRepository.existsById(refId))
            throw new IllegalArgumentException("No teacher with id : " + refId);
        if (commentRepository.existsByRefTableAndRefIdAndUserId(refTable, refId, userId))
            throw new IllegalArgumentException("This User has already commented this " + refTable);

        commentRepository.save(request.toEntity());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('COMMENT_UPDATE')")
    public void update(@PathVariable long id, @RequestBody @Valid CommentRequest request) {
        if (!commentRepository.existsById(id))
            throw new IllegalArgumentException("No comment with id : " + id);
        CommentEntity commentEntity = request.toEntity();
        commentEntity.id = id;
        commentRepository.save(commentEntity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('COMMENT_DELETE')")
    public void delete(@PathVariable long id) {
        commentRepository.deleteById(id);
    }

    @GetMapping(RATING_PATH + "/{id}")
    public CommentRatingEntity getOneRatingById(@PathVariable long id) {
        Optional<CommentRatingEntity> optionalRating = commentRatingRepository.findById(id);
        if (!optionalRating.isPresent())
            throw new IllegalArgumentException("No rating with id : " + id);
        return optionalRating.get();
    }

    @GetMapping(RATING_PATH + "/commentId/{id}")
    public List<CommentRatingEntity> getRatingsByCommentId(@PathVariable("id") long commentId) {
        checkCommentExist(commentId);
        return commentRatingRepository.findByCommentId(commentId);
    }

    @PostMapping(RATING_PATH + "")
    @PreAuthorize("hasAuthority('COMMENT_RATING_CREATE')")
    public void createRating(@RequestBody RatingRequest request) {
        checkCommentExist(request.commentId);
        checkUserExist(request.userId);
        if (commentRatingRepository.findByUserIdAndCommentId(request.userId, request.commentId).isPresent())
            throw new IllegalArgumentException("This User has already react this comment ");
        commentRatingRepository.save(request.toEntity());
    }

    @PutMapping(RATING_PATH + "/{id}")
    @PreAuthorize("hasAuthority('COMMENT_RATING_UPDATE')")
    public void updateRating(@PathVariable long id, @RequestBody RatingRequest request) {
        if (!commentRatingRepository.existsById(id))
            throw new IllegalArgumentException("No comment Rating with id : " + id);
        CommentRatingEntity commentRatingEntity = request.toEntity();
        commentRatingEntity.id = id;
        commentRatingRepository.save(commentRatingEntity);
    }

    @DeleteMapping(RATING_PATH + "/{id}")
    @PreAuthorize("hasAuthority('COMMENT_RATING_DELETE')")
    public void deleteRating(@PathVariable long id) {
        commentRatingRepository.deleteById(id);
    }

    @AllArgsConstructor
    private static class CommentWithReactCount {
        public final CommentEntity commentEntity;
        public final ReactCount reactCount;
    }

    @AllArgsConstructor
    private static class ReactCount {
        public final int like, dislike;
    }

    @AllArgsConstructor
    private static class TopCommentRequest {
        public final int limit, page;
        public final String refTable;
        public final long refId;
    }

    @AllArgsConstructor
    private static class CommentRequest implements RequestDTO {
        public long userId, refId;
        public String comment;
        public RefTable refTable;
        public boolean disable;

        public CommentEntity toEntity() {
            return Util.mapping(this, CommentEntity.class);
        }
    }

    @AllArgsConstructor
    private static class RatingRequest implements RequestDTO {
        public long userId, commentId;
        public boolean react;

        public CommentRatingEntity toEntity() {
            return new CommentRatingEntity(userId, commentId, react);
        }
    }

    private void checkCommentExist(long commentId) {
        if (!commentRepository.existsById(commentId))
            throw new IllegalArgumentException("No comment with id : " + commentId);
    }

    private void checkUserExist(long userId) {
        if (!userRepository.findById(userId).isPresent())
            throw new IllegalArgumentException("No User with id : " + userId);
    }

    private ReactCount countReact(long commentId) {
        int like, dislike;
        like = commentRatingRepository.countByCommentIdAndReact(commentId, true);
        dislike = commentRatingRepository.countByCommentIdAndReact(commentId, false);
        return new ReactCount(like, dislike);
    }
}
