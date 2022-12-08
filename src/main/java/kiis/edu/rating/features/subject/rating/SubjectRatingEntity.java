package kiis.edu.rating.features.subject.rating;

import kiis.edu.rating.features.common.BaseEntity;
import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "subject_rating")
@AllArgsConstructor
public class SubjectRatingEntity extends BaseEntity {
    public long userId, subjectId;
    public int practicality, difficult, homework, testDifficult, teacherPedagogical;
}
