package kiis.edu.rating.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum Department {
    MANAGEMENT("経営"), NETWORK("ネットワーク"), ALL("基礎");

    private final String label;

    Department(String label) {
        this.label = label;
    }

    public static List<Department> getValues() {
        return Arrays.stream(Department.values()).filter(value -> value != ALL).collect(Collectors.toList());
    }

    public static Map<Department, Integer> frame() {
        final Map<Department, Integer> map = new HashMap<>();
        getValues().forEach(department -> {
            map.put(department, 0);
        });
        return map;
    }
}
