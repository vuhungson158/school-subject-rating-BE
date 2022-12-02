package kiis.edu.rating.features.subject;

import kiis.edu.rating.features.common.BaseEntity;

import javax.persistence.Table;

@Table(name = "subject")
public class SubjectEntity extends BaseEntity {
    public long teacherId;
    public int unit, formYear;
    public String name, specialize;
}
