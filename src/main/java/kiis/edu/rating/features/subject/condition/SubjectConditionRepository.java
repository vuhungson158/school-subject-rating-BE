package kiis.edu.rating.features.subject.condition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface SubjectConditionRepository extends JpaRepository<SubjectCondition, Long> {

    List<SubjectCondition> findAllByDisable(boolean disable);
}


