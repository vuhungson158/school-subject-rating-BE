package kiis.edu.rating.enums;

import lombok.Getter;

@Getter
public enum Department {
    MANAGEMENT("経営"), NETWORK("ネットワーク"), ALL("基礎");

    private final String label;

    Department(String label) {
        this.label = label;
    }
}
