package kiis.edu.rating.features.teacher;

import kiis.edu.rating.features.common.BaseEntity;
import kiis.edu.rating.features.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "teacher")
@AllArgsConstructor
@NoArgsConstructor
public class TeacherEntity extends BaseEntity {
    public String name, nationality;
    @Enumerated(EnumType.STRING)
    public Gender gender;
    public Instant dob;
    public boolean disable;
}

@Entity
@AllArgsConstructor
class TeacherEntityWithRating extends TeacherEntity {
    public double enthusiasm, friendly, nonConservatism, erudition, pedagogicalLevel;
}
