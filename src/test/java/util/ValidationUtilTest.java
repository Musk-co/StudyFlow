package util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilTest {

    @Test
    void testValidMessageLength() {
        assertTrue(ValidationUtil.isValidMessageLength("Hello"));
        assertTrue(ValidationUtil.isValidMessageLength("a".repeat(1000)));
        assertFalse(ValidationUtil.isValidMessageLength(null));
        assertFalse(ValidationUtil.isValidMessageLength(""));
        assertFalse(ValidationUtil.isValidMessageLength("a".repeat(1001)));
    }

    @Test
    void testValidApiKey() {
        assertTrue(ValidationUtil.isValidApiKey("AIzaSyA1234567890abcdefghijklmnopqrstuvwxyz"));
        assertFalse(ValidationUtil.isValidApiKey(null));
        assertFalse(ValidationUtil.isValidApiKey(""));
        assertFalse(ValidationUtil.isValidApiKey("invalid"));
        assertFalse(ValidationUtil.isValidApiKey("a".repeat(40)));
    }

    @Test
    void testValidSessionId() {
        assertTrue(ValidationUtil.isValidSessionId(1L));
        assertTrue(ValidationUtil.isValidSessionId(Long.MAX_VALUE));
        assertFalse(ValidationUtil.isValidSessionId(null));
        assertFalse(ValidationUtil.isValidSessionId(0L));
        assertFalse(ValidationUtil.isValidSessionId(-1L));
    }

    @Test
    void testValidTaskName() {
        assertTrue(ValidationUtil.isValidTaskName("Study Java"));
        assertTrue(ValidationUtil.isValidTaskName("a".repeat(100)));
        assertFalse(ValidationUtil.isValidTaskName(null));
        assertFalse(ValidationUtil.isValidTaskName(""));
        assertFalse(ValidationUtil.isValidTaskName("a".repeat(101)));
    }

    @Test
    void testValidTaskType() {
        assertTrue(ValidationUtil.isValidTaskType("Programming"));
        assertTrue(ValidationUtil.isValidTaskType("a".repeat(50)));
        assertFalse(ValidationUtil.isValidTaskType(null));
        assertFalse(ValidationUtil.isValidTaskType(""));
        assertFalse(ValidationUtil.isValidTaskType("a".repeat(51)));
    }

    @Test
    void testValidDifficultyLevel() {
        assertTrue(ValidationUtil.isValidDifficultyLevel(1));
        assertTrue(ValidationUtil.isValidDifficultyLevel(3));
        assertTrue(ValidationUtil.isValidDifficultyLevel(5));
        assertFalse(ValidationUtil.isValidDifficultyLevel(0));
        assertFalse(ValidationUtil.isValidDifficultyLevel(6));
    }

    @Test
    void testValidFatigueLevel() {
        assertTrue(ValidationUtil.isValidFatigueLevel(1));
        assertTrue(ValidationUtil.isValidFatigueLevel(3));
        assertTrue(ValidationUtil.isValidFatigueLevel(5));
        assertFalse(ValidationUtil.isValidFatigueLevel(0));
        assertFalse(ValidationUtil.isValidFatigueLevel(6));
    }

    @Test
    void testValidDurationMinutes() {
        assertTrue(ValidationUtil.isValidDurationMinutes(1));
        assertTrue(ValidationUtil.isValidDurationMinutes(60));
        assertTrue(ValidationUtil.isValidDurationMinutes(480));
        assertFalse(ValidationUtil.isValidDurationMinutes(0));
        assertFalse(ValidationUtil.isValidDurationMinutes(-1));
        assertFalse(ValidationUtil.isValidDurationMinutes(481));
    }

    @Test
    void testValidNotes() {
        assertTrue(ValidationUtil.isValidNotes(null));
        assertTrue(ValidationUtil.isValidNotes(""));
        assertTrue(ValidationUtil.isValidNotes("Some notes"));
        assertTrue(ValidationUtil.isValidNotes("a".repeat(1000)));
        assertFalse(ValidationUtil.isValidNotes("a".repeat(1001)));
    }
} 