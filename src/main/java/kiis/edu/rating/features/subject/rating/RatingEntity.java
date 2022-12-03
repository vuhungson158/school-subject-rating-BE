package kiis.edu.rating.features.subject.rating;

import kiis.edu.rating.features.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "subject_rating")
public class RatingEntity extends BaseEntity {
    public long userId, subjectId;
    public int star;
}
