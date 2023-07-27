package kiis.edu.rating.features.subject.base;

import kiis.edu.rating.enums.Department;
import kiis.edu.rating.enums.PostgreSQLEnumType;
import kiis.edu.rating.enums.SubjectClassification;
import kiis.edu.rating.features.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name = "subject")
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "department", typeClass = PostgreSQLEnumType.class)
@TypeDef(name = "classification", typeClass = PostgreSQLEnumType.class)
public class SubjectEntity extends BaseEntity {
    public long teacherId;
    @Min(value = 1, message = "Min = 1")
    @Max(value = 6, message = "Max = 6")
    public int credit;
    @Min(value = 1, message = "Min = 1")
    @Max(value = 4, message = "Max = 4")
    public int formYear;
    public String name;
    @Enumerated(EnumType.STRING)
    @Type(type = "department")
    public Department department;
    @Enumerated(EnumType.STRING)
    @Type(type = "classification")
    public SubjectClassification.Small classification;
    public boolean require = false;
    public String semester = "";
    public String schedule = "";
}