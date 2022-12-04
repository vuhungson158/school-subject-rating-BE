package kiis.edu.rating.features.user;

import kiis.edu.rating.features.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.Instant;

@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {
    @Column(unique = true)
    public String email;
    public String password, displayName, gender;
    public Instant dob;
    @Enumerated(EnumType.STRING)
    public UserRole role;
}
