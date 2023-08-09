package kiis.edu.rating.features.subject.base;

import kiis.edu.rating.aop.AllowFeature;
import kiis.edu.rating.aop.AllowMethod;
import kiis.edu.rating.common.AdvanceController;
import kiis.edu.rating.features.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kiis.edu.rating.features.user.UserRole.Feature.SUBJECT;
import static kiis.edu.rating.features.user.UserRole.Method.FIND_BY_PAGEABLE;

@RequestMapping(path = "/subject")
@RestController
@AllowFeature(SUBJECT)
public class SubjectController extends AdvanceController<SubjectEntity> {
    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService) {
        super(subjectService);
        this.subjectService = subjectService;
    }
}
