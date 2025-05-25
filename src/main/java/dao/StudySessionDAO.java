package dao;

import model.StudySession;
import java.util.List;
import java.time.LocalDateTime;

public interface StudySessionDAO {
    // Create a new study session
    int create(StudySession session) throws Exception;
    
    // Retrieve a study session by ID
    StudySession getById(int sessionId) throws Exception;
    
    // Update an existing study session
    boolean update(StudySession session) throws Exception;
    
    // Delete a study session
    boolean delete(int sessionId) throws Exception;
    
    // Get all study sessions
    List<StudySession> getAll() throws Exception;
    
    // Get study sessions within a date range
    List<StudySession> getByDateRange(LocalDateTime start, LocalDateTime end) throws Exception;
    
    // Get study sessions by fatigue level
    List<StudySession> getByFatigueLevel(int fatigueLevel) throws Exception;
    
    // Get study sessions by difficulty level
    List<StudySession> getByDifficultyLevel(int difficultyLevel) throws Exception;
} 