package kiis.edu.rating.features.teacher.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRatingRepository extends JpaRepository<TeacherRatingEntity, Long> {
    List<TeacherRatingEntity> findAllByTeacherId(long teacherId);
    List<TeacherRatingEntity> findAllByUserId(long userId);
    boolean existsByTeacherIdAndUserId(long teacherId, long userId);
    Optional<TeacherRatingEntity> findByTeacherIdAndUserId(long teacherId, long userId);
}


@Repository
interface TeacherRatingAverageRepository extends JpaRepository<TeacherRatingAverage, Long> {
    @Query(nativeQuery = true, value =
            "select"
                    + "\n count(*) as total,"
                    + "\n avg(enthusiasm) as enthusiasm,"
                    + "\n avg(friendly) as friendly,"
                    + "\n avg(non_conservatism) as non_conservatism,"
                    + "\n avg(erudition) as erudition,"
                    + "\n avg(pedagogical_level) as pedagogical_level,"
                    + "\n avg(star) as star"
                    + "\n from"
                    + "\n teacher_rating"
                    + "\n where"
                    + "\n teacher_id = :id"
    )
    TeacherRatingAverage findTeacherRatingAverageByTeacherId(long id);

    @Query(nativeQuery = true, value =
            "select"
                    + "\n count(*) as total,"
                    + "\n avg(enthusiasm) as enthusiasm,"
                    + "\n avg(friendly) as friendly,"
                    + "\n avg(non_conservatism) as non_conservatism,"
                    + "\n avg(erudition) as erudition,"
                    + "\n avg(pedagogical_level) as pedagogical_level,"
                    + "\n avg(star) as star"
                    + "\n from"
                    + "\n teacher_rating"
                    + "\n where"
                    + "\n user_id = :id"
    )
    TeacherRatingAverage findTeacherRatingAverageByUserId(long id);
}