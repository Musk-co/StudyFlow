package model;

import java.time.LocalDateTime;

public class StudySession {
    private int sessionId;
    private String taskName;
    private String taskType;
    private int difficultyLevel;
    private int fatigueLevel;
    private int durationMinutes;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public StudySession() {}

    // Getters and Setters
    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        if (difficultyLevel >= 1 && difficultyLevel <= 5) {
            this.difficultyLevel = difficultyLevel;
        } else {
            throw new IllegalArgumentException("Difficulty level must be between 1 and 5");
        }
    }

    public int getFatigueLevel() {
        return fatigueLevel;
    }

    public void setFatigueLevel(int fatigueLevel) {
        if (fatigueLevel >= 1 && fatigueLevel <= 5) {
            this.fatigueLevel = fatigueLevel;
        } else {
            throw new IllegalArgumentException("Fatigue level must be between 1 and 5");
        }
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        if (durationMinutes > 0) {
            this.durationMinutes = durationMinutes;
        } else {
            throw new IllegalArgumentException("Duration must be positive");
        }
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "StudySession{" +
                "sessionId=" + sessionId +
                ", taskName='" + taskName + '\'' +
                ", taskType='" + taskType + '\'' +
                ", difficultyLevel=" + difficultyLevel +
                ", fatigueLevel=" + fatigueLevel +
                ", durationMinutes=" + durationMinutes +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
} 