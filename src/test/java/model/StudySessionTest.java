package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class StudySessionTest {

    @Test
    @DisplayName("Test valid study session creation")
    void testValidStudySession() {
        StudySession session = new StudySession();
        session.setTaskName("Math Homework");
        session.setTaskType("Homework");
        session.setDifficultyLevel(3);
        session.setFatigueLevel(2);
        session.setDurationMinutes(60);
        session.setNotes("Chapter 5 exercises");
        session.setStartTime(LocalDateTime.now());
        session.setEndTime(LocalDateTime.now().plusMinutes(60));

        assertEquals("Math Homework", session.getTaskName());
        assertEquals("Homework", session.getTaskType());
        assertEquals(3, session.getDifficultyLevel());
        assertEquals(2, session.getFatigueLevel());
        assertEquals(60, session.getDurationMinutes());
        assertEquals("Chapter 5 exercises", session.getNotes());
        assertNotNull(session.getStartTime());
        assertNotNull(session.getEndTime());
    }

    @Test
    @DisplayName("Test invalid task name")
    void testInvalidTaskName() {
        StudySession session = new StudySession();
        assertThrows(IllegalArgumentException.class, () -> {
            session.setTaskName("");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            session.setTaskName(null);
        });
    }

    @Test
    @DisplayName("Test invalid task type")
    void testInvalidTaskType() {
        StudySession session = new StudySession();
        assertThrows(IllegalArgumentException.class, () -> {
            session.setTaskType("");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            session.setTaskType(null);
        });
    }

    @Test
    @DisplayName("Test invalid difficulty level")
    void testInvalidDifficultyLevel() {
        StudySession session = new StudySession();
        assertThrows(IllegalArgumentException.class, () -> {
            session.setDifficultyLevel(0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            session.setDifficultyLevel(6);
        });
    }

    @Test
    @DisplayName("Test invalid fatigue level")
    void testInvalidFatigueLevel() {
        StudySession session = new StudySession();
        assertThrows(IllegalArgumentException.class, () -> {
            session.setFatigueLevel(0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            session.setFatigueLevel(6);
        });
    }

    @Test
    @DisplayName("Test invalid duration")
    void testInvalidDuration() {
        StudySession session = new StudySession();
        assertThrows(IllegalArgumentException.class, () -> {
            session.setDurationMinutes(0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            session.setDurationMinutes(-1);
        });
    }

    @Test
    @DisplayName("Test time range validation")
    void testTimeRangeValidation() {
        StudySession session = new StudySession();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime future = now.plusHours(1);

        session.setStartTime(now);
        session.setEndTime(future);

        assertEquals(now, session.getStartTime());
        assertEquals(future, session.getEndTime());
    }

    @Test
    @DisplayName("Test invalid time range")
    void testInvalidTimeRange() {
        StudySession session = new StudySession();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime past = now.minusHours(1);

        assertThrows(IllegalArgumentException.class, () -> {
            session.setStartTime(now);
            session.setEndTime(past);
        });
    }

    @Test
    @DisplayName("Test toString method")
    void testToString() {
        StudySession session = new StudySession();
        session.setTaskName("Test Task");
        session.setTaskType("Test Type");
        session.setDifficultyLevel(3);
        session.setFatigueLevel(2);
        session.setDurationMinutes(60);

        String expected = "StudySession{id=0, taskName='Test Task', taskType='Test Type', " +
                         "difficultyLevel=3, fatigueLevel=2, durationMinutes=60}";
        assertEquals(expected, session.toString());
    }
} 