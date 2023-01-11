package kiis.edu.rating.features.user;

import kiis.edu.rating.features.common.BaseEntity;
import kiis.edu.rating.features.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "auth_user")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {
    @Column(unique = true)
    public String email;
    public String password, displayName;
    @Enumerated(EnumType.STRING)
    public Gender gender;
    @Enumerated(EnumType.STRING)
    public UserRole role;
}
