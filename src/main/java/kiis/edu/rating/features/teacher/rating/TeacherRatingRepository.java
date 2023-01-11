package kiis.edu.rating.features.teacher.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface TeacherRatingRepository extends JpaRepository<TeacherRatingEntity, Long> {
    List<TeacherRatingEntity> findAllByTeacherId(long teacherId);
    List<TeacherRatingEntity> findAllByUserId(long userId);
    Optional<TeacherRatingEntity> existsByTeacherIdAndUserId(long teacherId, long userId);
}


@Repository
interface TeacherRatingAverageRepository extends JpaRepository<TeacherRatingAverage, Long> {
    @Query(nativeQuery = true, value =
            "select teacher.*,"
                    + "avg(enthusiasm) as enthusiasm,"
                    + "avg(friendly) as friendly,"
                    + "avg(non_conservatism) as non_conservatism,"
                    + "avg(erudition) as erudition,"
                    + "avg(pedagogical_level) as pedagogical_level"
                    + "from teacher"
                    + "left join teacher_rating"
                    + "on teacher.id = teacher_rating.teacher_id"
                    + "where teacher.id = ?1"
                    + "group by teacher_rating.id"
    )
    TeacherRatingAverage findTeacherRatingAverageByTeacherId(long id);
}