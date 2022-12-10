package kiis.edu.rating.features.comment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kiis.edu.rating.features.common.BaseEntity;
import kiis.edu.rating.features.common.enums.RefTable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Column(insertable = false, updatable = false)
    public int likeCount;
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Column(insertable = false, updatable = false)
    public int dislikeCount;
//    LinkedHashMap;
//    HashMap

}
