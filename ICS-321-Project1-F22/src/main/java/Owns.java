import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Owns {
    String owns="create table IF NOT EXISTS owns   (ownerId varchar(15) not null, \n" +
            "    horseId varchar(15) not null, \n" +
            "    primary key(ownerId, horseId), \n" +
            "    foreign key(ownerId) references Owner(ownerId) ON DELETE CASCADE, \n" +
            "    foreign key(horseId) references Horse(horseId) ON DELETE CASCADE ON UPDATE CASCADE);";
    String[] insertStatements = {
            "insert IGNORE into Owns values('owner14', 'horse1')",
            "insert IGNORE into Owns values('owner3', 'horse2')",
            "insert IGNORE into Owns values('owner2', 'horse3')",
            "insert IGNORE into Owns values('owner2', 'horse4')",
            "insert IGNORE into Owns values('owner1', 'horse5')",
            "insert IGNORE into Owns values('owner12', 'horse5')",
            "insert IGNORE into Owns values('owner14', 'horse5')",
            "insert IGNORE into Owns values('owner1', 'horse6')",
            "insert IGNORE into Owns values('owner5', 'horse6')",
            "insert IGNORE into Owns values('owner20', 'horse7')",
            "insert IGNORE into Owns values('owner19', 'horse8')",
            "insert IGNORE into Owns values('owner2', 'horse9')",
            "insert IGNORE into Owns values('owner18', 'horse10')",
            "insert IGNORE into Owns values('owner3', 'horse10')",
            "insert IGNORE into Owns values('owner4', 'horse11')",
            "insert IGNORE into Owns values('owner16', 'horse12')",
            "insert IGNORE into Owns values('owner17', 'horse13')",
            "insert IGNORE into Owns values('owner15', 'horse14')",
            "insert IGNORE into Owns values('owner15', 'horse15')",
            "insert IGNORE into Owns values('owner20', 'horse16')",
            "insert IGNORE into Owns values('owner4', 'horse17')",
            "insert IGNORE into Owns values('owner6', 'horse19')",
            "insert IGNORE into Owns values('owner12', 'horse20')",
            "insert IGNORE into Owns values('owner7', 'horse21')",
            "insert IGNORE into Owns values('owner7', 'horse22')",
            "insert IGNORE into Owns values('owner10', 'horse23')",
            "insert IGNORE into Owns values('owner12', 'horse24')",
            "insert IGNORE into Owns values('owner13', 'horse25')",
            "insert IGNORE into Owns values('owner2', 'horse26')",
            "insert IGNORE into Owns values('owner9', 'horse23')",
            "insert IGNORE into Owns values('owner8', 'horse18')"
    };
    public Statement CreateTable(Connection reservationConn) throws SQLException {
        Statement createTable= reservationConn.createStatement();
        createTable.execute(owns);
        for (String insertSql : insertStatements) {
            createTable.executeUpdate(insertSql);
        }
        return createTable;
    }
}
