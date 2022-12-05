package kiis.edu.rating.features.comment.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRatingRepository extends JpaRepository<CommentRatingEntity, Long> {
    long countByCommentIdAndReact(long commentId, boolean react);

    List<CommentRatingEntity> findByCommentId(long commentId);

    Optional<CommentRatingEntity> findByUserIdAndCommentId(long userId, long commentId);
}
