package kiis.edu.rating.features.comment;

import kiis.edu.rating.features.common.BaseEntity;
import kiis.edu.rating.features.common.enums.RefTable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "comment")
@AllArgsConstructor
public class CommentEntity extends BaseCommentEntity {

}

@Entity
class CommentWithLikeCount extends BaseCommentEntity {
    public int likeCount;
    public int dislikeCount;
}

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
abstract class BaseCommentEntity extends BaseEntity {
    public long userId, refId;
    public String comment;
    @Enumerated(EnumType.STRING)
    public RefTable refTable;
}