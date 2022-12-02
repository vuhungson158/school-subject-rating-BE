package kiis.edu.rating.features.teacher;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TeacherController {
    private final TeacherRepository teacherRepository;
}
