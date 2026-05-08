import java.sql.*;

public class Old_info {
        String Old_infoCreate="create table IF NOT EXISTS old_info " +
                " (horseId varchar(15) not null," +
                " horseName varchar(15) not null," +
                " age int," +
                " gender char," +
                " registration integer not null," +
                " stableId varchar(30) not null," +
                " primary key(horseId));";
    public Statement CreateTable(Connection reservationConn) throws SQLException {
        Statement CreateTable= reservationConn.createStatement();
        CreateTable.execute(Old_infoCreate);
        return CreateTable;
    }
    public void CreateTrigger(Connection con) {
        try (Statement stmt = con.createStatement()) {
            stmt.execute("DROP TRIGGER IF EXISTS after_horse_delete");

            String createTrig =
                    "CREATE TRIGGER after_horse_delete " +
                            "AFTER DELETE ON Horse " +
                            "FOR EACH ROW " +
                            "BEGIN " +
                            "    INSERT INTO old_info (horseId, horseName, age, gender, registration, stableId) " +
                            "    VALUES (OLD.horseId, OLD.horseName, OLD.age, OLD.gender, OLD.registration, OLD.stableId); " +
                            "END";

            stmt.execute(createTrig);
        } catch (SQLException e) {
            System.out.println("Error creating trigger: " + e.getMessage());
        }
    }
}
