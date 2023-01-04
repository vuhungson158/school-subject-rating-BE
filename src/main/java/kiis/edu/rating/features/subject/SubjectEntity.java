package kiis.edu.rating.features.subject;

import kiis.edu.rating.features.common.BaseEntity;
import kiis.edu.rating.features.common.enums.Specialize;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "subject")
@AllArgsConstructor
public class SubjectEntity extends BaseSubjectEntity {

}

@Entity
@NoArgsConstructor
@AllArgsConstructor
class SubjectWithAvgRating extends BaseSubjectEntity {
    public BigDecimal practicality, difficult, homework, testDifficult, teacherPedagogical;
}

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
abstract class BaseSubjectEntity extends BaseEntity {
    public long teacherId;
    public int unit, formYear;
    public String name;
    @Enumerated(EnumType.STRING)
    public Specialize specialize;
}
