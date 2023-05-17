package kiis.edu.rating.features.subject.plan;

import kiis.edu.rating.features.subject.condition.SubjectCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface SubjectPlanRepository extends JpaRepository<SubjectPlan, Long> {

    List<SubjectCondition> findAllByDisable(boolean disable);

    boolean existsByUserId(long userId);
}


