package dao.impl;

import dao.StudySessionDAO;
import model.StudySession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;

public class StudySessionDAOImplTest {
    private StudySessionDAO studySessionDAO;
    private StudySession testSession;

    @BeforeEach
    void setUp() {
        studySessionDAO = new StudySessionDAOImpl();
        testSession = new StudySession();
        testSession.setTaskName("Test Task");
        testSession.setTaskType("Test Type");
        testSession.setDifficultyLevel(3);
        testSession.setFatigueLevel(2);
        testSession.setDurationMinutes(30);
        testSession.setStartTime(LocalDateTime.now());
        testSession.setEndTime(LocalDateTime.now().plusMinutes(30));
        testSession.setNotes("Test notes");
    }

    @AfterEach
    void tearDown() {
        // Clean up test data if needed
    }

    @Test
    void testCreateAndGetById() {
        try {
            int sessionId = studySessionDAO.create(testSession);
            assertTrue(sessionId > 0);

            StudySession retrieved = studySessionDAO.getById(sessionId);
            assertNotNull(retrieved);
            assertEquals(testSession.getTaskName(), retrieved.getTaskName());
            assertEquals(testSession.getTaskType(), retrieved.getTaskType());
            assertEquals(testSession.getDifficultyLevel(), retrieved.getDifficultyLevel());
            assertEquals(testSession.getFatigueLevel(), retrieved.getFatigueLevel());
            assertEquals(testSession.getDurationMinutes(), retrieved.getDurationMinutes());
            assertEquals(testSession.getNotes(), retrieved.getNotes());
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    void testGetAll() {
        try {
            // Create multiple test sessions
            int sessionId1 = studySessionDAO.create(testSession);
            
            StudySession session2 = new StudySession();
            session2.setTaskName("Test Task 2");
            session2.setTaskType("Test Type 2");
            session2.setDifficultyLevel(4);
            session2.setFatigueLevel(3);
            session2.setDurationMinutes(45);
            session2.setStartTime(LocalDateTime.now());
            session2.setEndTime(LocalDateTime.now().plusMinutes(45));
            int sessionId2 = studySessionDAO.create(session2);

            List<StudySession> sessions = studySessionDAO.getAll();
            assertNotNull(sessions);
            assertTrue(sessions.size() >= 2);
            
            boolean foundSession1 = false;
            boolean foundSession2 = false;
            for (StudySession session : sessions) {
                if (session.getSessionId() == sessionId1) {
                    foundSession1 = true;
                }
                if (session.getSessionId() == sessionId2) {
                    foundSession2 = true;
                }
            }
            assertTrue(foundSession1 && foundSession2);
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    void testUpdate() {
        try {
            int sessionId = studySessionDAO.create(testSession);
            assertTrue(sessionId > 0);

            StudySession updated = studySessionDAO.getById(sessionId);
            updated.setTaskName("Updated Task");
            updated.setDifficultyLevel(5);
            updated.setNotes("Updated notes");

            boolean success = studySessionDAO.update(updated);
            assertTrue(success);

            StudySession retrieved = studySessionDAO.getById(sessionId);
            assertEquals("Updated Task", retrieved.getTaskName());
            assertEquals(5, retrieved.getDifficultyLevel());
            assertEquals("Updated notes", retrieved.getNotes());
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    void testDelete() {
        try {
            int sessionId = studySessionDAO.create(testSession);
            assertTrue(sessionId > 0);

            boolean success = studySessionDAO.delete(sessionId);
            assertTrue(success);

            StudySession deleted = studySessionDAO.getById(sessionId);
            assertNull(deleted);
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    void testGetByDateRange() {
        try {
            LocalDateTime start = LocalDateTime.now().minusHours(1);
            LocalDateTime end = LocalDateTime.now().plusHours(1);
            
            int sessionId = studySessionDAO.create(testSession);
            assertTrue(sessionId > 0);

            List<StudySession> sessions = studySessionDAO.getByDateRange(start, end);
            assertNotNull(sessions);
            assertTrue(sessions.size() >= 1);
            
            boolean found = false;
            for (StudySession session : sessions) {
                if (session.getSessionId() == sessionId) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    void testGetByFatigueLevel() {
        try {
            int sessionId = studySessionDAO.create(testSession);
            assertTrue(sessionId > 0);

            List<StudySession> sessions = studySessionDAO.getByFatigueLevel(testSession.getFatigueLevel());
            assertNotNull(sessions);
            assertTrue(sessions.size() >= 1);
            
            boolean found = false;
            for (StudySession session : sessions) {
                if (session.getSessionId() == sessionId) {
                    found = true;
                    assertEquals(testSession.getFatigueLevel(), session.getFatigueLevel());
                    break;
                }
            }
            assertTrue(found);
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    void testGetByDifficultyLevel() {
        try {
            int sessionId = studySessionDAO.create(testSession);
            assertTrue(sessionId > 0);

            List<StudySession> sessions = studySessionDAO.getByDifficultyLevel(testSession.getDifficultyLevel());
            assertNotNull(sessions);
            assertTrue(sessions.size() >= 1);
            
            boolean found = false;
            for (StudySession session : sessions) {
                if (session.getSessionId() == sessionId) {
                    found = true;
                    assertEquals(testSession.getDifficultyLevel(), session.getDifficultyLevel());
                    break;
                }
            }
            assertTrue(found);
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }
} 