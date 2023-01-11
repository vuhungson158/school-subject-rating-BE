package kiis.edu.rating.features.subject.base;

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
}