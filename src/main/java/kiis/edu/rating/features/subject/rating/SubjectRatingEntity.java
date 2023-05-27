package kiis.edu.rating.features.subject.rating;

import kiis.edu.rating.features.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name = "subject_rating")
@AllArgsConstructor
@NoArgsConstructor
public class SubjectRatingEntity extends BaseEntity {
    public long userId, subjectId;
    @Min(value = 0, message = "Min = 0")
    @Max(value = 100, message = "Max = 100")
    public int practicality;
    @Min(value = 0, message = "Min = 0")
    @Max(value = 100, message = "Max = 100")
    public int difficult;
    @Min(value = 0, message = "Min = 0")
    @Max(value = 100, message = "Max = 100")
    public int homework;
    @Min(value = 0, message = "Min = 0")
    @Max(value = 100, message = "Max = 100")
    public int testDifficult;
    @Min(value = 0, message = "Min = 0")
    @Max(value = 100, message = "Max = 100")
    public int teacherPedagogical;
    @Min(value = 0, message = "Min = 0")
    @Max(value = 10, message = "Max = 10")
    public int star;
}


@Entity
@NoArgsConstructor
@AllArgsConstructor
class SubjectRatingAverage {
    @Id
    public long total;
    public double practicality, difficult, homework, testDifficult, teacherPedagogical, star;
}