package kiis.edu.rating.features.subject;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SubjectController {
    private final SubjectRepository subjectRepository;
}
