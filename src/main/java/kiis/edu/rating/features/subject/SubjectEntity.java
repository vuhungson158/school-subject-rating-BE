package kiis.edu.rating.features.subject;

import kiis.edu.rating.features.common.BaseEntity;
import kiis.edu.rating.features.common.enums.Specialize;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "subject")
@AllArgsConstructor
@NoArgsConstructor
public class SubjectEntity extends BaseEntity {
    public long teacherId;
    public int unit, formYear;
    public String name;
    @Enumerated(EnumType.STRING)
    public Specialize specialize;
    public boolean disable;
}

@Entity
@AllArgsConstructor
class SubjectEntityWithRating extends SubjectEntity {
    public double practicality, difficult, homework, testDifficult, teacherPedagogical;
}
