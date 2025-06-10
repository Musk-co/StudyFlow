package dao.impl;

import dao.StudySessionDAO;
import model.StudySession;
import util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StudySessionDAOImpl implements StudySessionDAO {
    
    @Override
    public int create(StudySession session) throws Exception {
        String sql = "INSERT INTO study_sessions (task_name, task_type, difficulty_level, fatigue_level, " +
                    "duration_minutes, start_time, end_time, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, session.getTaskName());
            pstmt.setString(2, session.getTaskType());
            pstmt.setInt(3, session.getDifficultyLevel());
            pstmt.setInt(4, session.getFatigueLevel());
            pstmt.setInt(5, session.getDurationMinutes());
            pstmt.setTimestamp(6, Timestamp.valueOf(session.getStartTime()));
            pstmt.setTimestamp(7, Timestamp.valueOf(session.getEndTime()));
            pstmt.setString(8, session.getNotes());
            
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;
        }
    }
    
    @Override
    public StudySession getById(int sessionId) throws Exception {
        String sql = "SELECT * FROM study_sessions WHERE session_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, sessionId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStudySession(rs);
                }
            }
            return null;
        }
    }
    
    @Override
    public boolean update(StudySession session) throws Exception {
        String sql = "UPDATE study_sessions SET task_name = ?, task_type = ?, difficulty_level = ?, " +
                    "fatigue_level = ?, duration_minutes = ?, start_time = ?, end_time = ?, notes = ? " +
                    "WHERE session_id = ?";
                    
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, session.getTaskName());
            pstmt.setString(2, session.getTaskType());
            pstmt.setInt(3, session.getDifficultyLevel());
            pstmt.setInt(4, session.getFatigueLevel());
            pstmt.setInt(5, session.getDurationMinutes());
            pstmt.setTimestamp(6, Timestamp.valueOf(session.getStartTime()));
            pstmt.setTimestamp(7, Timestamp.valueOf(session.getEndTime()));
            pstmt.setString(8, session.getNotes());
            pstmt.setInt(9, session.getSessionId());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean delete(int sessionId) throws Exception {
        String sql = "DELETE FROM study_sessions WHERE session_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, sessionId);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public List<StudySession> getAll() throws Exception {
        String sql = "SELECT * FROM study_sessions ORDER BY start_time DESC";
        List<StudySession> sessions = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                sessions.add(mapResultSetToStudySession(rs));
            }
        }
        return sessions;
    }
    
    @Override
    public List<StudySession> getByDateRange(LocalDateTime start, LocalDateTime end) throws Exception {
        String sql = "SELECT * FROM study_sessions WHERE start_time BETWEEN ? AND ? ORDER BY start_time";
        List<StudySession> sessions = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setTimestamp(1, Timestamp.valueOf(start));
            pstmt.setTimestamp(2, Timestamp.valueOf(end));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    sessions.add(mapResultSetToStudySession(rs));
                }
            }
        }
        return sessions;
    }
    
    @Override
    public List<StudySession> getByFatigueLevel(int fatigueLevel) throws Exception {
        String sql = "SELECT * FROM study_sessions WHERE fatigue_level = ? ORDER BY start_time DESC";
        List<StudySession> sessions = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, fatigueLevel);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    sessions.add(mapResultSetToStudySession(rs));
                }
            }
        }
        return sessions;
    }
    
    @Override
    public List<StudySession> getByDifficultyLevel(int difficultyLevel) throws Exception {
        String sql = "SELECT * FROM study_sessions WHERE difficulty_level = ? ORDER BY start_time DESC";
        List<StudySession> sessions = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, difficultyLevel);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    sessions.add(mapResultSetToStudySession(rs));
                }
            }
        }
        return sessions;
    }
    
    private StudySession mapResultSetToStudySession(ResultSet rs) throws SQLException {
        StudySession session = new StudySession();
        session.setSessionId(rs.getInt("session_id"));
        session.setTaskName(rs.getString("task_name"));
        session.setTaskType(rs.getString("task_type"));
        session.setDifficultyLevel(rs.getInt("difficulty_level"));
        session.setFatigueLevel(rs.getInt("fatigue_level"));
        session.setDurationMinutes(rs.getInt("duration_minutes"));
        session.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
        session.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
        session.setNotes(rs.getString("notes"));
        session.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        session.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return session;
    }
} 