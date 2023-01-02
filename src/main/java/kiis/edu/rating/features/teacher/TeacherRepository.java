package kiis.edu.rating.features.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<TeacherEntity, Long> {
    List<TeacherEntity> findAllByDisable(boolean disable);
}

@Repository
interface TeacherWithAvgRatingRepository extends JpaRepository<TeacherWithAvgRating, Long> {

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
    TeacherWithAvgRating findTeacherEntityWithRatingById(long id);
}
