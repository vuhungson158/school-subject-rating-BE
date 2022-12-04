package kiis.edu.rating.helper;

import java.time.Instant;

public class Util {
    public static void makeSureBaseEntityEmpty(long id, Instant createAt, Instant updateAt) {
        if (id != 0 || createAt != null || updateAt != null)
            throw new IllegalArgumentException("Make sure id, createAt, updateAt is empty");
    }
}
