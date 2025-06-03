package net.akarah.sql4j.value.tuple;

public interface Tuple {
    record Of2<T1, T2>(T1 a, T2 b) implements Tuple { }
    record Of3<T1, T2, T3>(T1 a, T2 b, T3 c) implements Tuple { }
    record Of4<T1, T2, T3, T4>(T1 a, T2 b, T3 c, T4 d) implements Tuple { }
}
