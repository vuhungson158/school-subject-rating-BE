package kiis.edu.rating.features.subject.base;

import kiis.edu.rating.features.common.BaseEntity;
import kiis.edu.rating.enums.Department;
import kiis.edu.rating.enums.PostgreSQLEnumType;
import kiis.edu.rating.enums.SubjectClassification;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "subject")
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "department", typeClass = PostgreSQLEnumType.class)
@TypeDef(name = "classification", typeClass = PostgreSQLEnumType.class)
public class SubjectEntity extends BaseEntity {
    public long teacherId;
    public int unit, formYear;
    public String name;
    @Enumerated(EnumType.STRING)
    public Department department;
    @Enumerated(EnumType.STRING)
    public SubjectClassification.Small classification;
}