package kiis.edu.rating.features.subject.registration.condition;

import kiis.edu.rating.features.subject.base.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface SubjectRegistrationConditionRepository
        extends JpaRepository<SubjectRegistrationCondition, Long> {

    List<SubjectRegistrationCondition> findAllByDisable(boolean disable);
}


