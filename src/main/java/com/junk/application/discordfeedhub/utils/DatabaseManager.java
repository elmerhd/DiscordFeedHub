package com.junk.application.discordfeedhub.utils;

import com.junk.application.discordfeedhub.model.RssSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author elmerhd
 */
public class DatabaseManager {
    
    static {
        
        try {
            Connection conn = DriverManager.getConnection(getDatabaseConnectionURL());
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
        } catch (IOException | SQLException ex) {
            System.getLogger(DatabaseManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    private static String getDatabaseConnectionURL() throws IOException {
        Properties applicationProperty = Utility.getApplicationProperty();
        String applicationName = applicationProperty.getProperty("app.name");
        String template = "jdbc:sqlite:{0}\\{1}.db";
        String result = MessageFormat.format(
                template,
                Utility.getApplicationFolder(),
                applicationName.toLowerCase()
        );
        return result;
    }

    public static Connection getConnection() throws SQLException, IOException {
        return DriverManager.getConnection(getDatabaseConnectionURL());
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
            return null;
        } catch (SQLException | IOException ex) {
            System.getLogger(DatabaseManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return null;
        }
    }
    
    public static boolean isPosted(int sourceId, String itemLink) {
        String countSql = """
            SELECT id
            FROM posted_item
            WHERE rss_source_id = ? AND item_link = ?
        """;
        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(countSql)) {

            ps.setInt(1, sourceId);
            ps.setString(2, itemLink);
            
            return ps.executeQuery().next();

        } catch (SQLException | IOException ex) {
            System.getLogger(DatabaseManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }
    }
    
    public static DmlResult saveNewResource(
            String title, 
            String websiteURL, 
            String rssUrl, 
            String discordWebhookUrl) {
        
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
                    return DmlResult.failure(
                            DmlStatus.NO_ROWS_AFFECTED,
                            "RSS URL & Discord Webhook URL already exist",
                            null
                    );
                }
            }
           
            try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                insertPs.setString(1, title);
                insertPs.setString(2, websiteURL);
                insertPs.setString(3, rssUrl);
                insertPs.setString(4, discordWebhookUrl);
                
                int affectedRows = insertPs.executeUpdate();

                if (affectedRows == 0) {
                    return DmlResult.failure(
                            DmlStatus.NO_ROWS_AFFECTED,
                            "No rows were inserted",
                            null
                    );
                }
                return DmlResult.success(affectedRows);
            }

        } catch (SQLTimeoutException ex) {
            System.getLogger(DatabaseManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return DmlResult.failure(
                    DmlStatus.TIMEOUT,
                    "Query execution timed out",
                    ex
            );
        } catch (SQLException ex) {
            System.getLogger(DatabaseManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return DmlResult.failure(
                    DmlStatus.UNKNOWN_ERROR,
                    ex.getMessage(),
                    ex
            );
        } catch (IOException ex) {
            System.getLogger(DatabaseManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return DmlResult.failure(
                    DmlStatus.UNKNOWN_ERROR,
                    ex.getMessage(),
                    ex
            );
        }
    }
    
    public static DmlResult updateRSSSource(
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

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                return DmlResult.failure(
                        DmlStatus.NO_ROWS_AFFECTED,
                        "No rows were updated",
                        null
                );
            }

            return DmlResult.success(affectedRows);

        } catch (SQLTimeoutException ex) {
            System.getLogger(DatabaseManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return DmlResult.failure(
                    DmlStatus.TIMEOUT,
                    "Query execution timed out",
                    ex
            );
        } catch (SQLException ex) {
            System.getLogger(DatabaseManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return DmlResult.failure(
                    DmlStatus.UNKNOWN_ERROR,
                    ex.getMessage(),
                    ex
            );
        } catch (IOException ex) {
            System.getLogger(DatabaseManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return DmlResult.failure(
                DmlStatus.UNKNOWN_ERROR,
                    ex.getMessage(),
                    ex
            );
        }
    }
    
    public static DmlResult markAsPosted(int sourceId, String guid, String itemLink) {
        String sql = """
            INSERT OR IGNORE INTO posted_item
            (rss_source_id, item_guid, item_link)
            VALUES (?, ?, ?)
        """;

        try (Connection c = getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, sourceId);
            ps.setString(2, guid);
            ps.setString(3, itemLink);
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows == 0) {
                return DmlResult.failure(
                        DmlStatus.NO_ROWS_AFFECTED,
                        "No rows were updated",
                        null
                );
            }

            return DmlResult.success(affectedRows);
            
        } catch (SQLTimeoutException ex) {
            System.getLogger(DatabaseManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return DmlResult.failure(
                    DmlStatus.TIMEOUT,
                    "Query execution timed out",
                    ex
            );
        } catch (SQLException ex) {
            System.getLogger(DatabaseManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return DmlResult.failure(
                    DmlStatus.UNKNOWN_ERROR,
                    ex.getMessage(),
                    ex
            );
        } catch (IOException ex) {
            System.getLogger(DatabaseManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return DmlResult.failure(
                DmlStatus.UNKNOWN_ERROR,
                    ex.getMessage(),
                    ex
            );
        }
    }
    
    public static DmlResult deleteRssSource(int id) {
        String sql = "DELETE FROM rss_source WHERE id = ?";

        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                return DmlResult.failure(
                        DmlStatus.NO_ROWS_AFFECTED,
                        "No rows were affected",
                        null);
            }
            
            return DmlResult.success(affectedRows);

        } catch (SQLTimeoutException ex) {
            System.getLogger(DatabaseManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return DmlResult.failure(
                    DmlStatus.TIMEOUT,
                    "Query execution timed out",
                    ex
            );
        } catch (SQLException ex) {
            System.getLogger(DatabaseManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return DmlResult.failure(
                    DmlStatus.UNKNOWN_ERROR,
                    ex.getMessage(),
                    ex
            );
        } catch (IOException ex) {
            System.getLogger(DatabaseManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return DmlResult.failure(
                DmlStatus.UNKNOWN_ERROR,
                ex.getMessage(),
                ex
            );
        }
    }
    
    public static List<RssSource> loadSources() throws SQLException, IOException {
        return loadSources(false);
    }
    
    public static List<RssSource> loadSources(boolean enabledOnly) throws SQLException, IOException {
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
        
        if (enabledOnly) {
            sql += " WHERE enabled=true";
        }
        
        Connection c = getConnection();
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery(sql);

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
        return list;
    }
}
