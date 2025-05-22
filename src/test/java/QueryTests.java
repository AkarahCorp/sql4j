import net.akarah.sql4j.instruction.Select;
import net.akarah.sql4j.value.Expression;
import org.junit.jupiter.api.Test;

public class QueryTests {
    @Test
    public void testSelection() {
        try(var result =
                    Select.on(Expression.of(TestHelpers.PLAYER_NAME))
                            .from(TestHelpers.PLAYERS_TABLE)
                            .evaluate(TestHelpers.DATABASE)) {

            while(true) {
                var row = result.next();
                if(row.isEmpty()) {
                    System.out.println("empty, ending.");
                    return;
                }
                System.out.println(row.get());
            }
        }

    }

    @Test
    public void testAddition() {
        try(var result =
                Select.on(Expression.of(1).add(Expression.of(1)))
                    .evaluate(TestHelpers.DATABASE)) {
            result.next().ifPresent(value -> {
                assert value == 2;
            });
        }

    }
}
