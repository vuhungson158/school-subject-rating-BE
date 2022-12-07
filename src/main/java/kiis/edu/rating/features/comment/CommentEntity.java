package kiis.edu.rating.features.comment;

import kiis.edu.rating.features.common.BaseEntity;
import kiis.edu.rating.features.common.enums.RefTable;

import javax.persistence.*;

@Entity
@Table(name = "comment")
public class CommentEntity extends BaseEntity {
    public long userId, refId;
    public String comment;
    @Enumerated(EnumType.STRING)
    public RefTable refTable;
    public boolean disable;
    @Column(insertable = false, updatable = false)
    public int likeCount;
    @Column(insertable = false, updatable = false)
    public int dislikeCount;
}
