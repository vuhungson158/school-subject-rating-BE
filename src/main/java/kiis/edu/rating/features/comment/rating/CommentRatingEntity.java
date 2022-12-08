package kiis.edu.rating.features.comment.rating;

import kiis.edu.rating.features.common.BaseEntity;
import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "comment_rating")
@AllArgsConstructor
public class CommentRatingEntity extends BaseEntity {
    public long userId, commentId;
    // like = true, dislike = false
    public boolean react;
}
