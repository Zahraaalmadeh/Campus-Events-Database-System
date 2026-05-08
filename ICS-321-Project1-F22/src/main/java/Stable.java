import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Stable {
    String stable="create table IF NOT EXISTS Stable \n" +
            " (stableId varchar(15) not null,\n" +
            " stableName varchar(30),\n" +
            " location varchar(30),\n" +
            " colors varchar(20),\n" +
            " primary key (stableId));";
    String[] insertStatements = {
            "insert IGNORE into Stable values ('stable1', 'Zobair Farm', 'Riyadh', 'orange')",
            "insert IGNORE into Stable values ('stable2', 'Zayed Farm', 'Dubai', 'kiwi')",
            "insert IGNORE into Stable values ('stable3', 'Zahra Farm', 'Jeddah', 'cinnamon')",
            "insert IGNORE into Stable values ('stable4', 'Sunny Stables', 'Jubail', 'lemon')",
            "insert IGNORE into Stable values ('stable5', 'Ajman Stables', 'Ajman', 'lemon')",
            "insert IGNORE into Stable values ('stable6', 'Dubai Stables', 'Dubai', 'bright blue')"
    };
    public Statement CreateTable(Connection reservationConn) throws SQLException {
        Statement createTable= reservationConn.createStatement();
        createTable.execute(stable);
        for (String insertSql : insertStatements) {
            createTable.executeUpdate(insertSql);
        }

        return createTable;
    }
}
