package kiis.edu.rating.features.teacher.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRatingRepository extends JpaRepository<TeacherRatingEntity, Long> {
    List<TeacherRatingEntity> findAllByTeacherId(long teacherId);
    List<TeacherRatingEntity> findAllByUserId(long userId);
    Optional<TeacherRatingEntity> existsByTeacherIdAndUserId(long teacherId, long userId);
}
