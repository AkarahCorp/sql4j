package net.akarah.sql4j.value;

import java.util.List;
import java.util.stream.Gatherer;

public interface Tuple {
    List<Object> values();

    record Of2<T1, T2>(T1 a, T2 b) implements Tuple {
        @Override
        public List<Object> values() {
            return List.of(a, b);
        }
    }

    record Of3<T1, T2, T3>(T1 a, T2 b, T3 c) implements Tuple {
        @Override
        public List<Object> values() {
            return List.of(a, b, c);
        }
    }
}
