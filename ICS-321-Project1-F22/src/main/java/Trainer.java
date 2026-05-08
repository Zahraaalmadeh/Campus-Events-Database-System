import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;

public class Trainer {
    String trainer = "create table IF NOT EXISTS Trainer \n" +
            "   (trainerId varchar(15) not null, \n" +
            "    lname varchar(30), \n" +
            "    fname varchar(30),  \n" +
            "    stableId varchar(30), \n" +
            "    primary key(trainerId), \n" +
            "    foreign key(stableId) references Stable(stableId));";

    String[] insertStatements = {
            "insert IGNORE into Trainer values('trainer1', 'Mohammed', 'Fahd', 'stable2')",
            "insert IGNORE into Trainer values('trainer2', 'Saleh', 'Saeed', 'stable1')",
            "insert IGNORE into Trainer values('trainer3', 'Ali', 'Raad', 'stable4')",
            "insert IGNORE into Trainer values('trainer4', 'Sayed', 'Wasim', 'stable3')",
            "insert IGNORE into Trainer values('trainer5', 'Ahmed', 'Ali', 'stable3')",
            "insert IGNORE into Trainer values('trainer6', 'Faisal', 'Salah', 'stable5')",
            "insert IGNORE into Trainer values('trainer7', 'Hamid', 'Ahmed', 'stable6')",
            "insert IGNORE into Trainer values('trainer8', 'Khalid', 'Ahmed', 'stable6')"
    };

    public Statement CreateTable(Connection reservationConn) throws SQLException {
        Statement createTable = reservationConn.createStatement();
        createTable.execute(trainer);
        for (String insertSql : insertStatements) {
            createTable.executeUpdate(insertSql);
        }
        return createTable;
    }

    public boolean trainerExists(Connection conn, String trainerId) throws SQLException {
        String sql = "SELECT trainerId FROM Trainer WHERE trainerId = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, trainerId);
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


    public String getCurrentStable(Connection conn, String trainerId) throws SQLException {
        String sql = "SELECT stableId FROM Trainer WHERE trainerId = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, trainerId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("stableId");
        }
        return null;
    }

    public boolean approveTrainer(Connection conn, String trainerId, String stableId) throws SQLException {
        if (!trainerExists(conn, trainerId)) {
            throw new SQLException("Trainer with ID " + trainerId + " does not exist");
        }

        if (!stableExists(conn, stableId)) {
            throw new SQLException("Stable with ID " + stableId + " does not exist");
        }

        String currentStable = getCurrentStable(conn, trainerId);
        if (currentStable != null && currentStable.equals(stableId)) {
            throw new SQLException("Trainer is already approved for stable " + stableId);
        }

        String sql = "UPDATE Trainer SET stableId = ? WHERE trainerId = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, stableId);
        ps.setString(2, trainerId);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    }
}