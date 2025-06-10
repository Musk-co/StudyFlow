package model;

import java.time.LocalDateTime;
import util.ValidationUtil;

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
    public StudySession() {
        this.startTime = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

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
        if (!ValidationUtil.isValidTaskName(taskName)) {
            throw new IllegalArgumentException("Invalid task name");
        }
        this.taskName = taskName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        if (!ValidationUtil.isValidTaskType(taskType)) {
            throw new IllegalArgumentException("Invalid task type");
        }
        this.taskType = taskType;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        if (!ValidationUtil.isValidDifficultyLevel(difficultyLevel)) {
            throw new IllegalArgumentException("Invalid difficulty level");
        }
        this.difficultyLevel = difficultyLevel;
    }

    public int getFatigueLevel() {
        return fatigueLevel;
    }

    public void setFatigueLevel(int fatigueLevel) {
        if (!ValidationUtil.isValidFatigueLevel(fatigueLevel)) {
            throw new IllegalArgumentException("Invalid fatigue level");
        }
        this.fatigueLevel = fatigueLevel;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        if (!ValidationUtil.isValidDurationMinutes(durationMinutes)) {
            throw new IllegalArgumentException("Invalid duration");
        }
        this.durationMinutes = durationMinutes;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        if (startTime == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        }
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        if (endTime == null) {
            throw new IllegalArgumentException("End time cannot be null");
        }
        if (startTime != null && endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }
        this.endTime = endTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        if (!ValidationUtil.isValidNotes(notes)) {
            throw new IllegalArgumentException("Notes too long");
        }
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        if (createdAt == null) {
            throw new IllegalArgumentException("Created at time cannot be null");
        }
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        if (updatedAt == null) {
            throw new IllegalArgumentException("Updated at time cannot be null");
        }
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
                ", notes='" + notes + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
} 