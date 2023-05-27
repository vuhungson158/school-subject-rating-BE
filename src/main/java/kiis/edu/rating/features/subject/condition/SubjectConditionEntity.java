package kiis.edu.rating.features.subject.condition;

import kiis.edu.rating.features.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "subject_condition")
@AllArgsConstructor
@NoArgsConstructor
public class SubjectConditionEntity extends BaseEntity {
    public long fromId, toId;
}
