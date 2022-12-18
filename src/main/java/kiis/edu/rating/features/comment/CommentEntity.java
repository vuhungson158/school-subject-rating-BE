package kiis.edu.rating.features.comment;

import kiis.edu.rating.features.common.BaseEntity;
import kiis.edu.rating.features.common.enums.RefTable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

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
    @Formula(value = "count(if(react=1,1,null))")
    public int likeCount;
    @Formula(value = "case when count(if(react=0,1,null))")
    public int dislikeCount;
}
