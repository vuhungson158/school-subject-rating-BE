package kiis.edu.rating.features.comment;

import kiis.edu.rating.features.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "comment")
public class CommentEntity extends BaseEntity {
    public long userId, refId;
    public long refTable;
    public String comment;
    public boolean enable;
}
