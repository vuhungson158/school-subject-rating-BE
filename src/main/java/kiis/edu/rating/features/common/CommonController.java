package kiis.edu.rating.features.common;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unused")
@RestController
@AllArgsConstructor
@RequestMapping(path = "/common")
public class CommonController {
    private final CommonRepository commonRepository;

    @GetMapping("/statistics")
    public StatisticsEntity getStatistics() {
        return commonRepository.findStatistics();
    }
}
