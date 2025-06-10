package util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.SQLException;

public class DBUtilTest {

    @BeforeEach
    void setUp() {
        // Any setup needed before each test
    }

    @AfterEach
    void tearDown() {
        // Close the data source after each test
        DBUtil.closeDataSource();
    }

    @Test
    void testGetConnection() {
        try {
            Connection conn = DBUtil.getConnection();
            assertNotNull(conn);
            assertTrue(conn.isValid(5));
            conn.close();
        } catch (SQLException e) {
            fail("Failed to get database connection: " + e.getMessage());
        }
    }

    @Test
    void testIsConnectionValid() {
        assertTrue(DBUtil.isConnectionValid());
    }

    @Test
    void testMultipleConnections() {
        try {
            Connection conn1 = DBUtil.getConnection();
            Connection conn2 = DBUtil.getConnection();
            
            assertNotNull(conn1);
            assertNotNull(conn2);
            assertNotEquals(conn1, conn2);
            
            assertTrue(conn1.isValid(5));
            assertTrue(conn2.isValid(5));
            
            conn1.close();
            conn2.close();
        } catch (SQLException e) {
            fail("Failed to get multiple database connections: " + e.getMessage());
        }
    }

    @Test
    void testConnectionPooling() {
        try {
            // Get multiple connections to test the pool
            Connection[] connections = new Connection[10];
            for (int i = 0; i < 10; i++) {
                connections[i] = DBUtil.getConnection();
                assertNotNull(connections[i]);
                assertTrue(connections[i].isValid(5));
            }
            
            // Close all connections
            for (Connection conn : connections) {
                conn.close();
            }
            
            // Get a new connection after closing all
            Connection newConn = DBUtil.getConnection();
            assertNotNull(newConn);
            assertTrue(newConn.isValid(5));
            newConn.close();
            
        } catch (SQLException e) {
            fail("Failed to test connection pooling: " + e.getMessage());
        }
    }
} 