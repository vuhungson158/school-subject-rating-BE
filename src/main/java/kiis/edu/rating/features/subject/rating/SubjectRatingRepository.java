package kiis.edu.rating.features.subject.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    @Query(nativeQuery = true, value =
            "select"
                    + "\n count(*) as total,"
                    + "\n avg(practicality) as practicality,"
                    + "\n avg(difficult) as difficult,"
                    + "\n avg(homework) as homework,"
                    + "\n avg(test_difficult) as test_difficult,"
                    + "\n avg(teacher_pedagogical) as teacher_pedagogical,"
                    + "\n avg(star) as star"
                    + "\n from"
                    + "\n subject_rating"
                    + "\n where"
                    + "\n subject_id = :id"
    )
    SubjectRatingAverage findSubjectRatingAverageBySubjectId(@Param("id") long id);

    @Query(nativeQuery = true, value =
            "select"
                    + "\n count(*) as total,"
                    + "\n avg(practicality) as practicality,"
                    + "\n avg(difficult) as difficult,"
                    + "\n avg(homework) as homework,"
                    + "\n avg(test_difficult) as test_difficult,"
                    + "\n avg(teacher_pedagogical) as teacher_pedagogical,"
                    + "\n avg(star) as star"
                    + "\n from"
                    + "\n subject_rating"
                    + "\n where"
                    + "\n user_id = :id"
    )
    SubjectRatingAverage findSubjectRatingAverageByUserId(@Param("id") long id);

}