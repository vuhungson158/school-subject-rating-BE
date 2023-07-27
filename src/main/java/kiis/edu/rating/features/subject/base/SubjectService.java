package kiis.edu.rating.features.subject.base;

import kiis.edu.rating.features.common.SimpleCurdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectService extends SimpleCurdService<SubjectEntity> {

    @Autowired
    public SubjectService(SubjectRepository repository) {
        super(repository);
    }
}
