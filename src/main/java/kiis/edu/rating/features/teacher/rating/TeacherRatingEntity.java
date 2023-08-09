package kiis.edu.rating.features.teacher.rating;

import kiis.edu.rating.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "teacher_rating")
class TeacherRatingEntity extends BaseEntity {
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
    @Min(value = 0, message = "Min = 0")
    @Max(value = 10, message = "Max = 10")
    public int star;
}


@Entity
@NoArgsConstructor
@AllArgsConstructor
class TeacherRatingAverage {
    @Id
    public long total;
    public double enthusiasm, friendly, nonConservatism, erudition, pedagogicalLevel, star;
}