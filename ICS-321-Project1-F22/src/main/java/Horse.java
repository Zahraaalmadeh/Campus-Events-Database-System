import java.sql.*;
public class Horse {
    String HorseCreate="create table IF NOT EXISTS Horse " +
            " (horseId varchar(15) not null," +
            " horseName varchar(15) not null," +
            " age int," +
            " gender char," +
            " registration integer not null," +
            " stableId varchar(30) not null," +
            " foreign key(stableId) references Stable(stableId)," +
            " primary key(horseId));";
    String[] insertStatements = {
            "insert IGNORE into Horse values ('horse1', 'Warrior', 2, 'C', '11111', 'stable1')",
            "insert IGNORE into Horse values ('horse2', 'Conquerer', 2, 'F', '22222', 'stable6')",
            "insert IGNORE into Horse values ('horse3', 'Dove of Peace', 3, 'C', '33333', 'stable1')",
            "insert IGNORE into Horse values ('horse4', 'Ever Faster', 3, 'F', '44444', 'stable3')",
            "insert IGNORE into Horse values ('horse5', 'Slow Winner', 2, 'C', '55555', 'stable3')",
            "insert IGNORE into Horse values ('horse6', 'Windrunner', 2, 'F', '66666', 'stable2')",
            "insert IGNORE into Horse values ('horse7', 'Catapult', 4, 'M', '77777', 'stable6')",
            "insert IGNORE into Horse values ('horse8', 'Flying Force', 2, 'C', '88888', 'stable4')",
            "insert IGNORE into Horse values ('horse9', 'Laggard', 2, 'F', '99999', 'stable4')",
            "insert IGNORE into Horse values ('horse10', 'Formula One', 6, 'G', '10101', 'stable2')",
            "insert IGNORE into Horse values ('horse11', 'Frisky Frolic', 3, 'C', '11011', 'stable4')",
            "insert IGNORE into Horse values ('horse12', 'Fantastic', 3, 'F', '12121', 'stable2')",
            "insert IGNORE into Horse values ('horse13', 'Midnight', 2, 'C', '13131', 'stable3')",
            "insert IGNORE into Horse values ('horse14', 'Running Wild', 4, 'S', '14141', 'stable2')",
            "insert IGNORE into Horse values ('horse15', 'FastOffMyFeet', 3, 'C', '15151', 'stable1')",
            "insert IGNORE into Horse values ('horse16', 'Slow Poke', 2, 'C', '16161', 'stable3')",
            "insert IGNORE into Horse values ('horse17', 'Slinger', 3, 'F', '17171', 'stable2')",
            "insert IGNORE into Horse values ('horse18', 'Sublime', 5, 'M', '18181', 'stable6')",
            "insert IGNORE into Horse values ('horse19', 'Front Runner', 4, 'G', '19191', 'stable4')",
            "insert IGNORE into Horse values ('horse20', 'Night', 3, 'C', '20200', 'stable1')",
            "insert IGNORE into Horse values ('horse21', 'Negative', 3, 'F', '21210', 'stable3')",
            "insert IGNORE into Horse values ('horse22', 'Lightening', 2, 'C', '22220', 'stable6')",
            "insert IGNORE into Horse values ('horse23', 'Lazy Loser', 4, 'G', '23230', 'stable1')",
            "insert IGNORE into Horse values ('horse24', 'Leaping Lizard', 2, 'C', '24240', 'stable1')",
            "insert IGNORE into Horse values ('horse25', 'Beautiful Brown', 3, 'F', '25250', 'stable6')",
            "insert IGNORE into Horse values ('horse26', 'Sick Winner', 5, 'M', '26260', 'stable2')"
    };

    public Statement CreateTable(Connection reservationConn) throws SQLException {
        Statement CreateTable= reservationConn.createStatement();
        CreateTable.execute(HorseCreate);
        for (String insertSql : insertStatements) {
            CreateTable.executeUpdate(insertSql);
        }
        return CreateTable;
    }

    public boolean horseExists(Connection conn, String horseId) throws SQLException {
        String sql = "SELECT horseId FROM Horse WHERE horseId = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, horseId);
        ResultSet rs = ps.executeQuery();
        return rs.next();
}

    public boolean stableExists(Connection conn, String stableId) throws SQLException {
        String sql = "SELECT stableId FROM Stable WHERE stableId = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, stableId);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public String getCurrentStable(Connection conn, String horseId) throws SQLException {
        String sql = "SELECT stableId FROM Horse WHERE horseId = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, horseId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("stableId");
        }
        return null;
    }

    public boolean moveHorse(Connection conn, String horseId, String newStableId) throws SQLException {
        if (!horseExists(conn, horseId)) {
            throw new SQLException("Horse with ID " + horseId + " does not exist");
        }

        if (!stableExists(conn, newStableId)) {
            throw new SQLException("Stable with ID " + newStableId + " does not exist");
        }

        String currentStable = getCurrentStable(conn, horseId);
        if (currentStable != null && currentStable.equals(newStableId)) {
            throw new SQLException("Horse is already in stable " + newStableId);
        }

        String sql = "UPDATE Horse SET stableId = ? WHERE horseId = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, newStableId);
        ps.setString(2, horseId);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    }

}
