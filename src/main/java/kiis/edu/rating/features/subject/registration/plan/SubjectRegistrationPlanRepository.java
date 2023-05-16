package kiis.edu.rating.features.subject.registration.plan;

import kiis.edu.rating.features.subject.registration.condition.SubjectRegistrationCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface SubjectRegistrationPlanRepository
        extends JpaRepository<SubjectRegistrationPlan, Long> {

    List<SubjectRegistrationCondition> findAllByDisable(boolean disable);

    boolean existsByUserId(long userId);
}


