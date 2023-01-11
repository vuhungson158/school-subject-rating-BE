package kiis.edu.rating.features.subject.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRatingRepository extends JpaRepository<SubjectRatingEntity, Long> {
    List<SubjectRatingEntity> findAllBySubjectId(long subjectId);
    List<SubjectRatingEntity> findAllByUserId(long userId);
    Optional<SubjectRatingEntity> findBySubjectIdAndUserId(long subjectId, long userId);
}

@Repository
interface SubjectRatingAverageRepository extends JpaRepository<SubjectRatingAverage, Long> {
//    TODO: Rewrite
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
    SubjectRatingAverage findSubjectRatingAverageById(long id);
}