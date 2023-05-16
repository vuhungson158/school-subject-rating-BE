package kiis.edu.rating.enums;

import lombok.Getter;

public class SubjectClassification {

    @Getter
    public enum Small {
        HUMANITIES(Middle.GENERAL_EDUCATION),
        SOCIAL(Middle.GENERAL_EDUCATION),
        NATURAL(Middle.GENERAL_EDUCATION),
        SPORTS_HEALTH(Middle.GENERAL_EDUCATION),

        ENGLISH(Middle.LANGUAGE),
        CHINESE(Middle.LANGUAGE),
        KOREAN(Middle.LANGUAGE),
        JAPANESE(Middle.LANGUAGE),

        PRACTICE_SKILLS(Middle.CAREER_DEVELOPMENT),
        CAREER_DEVELOPMENT(Middle.CAREER_DEVELOPMENT),

        BASIC_SPECIAL_TRAINING(Middle.BASIC_SPECIAL_TRAINING),

        BASIC_INFORMATION(Middle.SPECIALIZED_BASIS),
        BUSINESS_ACCOUNTING(Middle.SPECIALIZED_BASIS),

        UPGRADE_INFORMATION(Middle.SPECIALIZED_UPGRADE),

        NETWORK(Middle.SPECIALIZED_PRACTICAL),
        DATA_SCIENCE(Middle.SPECIALIZED_PRACTICAL),
        ACCOUNTING(Middle.SPECIALIZED_PRACTICAL),
        BUSINESS(Middle.SPECIALIZED_PRACTICAL),
        INTERNATIONAL_BUSINESS(Middle.SPECIALIZED_PRACTICAL),

        SEMINAR(Middle.SEMINAR),

        SPECIALIZED_SPECIAL_TRAINING(Middle.SPECIALIZED_SPECIAL_TRAINING);

        private final Middle middle;
        Small(Middle middle) {
            this.middle = middle;
        }
    }

    @Getter
    public enum Middle {
        GENERAL_EDUCATION(Big.BASIC_GENERAL),
        LANGUAGE(Big.BASIC_GENERAL),
        CAREER_DEVELOPMENT(Big.BASIC_GENERAL),
        BASIC_SPECIAL_TRAINING(Big.BASIC_GENERAL),

        SPECIALIZED_BASIS(Big.SPECIALIZED_EDUCATION),
        SPECIALIZED_UPGRADE(Big.SPECIALIZED_EDUCATION),
        SPECIALIZED_PRACTICAL(Big.SPECIALIZED_EDUCATION),
        SEMINAR(Big.SPECIALIZED_EDUCATION),
        SPECIALIZED_SPECIAL_TRAINING(Big.SPECIALIZED_EDUCATION);

        private final Big big;
        Middle(Big big) {
            this.big = big;
        }
    }

    public enum Big {
        BASIC_GENERAL, SPECIALIZED_EDUCATION
    }
}


