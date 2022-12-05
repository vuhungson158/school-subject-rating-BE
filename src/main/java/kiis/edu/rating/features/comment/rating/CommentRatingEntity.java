package kiis.edu.rating.features.comment.rating;

import kiis.edu.rating.features.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name = "comment_rating")
public class CommentRatingEntity extends BaseEntity {
    public long userId, commentId;
    // like = true, dislike = false
    public boolean react;
}
