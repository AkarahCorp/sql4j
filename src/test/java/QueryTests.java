import net.akarah.sql4j.instruction.Insert;
import net.akarah.sql4j.instruction.Select;
import net.akarah.sql4j.value.expr.Expressions;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class QueryTests {
    @Test
    public void _testInsertion() {
        Insert.into(TestHelpers.PLAYERS_TABLE)
                .withValue(TestHelpers.PLAYER_NAME, Expressions.of("Endistic"))
                .withValue(TestHelpers.PLAYER_AGE, Expressions.of(17))
                .evaluate(TestHelpers.DATABASE)
                .close();

        Insert.into(TestHelpers.PLAYERS_TABLE)
                .withValue(TestHelpers.PLAYER_NAME, Expressions.of("TheYoung"))
                .withValue(TestHelpers.PLAYER_AGE, Expressions.of(15))
                .evaluate(TestHelpers.DATABASE)
                .close();

        Insert.into(TestHelpers.PLAYERS_TABLE)
                .withValue(TestHelpers.PLAYER_NAME, Expressions.of("TheAdult"))
                .withValue(TestHelpers.PLAYER_AGE, Expressions.of(19))
                .evaluate(TestHelpers.DATABASE)
                .close();
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
    public void selectionWhere() {
        try(var result =
                    Select.on(TestHelpers.PLAYER_NAME, TestHelpers.PLAYER_AGE)
                            .from(TestHelpers.PLAYERS_TABLE)
                            .where(Expressions.of(TestHelpers.PLAYER_AGE).greaterThan(Expressions.of(18)))
                            .evaluate(TestHelpers.DATABASE)) {

            assert result.next().isPresent();
            assert result.next().isEmpty();
        }
    }

    @Test
    public void selectionOrderedBy() {
        try(var result =
                    Select.on(TestHelpers.PLAYER_NAME, TestHelpers.PLAYER_AGE)
                            .from(TestHelpers.PLAYERS_TABLE)
                            .orderByAscending(TestHelpers.PLAYER_AGE)
                            .evaluate(TestHelpers.DATABASE)) {


            var first = result.next();
            assert first.isPresent();
            assert first.get().a().equals("TheYoung");
        }
    }

    @Test
    public void selectionOrderedByLimits() {
        try(var result =
                    Select.on(TestHelpers.PLAYER_NAME, TestHelpers.PLAYER_AGE)
                            .from(TestHelpers.PLAYERS_TABLE)
                            .orderByDescending(TestHelpers.PLAYER_AGE)
                            .limit(Expressions.of(1))
                            .evaluate(TestHelpers.DATABASE)) {

            var first = result.next();
            assert first.isPresent();
            assert first.get().a().equals("TheAdult");

            assert result.next().isEmpty();
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
