package kiis.edu.rating.features.subject.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRatingRepository extends JpaRepository<SubjectRatingEntity, Long> {
    List<SubjectRatingEntity> findAllBySubjectId(long subjectId);
    List<SubjectRatingEntity> findAllByUserId(long userId);
    void deleteBySubjectId(long subjectId);
    Optional<SubjectRatingEntity> findBySubjectIdAndUserId(long subjectId, long userId);
}
