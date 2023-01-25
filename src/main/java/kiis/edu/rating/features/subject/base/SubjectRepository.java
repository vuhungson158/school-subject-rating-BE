package kiis.edu.rating.features.subject.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, Long> {
    List<SubjectEntity> findAllByDisable(boolean disable);

    @Modifying
    @Query(value = "UPDATE subject SET disable = :disable WHERE teacher_id = :teacherId", nativeQuery = true)
    void updateDisableSubjectByTeacherId(@Param("teacherId") long teacherId, @Param("disable") boolean disable);
}


