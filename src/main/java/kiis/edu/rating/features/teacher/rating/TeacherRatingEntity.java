package kiis.edu.rating.features.teacher.rating;

import kiis.edu.rating.features.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name = "teacher_rating")
public class TeacherRatingEntity extends BaseEntity {
    public long userId, teacherId;
    @Min(value = 0, message = "Min = 0")
    @Max(value = 100, message = "Max = 100")
    public int enthusiasm;
    @Min(value = 0, message = "Min = 0")
    @Max(value = 100, message = "Max = 100")
    public int friendly;
    @Min(value = 0, message = "Min = 0")
    @Max(value = 100, message = "Max = 100")
    public int nonConservatism;
    @Min(value = 0, message = "Min = 0")
    @Max(value = 100, message = "Max = 100")
    public int erudition;
    @Min(value = 0, message = "Min = 0")
    @Max(value = 100, message = "Max = 100")
    public int pedagogicalLevel;
}
