import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;

public class RaceResults {
    String raceResults="create table IF NOT EXISTS RaceResults \n" +
            "   (raceId varchar(15) not null, \n" +
            "    horseId varchar(15) not null, \n" +
            "    results  varchar(15), \n" +
            "    prize    float(10,2), \n" +
            "    primary key (raceId, horseId), \n" +
            "    foreign key(raceId) references Race(raceId), \n" +
            "    foreign key(horseId) references Horse(horseId) ON DELETE CASCADE);";
    String[] insertStatements = {
            "insert IGNORE into RaceResults values('race1', 'horse3', 'first', 500000)",
            "insert IGNORE into RaceResults values('race1', 'horse11', 'second', 200000)",
            "insert IGNORE into RaceResults values('race1', 'horse15', 'third', 500000)",
            "insert IGNORE into RaceResults values('race2', 'horse6', 'first', 100000)",
            "insert IGNORE into RaceResults values('race2', 'horse2', 'second', 50000)",
            "insert IGNORE into RaceResults values('race2', 'horse20', 'third', 20000)",
            "insert IGNORE into RaceResults values('race3', 'horse22', 'first', 70000)",
            "insert IGNORE into RaceResults values('race3', 'horse5', 'second', 50000)",
            "insert IGNORE into RaceResults values('race3', 'horse1', 'third', 20000)",
            "insert IGNORE into RaceResults values('race4', 'horse19', 'first', 50000)",
            "insert IGNORE into RaceResults values('race4', 'horse18', 'no show', 0)",
            "insert IGNORE into RaceResults values('race4', 'horse14', 'no show', 0)",
            "insert IGNORE into RaceResults values('race6', 'horse25', 'first', 5000)",
            "insert IGNORE into RaceResults values('race7', 'horse7', 'second', 2000)",
            "insert IGNORE into RaceResults values('race9', 'horse11', 'last', 0)",
            "insert IGNORE into RaceResults values('race10', 'horse18', 'fourth', 500)",
            "insert IGNORE into RaceResults values('race11', 'horse12', 'first', 50000)",
            "insert IGNORE into RaceResults values('race11', 'horse17', 'second', 25000)",
            "insert IGNORE into RaceResults values('race11', 'horse21', 'fourth', 10000)",
            "insert IGNORE into RaceResults values('race12', 'horse14', 'first', 6000)",
            "insert IGNORE into RaceResults values('race12', 'horse18', 'second', 5000)",
            "insert IGNORE into RaceResults values('race13', 'horse25', 'first', 100000)",
            "insert IGNORE into RaceResults values('race13', 'horse4', 'second', 50000)",
            "insert IGNORE into RaceResults values('race13', 'horse12', 'third', 30000)",
            "insert IGNORE into RaceResults values('race14', 'horse23', 'first', 25000)",
            "insert IGNORE into RaceResults values('race14', 'horse26', 'second', 20000)",
            "insert IGNORE into RaceResults values('race15', 'horse11', 'second', 10000)",
            "insert IGNORE into RaceResults values('race15', 'horse24', 'third', 8000)",
            "insert IGNORE into RaceResults values('race16', 'horse10', 'second', 5000)",
            "insert IGNORE into RaceResults values('race16', 'horse14', 'third', 4000)",
            "insert IGNORE into RaceResults values('race17', 'horse7', 'first', 15000)",
            "insert IGNORE into RaceResults values('race17', 'horse10', 'second', 1100)",
            "insert IGNORE into RaceResults values('race18', 'horse6', 'first', 70000)",
            "insert IGNORE into RaceResults values('race19', 'horse22', 'first', 1000000)",
            "insert IGNORE into RaceResults values('race19', 'horse1', 'second', 80000)",
            "insert IGNORE into RaceResults values('race19', 'horse8', 'third', 60000)",
            "insert IGNORE into RaceResults values('race20', 'horse23', 'first', 1500)",
            "insert IGNORE into RaceResults values('race20', 'horse14', 'second', 1000)",
            "insert IGNORE into RaceResults values('race20', 'horse26', 'third', 800)",
            "insert IGNORE into RaceResults values('race20', 'horse10', 'fourth', 500)",
            "insert IGNORE into RaceResults values('race21', 'horse24', 'first', 70000)",
            "insert IGNORE into RaceResults values('race21', 'horse15', 'second', 55000)",
            "insert IGNORE into RaceResults values('race21', 'horse3', 'third', 40000)",
            "insert IGNORE into RaceResults values('race22', 'horse18', 'first', 10000)",
            "insert IGNORE into RaceResults values('race22', 'horse19', 'second', 8000)",
            "insert IGNORE into RaceResults values('race23', 'horse25', 'first', 150000)",
            "insert IGNORE into RaceResults values('race24', 'horse7', 'first', 10000)",
            "insert IGNORE into RaceResults values('race25', 'horse10', 'second', 8000)",
            "insert IGNORE into RaceResults values('race25', 'horse20', 'fourth', 2000)",
            "insert IGNORE into RaceResults values('race26', 'horse24', 'first', 8000)",
            "insert IGNORE into RaceResults values('race26', 'horse20', 'fourth', 2000)",
            "insert IGNORE into RaceResults values('race27', 'horse18', 'first', 70000)",
            "insert IGNORE into RaceResults values('race27', 'horse23', 'third', 40000)",
            "insert IGNORE into RaceResults values('race28', 'horse25', 'first', 90000)",
            "insert IGNORE into RaceResults values('race29', 'horse15', 'first', 80000)",
            "insert IGNORE into RaceResults values('race29', 'horse3', 'second', 65000)",
            "insert IGNORE into RaceResults values('race29', 'horse24', 'third', 50000)",
            "insert IGNORE into RaceResults values('race30', 'horse14', 'second', 1500)",
            "insert IGNORE into RaceResults values('race30', 'horse10', 'fourth', 500)",
            "insert IGNORE into RaceResults values('race31', 'horse7', 'first', 90000)",
            "insert IGNORE into RaceResults values('race31', 'horse26', 'second', 70000)",
            "insert IGNORE into RaceResults values('race31', 'horse23', 'third', 50000)",
            "insert IGNORE into RaceResults values('race31', 'horse10', 'fourth', 30000)",
            "insert IGNORE into RaceResults values('race32', 'horse22', 'first', 150000)",
            "insert IGNORE into RaceResults values('race32', 'horse13', 'second', 125000)",
            "insert IGNORE into RaceResults values('race32', 'horse16', 'third', 100000)",
            "insert IGNORE into RaceResults values('race33', 'horse23', 'second', 1700)",
            "insert IGNORE into RaceResults values('race33', 'horse26', 'third', 1200)",
            "insert IGNORE into RaceResults values('race34', 'horse11', 'first', 50000)",
            "insert IGNORE into RaceResults values('race34', 'horse15', 'second', 30000)",
            "insert IGNORE into RaceResults values('race35', 'horse7', 'first', 45000)",
            "insert IGNORE into RaceResults values('race35', 'horse19', 'second', 25000)",
            "insert IGNORE into RaceResults values('race36', 'horse11', 'first', 100000)",
            "insert IGNORE into RaceResults values('race36', 'horse15', 'second', 80000)",
            "insert IGNORE into RaceResults values('race36', 'horse20', 'third', 50000)"
    };
    public Statement CreateTable(Connection reservationConn) throws SQLException {
        Statement createTable= reservationConn.createStatement();
        createTable.execute(raceResults);
        for (String insertSql : insertStatements) {
            createTable.executeUpdate(insertSql);
        }
        return createTable;
    }
    public boolean horseExists(Connection conn, String horseId) throws SQLException {
        String sql = "SELECT horseId FROM Horse WHERE horseId = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, horseId);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public boolean resultExists(Connection conn, String raceId, String horseId) throws SQLException {
        String sql = "SELECT raceId FROM RaceResults WHERE raceId = ? AND horseId = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, raceId);
        ps.setString(2, horseId);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public boolean addRaceResult(Connection conn, String raceId, String horseId, String results, String prize) throws SQLException {
        if (!horseExists(conn, horseId)) {
            throw new SQLException("Horse with ID " + horseId + " does not exist");
        }
        if (resultExists(conn, raceId, horseId)) {
            throw new SQLException("Result for horse " + horseId + " in race " + raceId + " already exists");
        }
        String sql = "INSERT INTO RaceResults (raceId, horseId, results, prize) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, raceId);
            ps.setString(2, horseId);

            if (results != null && !results.trim().isEmpty()) {
                ps.setString(3, results.trim());
            } else {
                ps.setNull(3, java.sql.Types.VARCHAR);
            }

            if (prize != null && !prize.trim().isEmpty()) {
                try {
                    float prizeValue = Float.parseFloat(prize.trim());
                    ps.setFloat(4, prizeValue);
                } catch (NumberFormatException e) {
                    throw new SQLException("Prize must be a valid number");
                }
            } else {
                ps.setNull(4, java.sql.Types.FLOAT);
            }
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
