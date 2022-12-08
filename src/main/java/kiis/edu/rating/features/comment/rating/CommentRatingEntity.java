package kiis.edu.rating.features.comment.rating;

import kiis.edu.rating.features.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "comment_rating")
@AllArgsConstructor
@NoArgsConstructor
public class CommentRatingEntity extends BaseEntity {
    public long userId, commentId;
    // like = true, dislike = false
    public boolean react;
}
