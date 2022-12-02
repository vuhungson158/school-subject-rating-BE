package kiis.edu.rating.features.rating;

import kiis.edu.rating.features.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "rating")
public class RatingEntity extends BaseEntity {
    public long userId, subjectId;
    public String comment;
    public boolean enable;
    public int star;
}
