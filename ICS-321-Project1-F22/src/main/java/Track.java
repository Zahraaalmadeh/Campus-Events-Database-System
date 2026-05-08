import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Track {
    String track="create table IF NOT EXISTS Track \n" +
            "   (trackName varchar(30) not null, \n" +
            "    location   varchar(30), \n" +
            "    length     integer, \n" +
            "    primary key(trackName));";
    String[] insertStatements = {
            "insert IGNORE into Track values('Doha', 'QT', 20)",
            "insert IGNORE into Track values('Jubail', 'SA', 15)",
            "insert IGNORE into Track values('Yanbu', 'SA', 18)",
            "insert IGNORE into Track values('Dubai', 'UE', 17)",
            "insert IGNORE into Track values('Jeddah', 'SA', 19)",
            "insert IGNORE into Track values('Bahrain', 'BH', 18)",
            "insert IGNORE into Track values('Sharjah', 'UE', 20)",
            "insert IGNORE into Track values('Riyadh', 'SA', 22)",
            "insert IGNORE into Track values('Dhahran', 'SA', 20)"
    };
    public Statement CreateTable(Connection reservationConn) throws SQLException {
        Statement createTable= reservationConn.createStatement();
        createTable.execute(track);
        for (String insertSql : insertStatements) {
            createTable.executeUpdate(insertSql);
        }
        return createTable;
    }
}
