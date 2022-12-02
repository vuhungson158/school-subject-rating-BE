package kiis.edu.rating.features.user;

import kiis.edu.rating.features.common.BaseEntity;

import javax.persistence.Table;
import java.time.Instant;

@Table(name = "user")
public class UserEntity extends BaseEntity {
    public String email, password, displayName, phoneNumber, gender;
    public Instant dob;
    public UserRole role;
}
