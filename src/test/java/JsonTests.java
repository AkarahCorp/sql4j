import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.akarah.sql4j.instruction.Insert;
import net.akarah.sql4j.instruction.Select;
import net.akarah.sql4j.table.Column;
import net.akarah.sql4j.table.Table;
import net.akarah.sql4j.value.Type;
import net.akarah.sql4j.value.expr.Values;
import org.junit.jupiter.api.Test;

public class JsonTests {
    public static Column<JsonElement> JSON_COLUMN = Column.of("json_column", Type.jsonb());

    public static Table JSON_TABLE = Table.of(TestHelpers.DATABASE, "json_table")
            .withColumn(JSON_COLUMN)
            .dropIfExists()
            .createIfNotExists();

    static {
        System.out.println("loaded Json tests");
        Insert.into(JSON_TABLE)
                .withValue(JSON_COLUMN, Values.of(new JsonObject()))
                .evaluate(TestHelpers.DATABASE)
                .close();
    }

    @Test
    public void checkJson() {
        try(var result = Select.on(JSON_COLUMN)
                .from(JSON_TABLE)
                .where(JSON_COLUMN.equals(Values.of(new JsonObject())))
                .evaluate(TestHelpers.DATABASE)) {
            var row = result.next();
            assert row.isPresent();
            assert row.get().equals(new JsonObject());
        }
    }

}
