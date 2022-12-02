package kiis.edu.rating.features.teacher;

import kiis.edu.rating.features.common.BaseEntity;

import javax.persistence.Table;
import java.time.Instant;

@Table(name = "teacher")
public class TeacherEntity extends BaseEntity {
    public String name, nationality, gender;
    public Instant dob;

}
