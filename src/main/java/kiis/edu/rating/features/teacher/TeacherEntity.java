package kiis.edu.rating.features.teacher;

import kiis.edu.rating.features.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "teacher")
public class TeacherEntity extends BaseEntity {
    public String name, nationality, gender;
    public Instant dob;
}
