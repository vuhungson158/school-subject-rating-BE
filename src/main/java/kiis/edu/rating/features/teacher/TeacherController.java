package kiis.edu.rating.features.teacher;

import kiis.edu.rating.features.subject.SubjectEntity;
import kiis.edu.rating.features.teacher.rating.TeacherRatingEntity;
import kiis.edu.rating.features.teacher.rating.TeacherRatingRepository;
import kiis.edu.rating.features.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static kiis.edu.rating.helper.Constant.PATH;

@RestController
@AllArgsConstructor
@RequestMapping(path = PATH + "/teacher")
public class TeacherController {
    private final String RATING_PATH = "/rating";
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final TeacherRatingRepository teacherRatingRepository;

    @GetMapping("/{id}")
    public TeacherWithAverageScore getById(@PathVariable long id) {
        Optional<TeacherEntity> optionalTeacher = teacherRepository.findById(id);
        if (!optionalTeacher.isPresent())
            throw new IllegalArgumentException("No teacher with id : " + id);
        List<TeacherRatingEntity> allRating = getRatingsByTeacherId(id);
        return new TeacherWithAverageScore(optionalTeacher.get(), getAverageScore(allRating));
    }

    @GetMapping("")
    public List<TeacherEntity> getAll() {
        return teacherRepository.findAllByDisable(false);
    }

    @PostMapping("")
    public boolean create(@RequestBody TeacherEntity teacherEntity) {
        teacherEntity.makeSureBaseEntityEmpty();
        teacherRepository.save(teacherEntity);
        return true;
    }

    @PutMapping("/{id}")
    public boolean update(@PathVariable long id, @RequestBody TeacherEntity teacherEntity) {
        if (!teacherRepository.findById(id).isPresent())
            throw new IllegalArgumentException("Teacher not exist");
        teacherEntity.makeSureBaseEntityEmpty();
        teacherEntity.id = id;
        teacherRepository.save(teacherEntity);
        return true;
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable long id) {
        Optional<TeacherEntity> optionalTeacher = teacherRepository.findById(id);
        if (!optionalTeacher.isPresent()) throw new IllegalArgumentException("No Teacher with Id: " + id);
        TeacherEntity teacherEntity = optionalTeacher.get();
        teacherEntity.disable = true;
        teacherRepository.save(teacherEntity);
        return true;
    }

    @GetMapping(RATING_PATH + "/{id}")
    public TeacherRatingEntity getOneRatingById(@PathVariable long id) {
        Optional<TeacherRatingEntity> optionalRating = teacherRatingRepository.findById(id);
        if (!optionalRating.isPresent())
            throw new IllegalArgumentException("No rating with id : " + id);
        return optionalRating.get();
    }

    @GetMapping(RATING_PATH + "/teacherId/{id}")
    public List<TeacherRatingEntity> getRatingsByTeacherId(@PathVariable("id") long teacherId) {
        return teacherRatingRepository.findAllByTeacherId(teacherId);
    }

    @GetMapping(RATING_PATH + "/userId/{id}")
    public List<TeacherRatingEntity> getRatingsByUserId(@PathVariable("id") long userId) {
        return teacherRatingRepository.findAllByUserId(userId);
    }

    @GetMapping(RATING_PATH + "/userId/average/{id}")
    public AverageScore getRatingsAverageByUserId(@PathVariable("id") long userId) {
        List<TeacherRatingEntity> allRating = teacherRatingRepository.findAllByUserId(userId);
        return getAverageScore(allRating);
    }

    @PostMapping(RATING_PATH + "")
    public boolean createRating(@RequestBody @Valid TeacherRatingEntity teacherRatingEntity) {
        if (!teacherRepository.findById(teacherRatingEntity.teacherId).isPresent())
            throw new IllegalArgumentException("No Teacher with id : " + teacherRatingEntity.teacherId);
        if (!userRepository.findById(teacherRatingEntity.userId).isPresent())
            throw new IllegalArgumentException("No User with id : " + teacherRatingEntity.userId);
        if (teacherRatingRepository.findByTeacherIdAndUserId(teacherRatingEntity.teacherId, teacherRatingEntity.userId).isPresent())
            throw new IllegalArgumentException("This User has already rated this Teacher");
        teacherRatingEntity.makeSureBaseEntityEmpty();
        teacherRatingRepository.save(teacherRatingEntity);
        return true;
    }

    @PutMapping(RATING_PATH + "/{id}")
    public boolean updateRating(@PathVariable long id, @RequestBody @Valid TeacherRatingEntity teacherRatingEntity) {
        if (!teacherRatingRepository.findById(id).isPresent())
            throw new IllegalArgumentException("No Rating with Id: " + id);
        teacherRatingEntity.makeSureBaseEntityEmpty();
        teacherRatingEntity.id = id;
        teacherRatingRepository.save(teacherRatingEntity);
        return true;
    }

    @DeleteMapping(RATING_PATH + "/{id}")
    public boolean deleteRating(@PathVariable long id) {
        teacherRatingRepository.deleteById(id);
        return true;
    }

    private AverageScore getAverageScore(List<TeacherRatingEntity> allRating) {
        int enthusiasm = 0, friendly = 0, nonConservatism = 0, erudition = 0, pedagogicalLevel = 0;
        for (TeacherRatingEntity rating : allRating) {
            enthusiasm += rating.enthusiasm;
            friendly += rating.friendly;
            nonConservatism += rating.nonConservatism;
            erudition += rating.erudition;
            pedagogicalLevel += rating.pedagogicalLevel;
        }
        int listSize = allRating.size();
        enthusiasm = Math.floorDiv(enthusiasm, listSize);
        friendly = Math.floorDiv(friendly, listSize);
        nonConservatism = Math.floorDiv(nonConservatism, listSize);
        erudition = Math.floorDiv(erudition, listSize);
        pedagogicalLevel = Math.floorDiv(pedagogicalLevel, listSize);
        return new AverageScore(enthusiasm, friendly, nonConservatism, erudition, pedagogicalLevel, listSize);
    }

    @AllArgsConstructor
    private static class AverageScore {
        public int enthusiasm, friendly, nonConservatism, erudition, pedagogicalLevel, size;
    }

    @AllArgsConstructor
    private static class TeacherWithAverageScore {
        public TeacherEntity subject;
        public AverageScore averageScore;
    }

}
