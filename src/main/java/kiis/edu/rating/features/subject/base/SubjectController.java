package kiis.edu.rating.features.subject.base;

import kiis.edu.rating.aop.AllowFeature;
import kiis.edu.rating.common.AdvanceController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kiis.edu.rating.features.user.UserRole.Feature.SUBJECT;

@RequestMapping(path = "/subject")
@RestController
@AllowFeature(SUBJECT)
public class SubjectController extends AdvanceController<SubjectEntity> {

    @Autowired
    public SubjectController(SubjectService subjectService) {
        super(subjectService);
    }
}
