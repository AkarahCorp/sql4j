import net.akarah.sql4j.instruction.Insert;
import net.akarah.sql4j.instruction.Select;
import net.akarah.sql4j.instruction.Update;
import net.akarah.sql4j.value.expr.Functions;
import net.akarah.sql4j.value.expr.Value;
import net.akarah.sql4j.value.expr.Values;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class QueryTests {
    static {
        System.out.println("loaded Query Tests.");
    }

    @Test
    public void _testInsertion() {
        Insert.into(TestHelpers.PLAYERS_TABLE)
                .withValue(TestHelpers.PLAYER_NAME, Values.of("Endistic"))
                .withValue(TestHelpers.PLAYER_AGE, Values.of(17))
                .evaluate(TestHelpers.DATABASE)
                .close();

        Insert.into(TestHelpers.PLAYERS_TABLE)
                .withValue(TestHelpers.PLAYER_NAME, Values.of("TheYoung"))
                .withValue(TestHelpers.PLAYER_AGE, Values.of(15))
                .evaluate(TestHelpers.DATABASE)
                .close();

        Insert.into(TestHelpers.PLAYERS_TABLE)
                .withValue(TestHelpers.PLAYER_NAME, Values.of("TheAdult"))
                .withValue(TestHelpers.PLAYER_AGE, Values.of(19))
                .evaluate(TestHelpers.DATABASE)
                .close();
    }

    @Test
    public void _testUpdating() {
        Insert.into(TestHelpers.PLAYERS_TABLE)
                .withValue(TestHelpers.PLAYER_NAME, Values.of("TheUpdating"))
                .withValue(TestHelpers.PLAYER_AGE, Values.of(1550))
                .evaluate(TestHelpers.DATABASE)
                .close();

        Update.table(TestHelpers.PLAYERS_TABLE)
                .setColumn(TestHelpers.PLAYER_NAME, Values.of("TheUpdated"))
                .where(TestHelpers.PLAYER_NAME.equals(Values.of("TheUpdating")))
                .evaluate(TestHelpers.DATABASE)
                .close();

        try(var result = Select.on(TestHelpers.PLAYER_NAME)
                .from(TestHelpers.PLAYERS_TABLE)
                .where(TestHelpers.PLAYER_NAME.equals(Values.of("TheUpdated")))
                .evaluate(TestHelpers.DATABASE)) {

            var row = result.next();
            assert row.isPresent();
            assert row.get().equals("TheUpdated");
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
    public void selectionWhere() {
        try(var result =
                    Select.on(TestHelpers.PLAYER_NAME, TestHelpers.PLAYER_AGE)
                            .from(TestHelpers.PLAYERS_TABLE)
                            .where(Values.of(TestHelpers.PLAYER_AGE).greaterThan(Values.of(18)))
                            .where(TestHelpers.PLAYER_NAME.notEquals(Values.of("TheUpdated")))
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
                            .where(TestHelpers.PLAYER_NAME.notEquals(Values.of("TheUpdated")))
                            .orderByDescending(TestHelpers.PLAYER_AGE)
                            .limit(Values.of(1L))
                            .evaluate(TestHelpers.DATABASE)) {

            var first = result.next();
            assert first.isPresent();
            assert first.get().a().equals("TheAdult");

            assert result.next().isEmpty();
        }
    }

    @Test
    public void selectionSum() {
        try(var result =
                    Select.on(Functions.sum(TestHelpers.PLAYER_AGE))
                            .from(TestHelpers.PLAYERS_TABLE)
                            .where(TestHelpers.PLAYER_NAME.notEquals(Values.of("TheUpdated")))
                            .evaluate(TestHelpers.DATABASE)) {

            var first = result.next();
            assert first.isPresent();
            assert first.get() == 17 + 15 + 19;

            assert result.next().isEmpty();
        }
    }

    @Test
    public void testAddition() {
        try(var result =
                Select.on(Values.of(1).add(Values.of(1)))
                    .evaluate(TestHelpers.DATABASE)) {

            var row = result.next();
            assert row.isPresent();
            assert row.get() == 2;
        }
    }
}
