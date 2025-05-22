package net.akarah.sql4j;

import java.util.function.Supplier;

public class ExceptionUtils {
    public interface Throws<T, E extends Throwable> {
        T apply() throws E;
    }

    public static <T, E extends Throwable> T sneakyThrows(Throws<T, E> value) {
       try {
           return value.apply();
       } catch (Throwable e) {
           throw new RuntimeException(e);
       }
    }
}
