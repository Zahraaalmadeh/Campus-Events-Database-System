import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;


public class Race {
    String race="\n" +
            "create table IF NOT EXISTS Race \n" +
            "   (raceId varchar(15) not null, \n" +
            "    raceName varchar(30), \n" +
            "    trackName varchar(30), \n" +
            "    raceDate date, \n" +
            "    raceTime time, \n" +
            "    primary key(raceId), \n" +
            "    foreign key (trackName) references Track(trackName));";
    String[] insertStatements = {
            "insert IGNORE into Race values('race1', 'Kings Cup', 'Riyadh', '2007-05-03','14:00')",
            "insert IGNORE into Race values('race2', '2-year-old fillies', 'Doha', '2007-05-03','13:00')",
            "insert IGNORE into Race values('race3', '2-year-old colts', 'Doha', '2007-05-03','13:30')",
            "insert IGNORE into Race values('race4', 'Handicap', 'Doha', '2007-05-03','12:00')",
            "insert IGNORE into Race values('race5', 'Claiming Stake', 'Sharjah', '2007-05-03','12:30')",
            "insert IGNORE into Race values('race6', '3-year-old fillies', 'Jubail', '2007-06-02','12:30')",
            "insert IGNORE into Race values('race7', 'Handicap', 'Jubail', '2007-06-02','9:30')",
            "insert IGNORE into Race values('race8', '2-year-old colts', 'Riyadh', '2007-06-02','10:30')",
            "insert IGNORE into Race values('race9', '2-year-old fillies', 'Jubail', '2007-06-02','11:30')",
            "insert IGNORE into Race values('race10', 'Claiming Stake', 'Sharjah', '2007-06-02','12:30')",
            "insert IGNORE into Race values('race11', '3-year-old fillies', 'Dubai', '2007-04-02','10:30')",
            "insert IGNORE into Race values('race12', 'Handicap', 'Yanbu', '2007-05-03','11:30')",
            "insert IGNORE into Race values('race13', '3-year-old fillies', 'Yanbu', '2007-05-03','11:00')",
            "insert IGNORE into Race values('race14', 'Handicap', 'Dhahran', '2007-05-10','10:00')",
            "insert IGNORE into Race values('race15', '3-year-old colts', 'Dubai', '2007-05-12','15:00')",
            "insert IGNORE into Race values('race16', 'Claiming Stake', 'Yanbu', '2007-05-20','14:30')",
            "insert IGNORE into Race values('race17', 'Handicap', 'Doha', '2007-05-20','13:00')",
            "insert IGNORE into Race values('race18', '3-year-old fillies', 'Sharjah', '2007-05-21','8:00')",
            "insert IGNORE into Race values('race19', '2-year-old colts', 'Dhahran', '2007-05-25','11:00')",
            "insert IGNORE into Race values('race20', 'Claiming Stake', 'Jeddah', '2007-05-25','8:30')",
            "insert IGNORE into Race values('race21', '3-year-old colts', 'Riyadh', '2007-03-19','14:30')",
            "insert IGNORE into Race values('race22', 'Handicap', 'Dhahran', '2007-03-27','15:00')",
            "insert IGNORE into Race values('race23', '3-year-old fillies', 'Jeddah', '2007-03-28','9:30')",
            "insert IGNORE into Race values('race24', '3-year-old colts', 'Jubail', '2007-03-28','13:30')",
            "insert IGNORE into Race values('race25', 'Claiming Stake', 'Jeddah', '2007-03-29','10:00')",
            "insert IGNORE into Race values('race26', '3-year-old colts', 'Yanbu', '2007-03-30','12:30')",
            "insert IGNORE into Race values('race27', 'Handicap', 'Dubai', '2007-04-03','14:00')",
            "insert IGNORE into Race values('race28', '2-year-old fillies', 'Jeddah', '2007-04-04','8:30')",
            "insert IGNORE into Race values('race29', '3-year-old colts', 'Bahrain', '2007-04-05','8:00')",
            "insert IGNORE into Race values('race30', 'Claiming Stake', 'Dhahran', '2007-04-08','9:30')",
            "insert IGNORE into Race values('race31', 'Handicap', 'Dhahran', '2007-04-08','9:00')",
            "insert IGNORE into Race values('race32', '2-year-old colts', 'Jubail', '2007-04-09','11:00')",
            "insert IGNORE into Race values('race33', 'Claiming Stake', 'Bahrain', '2007-04-10','13:00')",
            "insert IGNORE into Race values('race34', '3-year-old colts', 'Dubai', '2007-05-12','12:00')",
            "insert IGNORE into Race values('race35', 'Handicap', 'Dubai', '2007-04-13','10:30')",
            "insert IGNORE into Race values('race36', '3-year-old colts', 'Jeddah', '2007-05-03','14:30')"
    };
    public Statement CreateTable(Connection reservationConn) throws SQLException {
        Statement createTable= reservationConn.createStatement();
        createTable.execute(race);
        for (String insertSql : insertStatements) {
            createTable.executeUpdate(insertSql);
        }
        return createTable;
    }

    public boolean raceExists(Connection conn, String raceId) throws SQLException {
        String sql = "SELECT raceId FROM Race WHERE raceId = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, raceId);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public boolean addNewRace(Connection conn, String raceId, String raceName, String trackName,
                              String raceDate, String raceTime) throws SQLException {
        if (raceExists(conn, raceId)) {
            throw new SQLException("Race with ID " + raceId + " already exists");
        }
        String sql = "INSERT INTO Race (raceId, raceName, trackName, raceDate, raceTime) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, raceId);

            if (raceName != null && !raceName.trim().isEmpty()) {
                ps.setString(2, raceName.trim());
            } else {
                ps.setNull(2, java.sql.Types.VARCHAR);
            }

            if (trackName != null && !trackName.trim().isEmpty()) {
                ps.setString(3, trackName.trim());
            } else {
                ps.setNull(3, java.sql.Types.VARCHAR);
            }

            if (raceDate != null && !raceDate.trim().isEmpty()) {
                try {
                    java.sql.Date sqlDate = java.sql.Date.valueOf(raceDate.trim()); // expects "yyyy-MM-dd"
                    ps.setDate(4, sqlDate);
                } catch (IllegalArgumentException e) {
                    throw new SQLException("Invalid date format. Please use yyyy-MM-dd format");
                }
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }

            if (raceTime != null && !raceTime.trim().isEmpty()) {
                try {
                    String timeValue = raceTime.trim();
                    if (!timeValue.contains(":")) {
                        throw new IllegalArgumentException("Invalid time format");
                    }
                    String[] timeParts = timeValue.split(":");
                    if (timeParts.length == 2) {
                        timeValue = timeValue + ":00"; // Add seconds if missing
                    }
                    java.sql.Time sqlTime = java.sql.Time.valueOf(timeValue); // expects "HH:mm:ss"
                    ps.setTime(5, sqlTime);
                } catch (IllegalArgumentException e) {
                    throw new SQLException("Invalid time format. Please use HH:mm format");
                }
            } else {
                ps.setNull(5, java.sql.Types.TIME);
            }
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
