package net.akarah.sql4j.value;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.akarah.sql4j.value.tuple.Tuple;
import org.postgresql.util.PGobject;

import java.util.List;

public final class Conversions {
    static Gson GSON = new Gson();

    // this function is safe since it's invariants are checked by the type system in respective instructions
    @SuppressWarnings("unchecked")
    public static <T> T getValueFromList(List<Object> objects) {
        return switch (objects.size()) {
            case 1 -> convertToSql(objects.getFirst());
            case 2 -> (T) new Tuple.Of2<>(
                    convertToSql(objects.getFirst()),
                    convertToSql(objects.get(1))
            );
            case 3 -> (T) new Tuple.Of3<>(
                    convertToSql(objects.getFirst()),
                    convertToSql(objects.get(1)),
                    convertToSql(objects.get(2))
            );
            case 4 -> (T) new Tuple.Of4<>(
                    convertToSql(objects.getFirst()),
                    convertToSql(objects.get(1)),
                    convertToSql(objects.get(2)),
                    convertToSql(objects.get(3))
            );
            default -> throw new RuntimeException("not possible, too many columns :P");
        };
    }

    // this function is safe since it's invariants are checked by the generic type system
    @SuppressWarnings("unchecked")
    public static <T> T convertToSql(Object object) {
        return switch (object) {
            case PGobject postgresObject -> switch (postgresObject.getType()) {
                case "json", "jsonb" -> (T) GSON.fromJson(postgresObject.getValue(), JsonElement.class);
                default -> throw new RuntimeException("unknown psql type " + postgresObject.getType());
            };
            case null -> (T) null;
            default -> (T) object;
        };
    }
}
