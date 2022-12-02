package kiis.edu.rating.features.rating;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RatingController {
    private final RatingRepository ratingRepository;
}
