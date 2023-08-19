package kiis.edu.rating.features.subject.base;

import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;

@Component
public class SubjectEntityListener {


    @PrePersist
    public void prePersist(SubjectEntity subjectEntity) {
        final Long teacherId = subjectEntity.teacherId;
    }
}
