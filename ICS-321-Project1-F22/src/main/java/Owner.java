import java.sql.*;
public class Owner {
    String owner = "create table IF NOT EXISTS Owner \n" +
            "   (ownerId varchar(15) not null, \n" +
            "    lname   varchar(15), \n" +
            "    fname   varchar(15), \n" +
            "    primary key(ownerId));";

    String[] insertStatements = {
            "insert IGNORE into Owner values('owner1', 'Saeed', 'Ahmed')",
            "insert IGNORE into Owner values('owner2', 'Mohammed', 'Khalid')",
            "insert IGNORE into Owner values('owner3', 'Mohammed', 'Faisal')",
            "insert IGNORE into Owner values('owner4', 'Fahd', 'Abdul Rahman')",
            "insert IGNORE into Owner values('owner5', 'Nasr', '')",
            "insert IGNORE into Owner values('owner6', 'Mohammed', 'Sheikh')",
            "insert IGNORE into Owner values('owner7', 'Abed', 'Ahmed')",
            "insert IGNORE into Owner values('owner8', 'Mashour', '')",
            "insert IGNORE into Owner values('owner9', 'Said', 'Sheikh')",
            "insert IGNORE into Owner values('owner10', 'Faisal', 'Khan')",
            "insert IGNORE into Owner values('owner11', 'Jabr', 'Mohammed')",
            "insert IGNORE into Owner values('owner12', 'Faleh', 'Mahmood')",
            "insert IGNORE into Owner values('owner13', 'Yahya', 'Mohammed')",
            "insert IGNORE into Owner values('owner14', 'Sulaiman', '')",
            "insert IGNORE into Owner values('owner15', 'Saeed', 'Ali')",
            "insert IGNORE into Owner values('owner16', 'Ahmed', 'Faisal')",
            "insert IGNORE into Owner values('owner17', 'Saud', 'Mohammed')",
            "insert IGNORE into Owner values('owner18', 'Nazir', 'Mohammed')",
            "insert IGNORE into Owner values('owner19', 'Saleh', 'Fahd')",
            "insert IGNORE into Owner values('owner20', 'Mohammed', 'Naeem')"
    };

    public Statement CreateTable(Connection reservationConn) throws SQLException {
        Statement createTable = reservationConn.createStatement();
        createTable.execute(owner);
        for (String insertSql : insertStatements) {
            createTable.executeUpdate(insertSql);
        }
        return createTable;
    }

    public boolean ownerExists(Connection conn, String ownerId) throws SQLException {
        String sql = "SELECT ownerId FROM Owner WHERE ownerId = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, ownerId);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
    public boolean OwnerDeletionOnly(Connection conn, String ownerId) throws SQLException {
        if (!ownerExists(conn, ownerId)) {
            return false;
        }
        conn.setAutoCommit(false);
        try {
            String deleteSql = "DELETE FROM Owner WHERE ownerId = ?";
            PreparedStatement ps = conn.prepareStatement(deleteSql);
            ps.setString(1, ownerId);
            int rowsAffected = ps.executeUpdate();
            conn.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public boolean deleteOwner(Connection conn, String ownerId) throws SQLException {
        String sql = "{CALL delete_owner(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, ownerId);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error executing delete_owner: " + e.getMessage());
            return false;
        }
    }
    public void CreateDeleteOwnerProcedure(Connection con) {
        try (Statement stmt = con.createStatement()) {
            stmt.execute("DROP PROCEDURE IF EXISTS delete_owner");
            String createProc =
                    "CREATE PROCEDURE delete_owner(IN p_ownerId VARCHAR(15)) "
                            + "BEGIN "
                            + "    DELETE FROM RaceResults "
                            + "    WHERE horseId IN (SELECT horseId FROM Owns WHERE ownerId = p_ownerId); "
                            + "    DELETE FROM Owns WHERE ownerId = p_ownerId; "
                            + "    DELETE FROM Owner WHERE ownerId = p_ownerId; "
                            + "END";

            stmt.execute(createProc);
        } catch (SQLException e) {
            System.out.println("Error creating stored procedure: " + e.getMessage());
        }
    }
}