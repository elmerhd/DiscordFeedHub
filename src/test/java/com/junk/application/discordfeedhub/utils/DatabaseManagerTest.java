package com.junk.application.discordfeedhub.utils;

import com.junk.application.discordfeedhub.model.DmlResult;
import com.junk.application.discordfeedhub.model.RssSource;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author elmerhd
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseManagerTest {

    private static int testSourceId;

    @Test
    @Order(1)
    void testSaveNewResource() {
        DmlResult result = DatabaseManager.saveNewResource(
                "Test Feed",
                "https://example.com",
                "https://example.com/rss",
                "https://discord.com/api/webhooks/test"
        );

        assertTrue(result.isSuccess(), "Should successfully insert new RSS source");
    }

    @Test
    @Order(2)
    void testLoadSources() throws SQLException, IOException {
        List<RssSource> sources = DatabaseManager.loadSources();
        assertFalse(sources.isEmpty(), "Sources list should not be empty");

        // Save the first source id for later tests
        testSourceId = sources.get(0).id();
    }

    @Test
    @Order(3)
    void testGetRssResourceById() {
        RssSource source = DatabaseManager.getRssResourceById(testSourceId);
        assertNotNull(source, "Should return RSS source by ID");
        assertEquals(testSourceId, source.id());
    }

    @Test
    @Order(4)
    void testUpdateRSSSource() {
        DmlResult result = DatabaseManager.updateRSSSource(
                testSourceId,
                "Updated Feed",
                "https://example.org",
                "https://example.org/rss",
                "https://discord.com/api/webhooks/test-updated",
                1
        );
        assertTrue(result.isSuccess(), "Should successfully update RSS source");

        RssSource updated = DatabaseManager.getRssResourceById(testSourceId);
        assertEquals("Updated Feed", updated.title());
    }

    @Test
    @Order(5)
    void testMarkAsPosted() {
        DmlResult result = DatabaseManager.markAsPosted(
                testSourceId,
                "guid-test",
                "https://example.org/rss/item1"
        );
        assertTrue(result.isSuccess(), "Should successfully mark item as posted");

        assertTrue(DatabaseManager.isPosted(testSourceId, "https://example.org/rss/item1"),
                "isPosted should return true for posted item");
    }
}

