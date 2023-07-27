package kiis.edu.rating.features.subject.base;

import kiis.edu.rating.features.common.SimpleCurdController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/subject")
public class SubjectController extends SimpleCurdController<SubjectEntity> {

    @Autowired
    public SubjectController(SubjectService service) {
        super(service);
    }
}
