package kiis.edu.rating.features.teacher.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import kiis.edu.rating.features.common.BaseEntity;
import kiis.edu.rating.features.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table(name = "teacher")
@AllArgsConstructor
@NoArgsConstructor
public class TeacherEntity extends BaseEntity {
    public String name, nationality;
    @Enumerated(EnumType.STRING)
    public Gender gender;
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    public Date dob;
}
