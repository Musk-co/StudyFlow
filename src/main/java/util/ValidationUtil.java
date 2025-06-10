package util;

public class ValidationUtil {
    private static final int MIN_MESSAGE_LENGTH = 1;
    private static final int MAX_MESSAGE_LENGTH = 1000;
    private static final String API_KEY_PATTERN = "^[A-Za-z0-9_-]{39}$";

    public static boolean isValidMessageLength(String message) {
        return message != null && 
               message.length() >= MIN_MESSAGE_LENGTH && 
               message.length() <= MAX_MESSAGE_LENGTH;
    }

    public static boolean isValidApiKey(String apiKey) {
        return apiKey != null && apiKey.matches(API_KEY_PATTERN);
    }

    public static boolean isValidSessionId(Long sessionId) {
        return sessionId != null && sessionId > 0;
    }

    public static boolean isValidTaskName(String taskName) {
        return taskName != null && !taskName.trim().isEmpty() && taskName.length() <= 100;
    }

    public static boolean isValidTaskType(String taskType) {
        return taskType != null && !taskType.trim().isEmpty() && taskType.length() <= 50;
    }

    public static boolean isValidDifficultyLevel(int level) {
        return level >= 1 && level <= 5;
    }

    public static boolean isValidFatigueLevel(int level) {
        return level >= 1 && level <= 5;
    }

    public static boolean isValidDurationMinutes(int minutes) {
        return minutes > 0 && minutes <= 480; // Max 8 hours
    }

    public static boolean isValidNotes(String notes) {
        return notes == null || notes.length() <= 1000;
    }
} 