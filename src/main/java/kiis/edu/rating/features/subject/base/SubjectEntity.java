package kiis.edu.rating.features.subject.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import kiis.edu.rating.common.BaseEntity;
import kiis.edu.rating.enums.Department;
import kiis.edu.rating.enums.PostgreSQLEnumType;
import kiis.edu.rating.enums.SubjectClassification;
import kiis.edu.rating.features.teacher.base.TeacherEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
//@EntityListeners(SubjectEntityListener.class)
@Table(name = "subject")
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "department", typeClass = PostgreSQLEnumType.class)
@TypeDef(name = "classification", typeClass = PostgreSQLEnumType.class)
public class SubjectEntity extends BaseEntity {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "teacher_id")
    public Long teacherId;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id", insertable = false, updatable = false)
    public TeacherEntity teacher;

    @Min(value = 1, message = "Min = 1")
    @Max(value = 6, message = "Max = 6")
    public Integer credit;

    @Min(value = 1, message = "Min = 1")
    @Max(value = 4, message = "Max = 4")
    public Integer formYear;

    public String name;

    @Enumerated(EnumType.STRING)
    @Type(type = "department")
    public Department department;

    @Enumerated(EnumType.STRING)
    @Type(type = "classification")
    public SubjectClassification.Small classification;

    public Boolean require = false;

    public String semester;

    public String schedule;
}