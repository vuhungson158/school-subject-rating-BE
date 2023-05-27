package kiis.edu.rating.enums;

import lombok.Getter;

import static kiis.edu.rating.enums.Department.ALL;
import static kiis.edu.rating.enums.Department.MANAGEMENT;

public class SubjectClassification {

    @Getter
    public enum Small {
        HUMANITIES(Middle.GENERAL_EDUCATION, ALL, "人文"),
        SOCIAL(Middle.GENERAL_EDUCATION, ALL, "社会"),
        NATURAL(Middle.GENERAL_EDUCATION, ALL, "自然"),
        SPORTS_HEALTH(Middle.GENERAL_EDUCATION, ALL, "健康"),

        ENGLISH(Middle.LANGUAGE, ALL, "英語"),
        CHINESE(Middle.LANGUAGE, ALL, "中国語"),
        KOREAN(Middle.LANGUAGE, ALL, "韓国語"),
        JAPANESE(Middle.LANGUAGE, ALL, "日本語"),

        PRACTICE_SKILLS(Middle.CAREER_DEVELOPMENT, ALL, "実践"),
        CAREER_DEVELOPMENT(Middle.CAREER_DEVELOPMENT, ALL, "開発"),

        BASIC_SPECIAL_TRAINING(Middle.BASIC_SPECIAL_TRAINING, ALL, "基礎"),

        BASIC_INFORMATION(Middle.SPECIALIZED_BASIS, ALL, "情報"),
        BUSINESS_ACCOUNTING(Middle.SPECIALIZED_BASIS, ALL, "経営・会計"),

        UPGRADE_INFORMATION(Middle.SPECIALIZED_UPGRADE, ALL, "情報"),
        UPGRADE_BUSINESS_ACCOUNTING(Middle.SPECIALIZED_UPGRADE, MANAGEMENT, "経営・会計"),

        NETWORK(Middle.SPECIALIZED_PRACTICAL, Department.NETWORK, "ネットワーク"),
        DATA_SCIENCE(Middle.SPECIALIZED_PRACTICAL, Department.NETWORK, "データサイエンス"),
        ACCOUNTING(Middle.SPECIALIZED_PRACTICAL, MANAGEMENT, "会計"),
        BUSINESS(Middle.SPECIALIZED_PRACTICAL, MANAGEMENT, "経営"),
        INTERNATIONAL_BUSINESS(Middle.SPECIALIZED_PRACTICAL, MANAGEMENT, "国際ビジネス"),

        SEMINAR(Middle.SEMINAR, ALL, "ゼミ"),

        SPECIALIZED_SPECIAL_TRAINING(Middle.SPECIALIZED_SPECIAL_TRAINING, ALL, "専門");

        private final Middle middle;
        private final Department department;
        private final String label;

        Small(Middle middle, Department department, String label) {
            this.middle = middle;
            this.department = department;
            this.label = label;
        }
    }

    @Getter
    public enum Middle {
        GENERAL_EDUCATION(Big.BASIC_GENERAL, 8, "総合教養"),
        LANGUAGE(Big.BASIC_GENERAL, 4, "語学"),
        CAREER_DEVELOPMENT(Big.BASIC_GENERAL, 2, "実践力養成・キャリア開発"),
        BASIC_SPECIAL_TRAINING(Big.BASIC_GENERAL, 0, "特別"),

        SPECIALIZED_BASIS(Big.SPECIALIZED_EDUCATION, 12, "専門基礎"),
        SPECIALIZED_UPGRADE(Big.SPECIALIZED_EDUCATION, 8, "専門発展"),
        SPECIALIZED_PRACTICAL(Big.SPECIALIZED_EDUCATION, 24, "専門応用"),
        SEMINAR(Big.SPECIALIZED_EDUCATION, 0, "演習"),
        SPECIALIZED_SPECIAL_TRAINING(Big.SPECIALIZED_EDUCATION, 0, "特別");

        private final Big big;
        private final int creditNeeded;
        private final String label;

        Middle(Big big, int creditNeeded, String label) {
            this.big = big;
            this.creditNeeded = creditNeeded;
            this.label = label;
        }
    }

    @Getter
    public enum Big {
        BASIC_GENERAL(24, "基礎総合科目"),
        SPECIALIZED_EDUCATION(58, "専門教育科目");

        private final int creditNeeded;
        private final String label;

        Big(int creditNeeded, String label) {
            this.creditNeeded = creditNeeded;
            this.label = label;
        }
    }
}


