package kiis.edu.rating.features.subject.rating;

import kiis.edu.rating.features.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name = "subject_rating")
public class RatingEntity extends BaseEntity {
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
}
