package kiis.edu.rating.features.subject.registration.condition;

import kiis.edu.rating.features.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "subject_registration_condition")
@AllArgsConstructor
@NoArgsConstructor
public class SubjectRegistrationCondition extends BaseEntity {
    public int fromId, toId;
}
