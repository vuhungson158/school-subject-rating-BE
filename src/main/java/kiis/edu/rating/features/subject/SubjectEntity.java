package kiis.edu.rating.features.subject;

import kiis.edu.rating.features.common.BaseEntity;
import kiis.edu.rating.features.common.enums.Specialize;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
@Table(name = "subject")
@NoArgsConstructor
@AllArgsConstructor
public class SubjectEntity extends BaseEntity {
    public long teacherId;
    @Min(value = 1, message = "Min = 1")
    @Max(value = 6, message = "Max = 6")
    public int unit;
    @Min(value = 1, message = "Min = 1")
    @Max(value = 4, message = "Max = 4")
    public int formYear;
    public String name;
    @Enumerated(EnumType.STRING)
    public Specialize specialize;
    public boolean disable;
}
