-- Create database
CREATE DATABASE IF NOT EXISTS focusmind_studyflow;
USE focusmind_studyflow;

-- Create study_sessions table
CREATE TABLE IF NOT EXISTS study_sessions (
    session_id INT PRIMARY KEY AUTO_INCREMENT,
    task_name VARCHAR(255) NOT NULL,
    task_type VARCHAR(50) NOT NULL,
    difficulty_level INT CHECK (difficulty_level BETWEEN 1 AND 5),
    fatigue_level INT CHECK (fatigue_level BETWEEN 1 AND 5),
    duration_minutes INT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create adapt_rules table
CREATE TABLE IF NOT EXISTS adapt_rules (
    rule_id INT PRIMARY KEY AUTO_INCREMENT,
    rule_name VARCHAR(255) NOT NULL,
    condition_text TEXT NOT NULL,
    action_text TEXT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_study_sessions_dates ON study_sessions(start_time, end_time);
CREATE INDEX idx_adapt_rules_active ON adapt_rules(is_active); 