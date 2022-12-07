package kiis.edu.rating.features.comment;

import kiis.edu.rating.features.comment.rating.CommentRatingEntity;
import kiis.edu.rating.features.comment.rating.CommentRatingRepository;
import kiis.edu.rating.features.subject.SubjectRepository;
import kiis.edu.rating.features.teacher.TeacherRepository;
import kiis.edu.rating.features.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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
    private final CommentRatingRepository commentRatingRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public CommentWithReactCount getById(@PathVariable long id) {
        Optional<CommentEntity> optionalComment = commentRepository.findById(id);
        if (!optionalComment.isPresent())
            throw new IllegalArgumentException("No comment with id : " + id);
        return new CommentWithReactCount(optionalComment.get(), countReact(id));
    }

    @PostMapping("/top-comment/")
    public List<CommentWithReactCount> getTopRatingById(@RequestBody TopCommentRequest request) {
        List<CommentWithReactCount> result = new ArrayList<>();
        List<CommentEntity> commentEntityList = commentRepository
                .findTopRatingComment(request.limit, request.page, request.refTable, request.refId);
        for (CommentEntity commentEntity : commentEntityList) {
            ReactCount reactCount = countReact(commentEntity.id);
            result.add(new CommentWithReactCount(commentEntity, reactCount));
        }
        return result;
    }

    @GetMapping("")
    public List<CommentEntity> getAll() {
        return commentRepository.findAllByDisable(false);
    }

    @PostMapping("")
    public boolean create(@RequestBody @Valid CommentEntity commentEntity) {
        commentEntity.makeSureBaseEntityEmpty();
        String refTable = commentEntity.refTable.name();
        long userId = commentEntity.userId;
        long refId = commentEntity.refId;

        if (!userRepository.findById(userId).isPresent())
            throw new IllegalArgumentException("No User with id : " + userId);
        if (Objects.equals(refTable, "subject")
                && !subjectRepository.findById(refId).isPresent())
            throw new IllegalArgumentException("No subject with id : " + refId);
        if (Objects.equals(refTable, "teacher")
                && !teacherRepository.findById(refId).isPresent())
            throw new IllegalArgumentException("No teacher with id : " + refId);
        if (commentRepository.findByRefTableAndRefIdAndUserId(refTable, refId, userId).isPresent())
            throw new IllegalArgumentException("This User has already commented this " + refTable);
        commentRepository.save(commentEntity);
        return true;
    }

    @PutMapping("/{id}")
    public boolean update(@PathVariable long id, @RequestBody @Valid CommentEntity commentEntity) {
        commentEntity.makeSureBaseEntityEmpty();
        if (!commentRepository.findById(id).isPresent())
            throw new IllegalArgumentException("No comment with id : " + id);
        commentRepository.save(commentEntity);
        return true;
    }

    @DeleteMapping("/{id}")
    public boolean update(@PathVariable long id) {
//        if (!commentRepository.findById(id).isPresent())
//            throw new IllegalArgumentException("No comment with id : " + id);
        commentRepository.deleteById(id);
        return true;
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
    public boolean createRating(@RequestBody CommentRatingEntity commentRatingEntity) {
        commentRatingEntity.makeSureBaseEntityEmpty();
        checkCommentExist(commentRatingEntity.commentId);
        checkUserExist(commentRatingEntity.userId);
        if (commentRatingRepository.findByUserIdAndCommentId(commentRatingEntity.userId, commentRatingEntity.commentId).isPresent())
            throw new IllegalArgumentException("This User has already react this comment ");
        commentRatingRepository.save(commentRatingEntity);
        return true;
    }

    @PutMapping(RATING_PATH + "/{id}")
    public boolean updateRating(@PathVariable long id, @RequestBody CommentRatingEntity commentRatingEntity) {
        commentRatingEntity.makeSureBaseEntityEmpty();
        if (!commentRatingRepository.findById(id).isPresent())
            throw new IllegalArgumentException("No comment Rating with id : " + id);
        commentRatingEntity.id = id;
        commentRatingRepository.save(commentRatingEntity);
        return true;
    }

    @DeleteMapping(RATING_PATH + "/{id}")
    public boolean deleteRating(@PathVariable long id) {
        commentRatingRepository.deleteById(id);
        return true;
    }

    @AllArgsConstructor
    private static class CommentWithReactCount {
        public final CommentEntity commentEntity;
        public final ReactCount reactCount;
    }

    @AllArgsConstructor
    private static class ReactCount {
        public final long like, dislike;
    }

    @AllArgsConstructor
    private static class TopCommentRequest {
        public final int limit, page;
        public final String refTable;
        public final long refId;
    }

    private void checkCommentExist(long commentId) {
        if (!commentRepository.findById(commentId).isPresent())
            throw new IllegalArgumentException("No comment with id : " + commentId);
    }

    private void checkUserExist(long userId) {
        if (!userRepository.findById(userId).isPresent())
            throw new IllegalArgumentException("No User with id : " + userId);
    }

    private ReactCount countReact(long commentId) {
        long like, dislike;
        like = commentRatingRepository.countByCommentIdAndReact(commentId, true);
        dislike = commentRatingRepository.countByCommentIdAndReact(commentId, false);
        return new ReactCount(like, dislike);
    }
}
