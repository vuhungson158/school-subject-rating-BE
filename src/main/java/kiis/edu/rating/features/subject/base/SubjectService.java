package kiis.edu.rating.features.subject.base;

import kiis.edu.rating.common.AdvanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectService extends AdvanceService<SubjectEntity> {

    @Autowired
    public SubjectService(SubjectRepository repository) {
        super(repository);
    }
}
