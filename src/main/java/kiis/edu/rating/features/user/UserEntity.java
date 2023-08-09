package kiis.edu.rating.features.user;

import kiis.edu.rating.common.BaseEntity;
import kiis.edu.rating.enums.Gender;
import kiis.edu.rating.enums.PostgreSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Table(name = "auth_user")
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "gender", typeClass = PostgreSQLEnumType.class)
@TypeDef(name = "user_role", typeClass = PostgreSQLEnumType.class)
public class UserEntity extends BaseEntity {
    @Column(unique = true)
    public String email;
    public String password, displayName;
    @Enumerated(EnumType.STRING)
    @Type(type = "gender")
    public Gender gender;
    @Enumerated(EnumType.STRING)
    @Type(type = "user_role")
    public UserRole role;
}
