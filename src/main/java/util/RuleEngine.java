package util;

import model.AdaptRule;
import model.StudySession;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleEngine {
    
    public static int evaluateAndAdjustDuration(StudySession session, List<AdaptRule> rules) {
        int adjustedDuration = session.getDurationMinutes();
        
        for (AdaptRule rule : rules) {
            if (!rule.isActive()) continue;
            
            if (evaluateCondition(rule.getConditionText(), session)) {
                adjustedDuration = applyAction(rule.getActionText(), adjustedDuration);
            }
        }
        
        return adjustedDuration;
    }
    
    private static boolean evaluateCondition(String condition, StudySession session) {
        // Parse condition like "fatigue_level > 3 AND difficulty_level > 2"
        String[] parts = condition.split("AND");
        
        for (String part : parts) {
            part = part.trim();
            
            if (part.contains("fatigue_level")) {
                if (!evaluateFatigueCondition(part, session.getFatigueLevel())) {
                    return false;
                }
            } else if (part.contains("difficulty_level")) {
                if (!evaluateDifficultyCondition(part, session.getDifficultyLevel())) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private static boolean evaluateFatigueCondition(String condition, int fatigueLevel) {
        Pattern pattern = Pattern.compile("fatigue_level\\s*([<>=!]+)\\s*(\\d+)");
        Matcher matcher = pattern.matcher(condition);
        
        if (matcher.find()) {
            String operator = matcher.group(1);
            int value = Integer.parseInt(matcher.group(2));
            
            switch (operator) {
                case ">": return fatigueLevel > value;
                case "<": return fatigueLevel < value;
                case ">=": return fatigueLevel >= value;
                case "<=": return fatigueLevel <= value;
                case "==": return fatigueLevel == value;
                case "!=": return fatigueLevel != value;
            }
        }
        
        return false;
    }
    
    private static boolean evaluateDifficultyCondition(String condition, int difficultyLevel) {
        Pattern pattern = Pattern.compile("difficulty_level\\s*([<>=!]+)\\s*(\\d+)");
        Matcher matcher = pattern.matcher(condition);
        
        if (matcher.find()) {
            String operator = matcher.group(1);
            int value = Integer.parseInt(matcher.group(2));
            
            switch (operator) {
                case ">": return difficultyLevel > value;
                case "<": return difficultyLevel < value;
                case ">=": return difficultyLevel >= value;
                case "<=": return difficultyLevel <= value;
                case "==": return difficultyLevel == value;
                case "!=": return difficultyLevel != value;
            }
        }
        
        return false;
    }
    
    private static int applyAction(String action, int currentDuration) {
        // Parse action like "reduce_duration_by 20%" or "increase_duration_by 10%"
        Pattern pattern = Pattern.compile("(reduce|increase)_duration_by\\s*(\\d+)%");
        Matcher matcher = pattern.matcher(action);
        
        if (matcher.find()) {
            String operation = matcher.group(1);
            int percentage = Integer.parseInt(matcher.group(2));
            
            if (operation.equals("reduce")) {
                return currentDuration * (100 - percentage) / 100;
            } else {
                return currentDuration * (100 + percentage) / 100;
            }
        }
        
        return currentDuration;
    }
} 