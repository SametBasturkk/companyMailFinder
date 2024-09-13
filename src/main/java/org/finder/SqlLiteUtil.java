package org.finder;

import lombok.Synchronized;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SqlLiteUtil {
    private static final String FILE_NAME = "company.db";
    private static final String DB_URL = "jdbc:sqlite:" + FILE_NAME;
    private static final Logger LOGGER = Logger.getLogger(SqlLiteUtil.class.getName());
    private static final BasicDataSource dataSource = new BasicDataSource();

    static {
        dataSource.setUrl(DB_URL);
        dataSource.setInitialSize(5);
        dataSource.setMaxTotal(20);
    }

    @Synchronized
    public static boolean createNewDatabase() {
        Path dbPath = Path.of(FILE_NAME).toAbsolutePath();
        LOGGER.info("File path: " + dbPath);

        if (dbPath.toFile().exists()) {
            LOGGER.info("Database already exists.");
            return false;
        }

        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            LOGGER.info("The driver name is " + meta.getDriverName());
            LOGGER.info("A new database has been created.");
            createNewTable();
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating database", e);
            return false;
        }
    }

    public static void createNewTable() {
        String query = "CREATE TABLE IF NOT EXISTS companies (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "mail TEXT" +
                ");";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(query);
            LOGGER.info("Table created.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating table", e);
        }
    }

    public static void insertCompanyName(String data) {
        String query = "INSERT INTO companies (name) VALUES (?);";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, data);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting company name", e);
        }
    }

    public static void update(String name, String data) {
        String query = "UPDATE companies SET mail = ? WHERE name = ?;";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, data);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating mail", e);
        }
    }

    public static void prepareData() throws FileNotFoundException {
        if (createNewDatabase()) {
            List<String> companies = CompanyUtil.readFromFile();
            LOGGER.info(companies.size() + " companies were read from the file.");
            insertCompanies(companies);
        }
    }

    private static void insertCompanies(List<String> companies) {
        String query = "INSERT INTO companies (name) VALUES (?);";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            conn.setAutoCommit(false);
            for (String company : companies) {
                pstmt.setString(1, company);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting companies", e);
        }
    }

    public static HashMap<String, String> getCompanyById(Integer id) {
        String query = "SELECT name, mail FROM companies WHERE id = ?;";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                HashMap<String, String> company = new HashMap<>();
                company.put("name", rs.getString("name"));
                company.put("mail", rs.getString("mail"));
                return company;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting company by id", e);
        }
        return null;
    }
}