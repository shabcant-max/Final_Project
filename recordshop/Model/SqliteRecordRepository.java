package recordshop.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SqliteRecordRepository implements RecordRepository {

    private final String dbUrl;

    public SqliteRecordRepository(String dbFilePath) {
        this.dbUrl = "jdbc:sqlite:" + dbFilePath;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS records (" +
                     "idnumber TEXT PRIMARY KEY, " +
                     "title TEXT NOT NULL, " +
                     "artist TEXT NOT NULL, " +
                     "price REAL NOT NULL, " +
                     "instock INTEGER NOT NULL);";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error creating database table: " + e.getMessage());
        }
    }

    @Override
    public List<Record> findAll() {
        List<Record> records = new ArrayList<>();
        String sql = "SELECT idnumber, title, artist, price, instock FROM records";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                records.add(new Record(
                    rs.getString("idnumber"),
                    rs.getDouble("price"),
                    rs.getString("artist"),
                    rs.getInt("instock") == 1,
                    rs.getString("title")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching records: " + e.getMessage());
        }
        return records;
    }

    @Override
    public Record findById(String id) {
        if (id == null) {
            return null;
        }
        String sql = "SELECT idnumber, title, artist, price, instock FROM records WHERE idnumber = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Record(
                        rs.getString("idnumber"),
                        rs.getDouble("price"),
                        rs.getString("artist"),
                        rs.getInt("instock") == 1,
                        rs.getString("title")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding record by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Record save(Record record) {
        if (record == null) {
            throw new IllegalArgumentException("Cannot save a null record.");
        }

        Record existing = findById(record.getIdnumber());
        if (existing == null) {
            String sql = "INSERT INTO records(idnumber, title, artist, price, instock) VALUES(?, ?, ?, ?, ?)";
            try (Connection conn = DriverManager.getConnection(dbUrl);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, record.getIdnumber());
                pstmt.setString(2, record.getTitle());
                pstmt.setString(3, record.getArtist());
                pstmt.setDouble(4, record.getPrice());
                pstmt.setInt(5, record.getAvailability() ? 1 : 0);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error inserting record: " + e.getMessage());
            }
        } else {
            String sql = "UPDATE records SET title = ?, artist = ?, price = ?, instock = ? WHERE idnumber = ?";
            try (Connection conn = DriverManager.getConnection(dbUrl);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, record.getTitle());
                pstmt.setString(2, record.getArtist());
                pstmt.setDouble(3, record.getPrice());
                pstmt.setInt(4, record.getAvailability() ? 1 : 0);
                pstmt.setString(5, record.getIdnumber());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error updating record: " + e.getMessage());
            }
        }
        return record;
    }

    @Override
    public void deleteById(String id) {
        if (id == null) {
            return;
        }
        String sql = "DELETE FROM records WHERE idnumber = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting record: " + e.getMessage());
        }
    }
}