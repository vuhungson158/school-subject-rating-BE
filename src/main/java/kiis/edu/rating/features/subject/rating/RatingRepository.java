package kiis.edu.rating.features.subject.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Long> {
    List<RatingEntity> findAllBySubjectId(long subjectId);
    List<RatingEntity> findAllByUserId(long userId);
    void deleteBySubjectId(long subjectId);
    Optional<RatingEntity> findBySubjectIdAndUserId(long subjectId, long userId);
}
