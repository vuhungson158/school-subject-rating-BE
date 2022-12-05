package kiis.edu.rating.features.comment;

import kiis.edu.rating.features.comment.rating.CommentRatingEntity;
import kiis.edu.rating.features.comment.rating.CommentRatingRepository;
import kiis.edu.rating.features.subject.SubjectRepository;
import kiis.edu.rating.features.teacher.TeacherRepository;
import lombok.AllArgsConstructor;
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
    private final CommentRatingRepository commentRatingRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    @GetMapping("/{id}")
    public CommentWithReactCount getById(@PathVariable long id) {
        Optional<CommentEntity> optionalComment = commentRepository.findById(id);
        if (!optionalComment.isPresent())
            throw new IllegalArgumentException("No comment with id : " + id);
        return new CommentWithReactCount(optionalComment.get(), countReact(id));
    }

//    @GetMapping("/top-rating/{id}")
//    public CommentWithReactCount getTopRatingById(@PathVariable long id) {
//        Optional<CommentEntity> optionalComment = commentRepository.findById(id);
//        commentRepository.findAll(Pageable);
//        if (!optionalComment.isPresent())
//            throw new IllegalArgumentException("No comment with id : " + id);
//        return new CommentWithReactCount(optionalComment.get(), countReact(id));
//    }

    @GetMapping("")
    public List<CommentEntity> getAll() {
        return commentRepository.findAllByDisable(false);
    }

    @PostMapping("")
    public boolean create(@RequestBody @Valid CommentEntity commentEntity) {
        commentEntity.makeSureBaseEntityEmpty();
        if (Objects.equals(commentEntity.refTable, "subject")
                && subjectRepository.findById(commentEntity.refId).isPresent())
            throw new IllegalArgumentException("No subject with id : " + commentEntity.refId);
        if (Objects.equals(commentEntity.refTable, "teacher")
                && !teacherRepository.findById(commentEntity.refId).isPresent())
            throw new IllegalArgumentException("No teacher with id : " + commentEntity.refId);
        if (commentRepository.findByRefTableAndRefIdAndUserId(commentEntity.refTable, commentEntity.refId, commentEntity.userId).isPresent())
            throw new IllegalArgumentException("This User has already commented this " + commentEntity.refTable);
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
    public List<CommentRatingEntity> getRatingsByCommentId(@PathVariable long commentId) {
        checkCommentExist(commentId);
        return commentRatingRepository.findByCommentId(commentId);
    }

    @PostMapping(RATING_PATH + "")
    public boolean createRating(@RequestBody CommentRatingEntity commentRatingEntity) {
        commentRatingEntity.makeSureBaseEntityEmpty();
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

    private void checkCommentExist(long commentId) {
        if (!commentRepository.findById(commentId).isPresent())
            throw new IllegalArgumentException("No comment with id : " + commentId);
    }

    private ReactCount countReact(long commentId) {
        long like, dislike;
        like = commentRatingRepository.countByCommentIdAndReact(commentId, true);
        dislike = commentRatingRepository.countByCommentIdAndReact(commentId, false);
        return new ReactCount(like, dislike);
    }
}
