package kiis.edu.rating.features.teacher.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import kiis.edu.rating.common.BaseEntity;
import kiis.edu.rating.enums.Gender;
import kiis.edu.rating.enums.PostgreSQLEnumType;
import kiis.edu.rating.features.subject.base.SubjectEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "teacher")
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "gender", typeClass = PostgreSQLEnumType.class)
public class TeacherEntity extends BaseEntity {

    @OneToMany(mappedBy = "teacher")
    @JsonIgnore
    public List<SubjectEntity> subjectList;

    public String name, nationality;

    @Enumerated(EnumType.STRING)
    @Type(type = "gender")
    public Gender gender;

    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    public Date dob;
}
