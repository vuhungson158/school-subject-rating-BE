package kiis.edu.rating.features.teacher.rating;

import kiis.edu.rating.features.common.BaseEntity;
import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@Entity
@Table(name = "teacher_rating")
public class TeacherRatingEntity extends BaseEntity {
    public long userId, teacherId;
    public int enthusiasm, friendly, nonConservatism, erudition, pedagogicalLevel;
}
