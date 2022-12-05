package kiis.edu.rating.features.comment;

import kiis.edu.rating.features.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "comment")
public class CommentEntity extends BaseEntity {
    public long userId, refId;
    public String refTable, comment;
    public boolean disable;
}
