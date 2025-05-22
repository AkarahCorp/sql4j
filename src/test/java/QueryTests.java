import net.akarah.sql4j.instruction.Insert;
import net.akarah.sql4j.instruction.Select;
import net.akarah.sql4j.value.expr.Expressions;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QueryTests {
    @Test
    @Order(-1)
    public void testInsertion() {
        try(var result = Insert.into(TestHelpers.PLAYERS_TABLE)
                .withValue(TestHelpers.PLAYER_NAME, Expressions.of("Endistic"))
                .withValue(TestHelpers.PLAYER_AGE, Expressions.of(17))
                .evaluate(TestHelpers.DATABASE)) {
        }
    }

    @Test
    public void testSelection() {
        try(var result =
                    Select.on(TestHelpers.PLAYER_NAME)
                            .from(TestHelpers.PLAYERS_TABLE)
                            .evaluate(TestHelpers.DATABASE)) {

            var row = result.next();
            assert row.isPresent();
            assert row.get().equals("Endistic");
        }

    }

    @Test
    public void testAddition() {
        try(var result =
                Select.on(Expressions.of(1).add(Expressions.of(1)))
                    .evaluate(TestHelpers.DATABASE)) {

            var row = result.next();
            assert row.isPresent();
            assert row.get() == 2;
        }
    }
}
