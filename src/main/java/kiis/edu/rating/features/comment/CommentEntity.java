package kiis.edu.rating.features.comment;

import kiis.edu.rating.features.common.BaseEntity;
import kiis.edu.rating.features.common.enums.RefTable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "comment")
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity extends BaseEntity {
    public long userId, refId;
    public String comment;
    @Enumerated(EnumType.STRING)
    public RefTable refTable;
    public boolean disable;
}

@Entity
class CommentEntityWithLikeCount extends CommentEntity {
    public int likeCount;
    public int dislikeCount;
}


