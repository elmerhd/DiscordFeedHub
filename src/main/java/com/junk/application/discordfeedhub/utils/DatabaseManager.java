package com.junk.application.discordfeedhub.utils;

import com.junk.application.discordfeedhub.model.RssSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elmerhd
 */
public class DatabaseManager {
    
    private static final String dbConnectionUrl = "jdbc:sqlite:discordfeedhub.db";
    
    static {
        try (Connection conn = DriverManager.getConnection(dbConnectionUrl)) {
            String sqlRSSSource = """
                CREATE TABLE IF NOT EXISTS rss_source (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    website_url TEXT,
                    rss_url TEXT NOT NULL,
                    discord_webhook_url TEXT NOT NULL,
                    enabled INTEGER DEFAULT 1,
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                );
            """;
            
            String sqlPostedItem = """
                CREATE TABLE IF NOT EXISTS posted_item (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    rss_source_id INTEGER,
                    item_guid TEXT,
                    item_link TEXT,
                    posted_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                    UNIQUE (rss_source_id, item_guid),
                    FOREIGN KEY (rss_source_id) REFERENCES rss_sources(id)
                );
            """;
            conn.createStatement().execute(sqlRSSSource);
            conn.createStatement().execute(sqlPostedItem);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbConnectionUrl);
    }
    
    
    public static RssSource getRssResourceById(int id) {
        String getRssSourceSql = """
            SELECT id, 
                title, 
                website_url, 
                rss_url, 
                discord_webhook_url, 
                enabled
            FROM rss_source
            WHERE id = ? LIMIT 1
        """;
        try (Connection conn = getConnection();
            PreparedStatement countPs = conn.prepareStatement(getRssSourceSql)) {
            countPs.setInt(1, id);

            try (ResultSet rs = countPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return new RssSource(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6) == 1 ? true : false);
                }
            }

        } catch (SQLException e) {
            return null;
        }
        return null;
    }
    
    public static DatabaseStatementStatus saveNewResource(String title, String websiteURL, String rssUrl, String discordWebhookUrl) {
        
        String countSql = """
            SELECT COUNT(*)
            FROM rss_source
            WHERE rss_url = ? AND discord_webhook_url = ?
        """;

        String insertSql = """
            INSERT INTO rss_source
            (title, website_url, rss_url, discord_webhook_url)
            VALUES (?, ?, ?, ?)
        """;
        
        try (Connection conn = getConnection();
            PreparedStatement countPs = conn.prepareStatement(countSql)) {

            countPs.setString(1, rssUrl);
            countPs.setString(2, discordWebhookUrl);

            try (ResultSet rs = countPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return new DatabaseStatementStatus(
                        false,
                        "RSS URL & Discord Webhook URL already exist"
                    );
                }
            }
           
            try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                insertPs.setString(1, title);
                insertPs.setString(2, websiteURL);
                insertPs.setString(3, rssUrl);
                insertPs.setString(4, discordWebhookUrl);

                insertPs.executeUpdate();
            }

           return new DatabaseStatementStatus(true, "Successful");

        } catch (SQLException e) {
            return new DatabaseStatementStatus(false, e.getMessage());
        }
    }
    
    public static DatabaseStatementStatus updateRSSSource(
            int id,
            String title,
            String websiteUrl,
            String rssUrl,
            String webhookUrl,
            int enabled
    ) {

        String updateSql = """
            UPDATE rss_source
            SET title = ?,
                website_url = ?,
                rss_url = ?,
                discord_webhook_url = ?,
                enabled = ?
            WHERE id = ?
        """;

        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(updateSql)) {

            ps.setString(1, title);
            ps.setString(2, websiteUrl);
            ps.setString(3, rssUrl);
            ps.setString(4, webhookUrl);
            ps.setInt(5, enabled);
            ps.setInt(6, id);

            int affected = ps.executeUpdate();

            if (affected == 0) {
                return new DatabaseStatementStatus(false, "No record updated");
            }
            return new DatabaseStatementStatus(true, "Updated successfully");

        } catch (SQLException e) {
            return new DatabaseStatementStatus(false, e.getMessage());
        }
    }
    
    public static DatabaseStatementStatus deleteRssSource(int id) {
        String sql = "DELETE FROM rss_source WHERE id = ?";

        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                return new DatabaseStatementStatus(true, "Deleted successfully");
            } else {
                return new DatabaseStatementStatus(false, "Record not found");
            }

        } catch (SQLException e) {
            return new DatabaseStatementStatus(false, e.getMessage());
        }
    }
    
    public static List<RssSource> loadSources() {
        List<RssSource> list = new ArrayList<>();
        String sql = """
            SELECT id,
                title,
                website_url, 
                rss_url, 
                discord_webhook_url, 
                enabled 
            FROM rss_source
        """;

        try (Connection c = getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {

            while (rs.next()) {
                RssSource r = new RssSource(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("website_url"),
                    rs.getString("rss_url"),
                    rs.getString("discord_webhook_url"),
                    rs.getInt("enabled") == 1 ? true : false);
                list.add(r);
            }

        } catch (SQLException e) {
            return list;
        }
        return list;
    }
}
