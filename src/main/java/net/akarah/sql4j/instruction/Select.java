package net.akarah.sql4j.instruction;

import net.akarah.sql4j.table.Table;
import net.akarah.sql4j.value.Expression;
import net.akarah.sql4j.value.IntoExpression;

public class Select<T> implements Instruction<T> {
    Expression<T> baseExpression;
    Expression<Table> table;



    public static <T> Select<T> on(IntoExpression<T> baseExpression) {
        var sel = new Select<T>();
        sel.baseExpression = baseExpression.intoExpression();
        return sel;
    }

    public Select<T> from(IntoExpression<Table> table) {
        this.table = table.intoExpression();
        return this;
    }

    @Override
    public String toSql() {
        var sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(baseExpression.toSql());
        if(this.table != null) {
            sb.append(" FROM ");
            sb.append(this.table.toSql());
        }
        sb.append(";");
        return sb.toString();
    }
}
