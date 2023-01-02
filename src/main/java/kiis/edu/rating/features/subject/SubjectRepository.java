package kiis.edu.rating.features.subject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, Long> {
    List<SubjectEntity> findAllByDisable(boolean disable);
}


@Repository
interface SubjectWithAvgRatingRepository extends JpaRepository<SubjectWithAvgRating, Long> {
    @Query(nativeQuery = true, value =
            "select subject.*, "
                    + "avg(practicality) as practicality, "
                    + "avg(difficult) as difficult, "
                    + "avg(homework) as homework, "
                    + "avg(test_difficult) as test_difficult, "
                    + "avg(teacher_pedagogical) as teacher_pedagogical "
                    + "from subject "
                    + "left join subject_rating "
                    + "on subject.id = subject_rating.subject_id "
                    + "where subject.id = ?1 "
                    + "group by subject.id limit 1"
    )
    SubjectWithAvgRating findSubjectEntityWithRatingById(long id);
}