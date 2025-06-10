-- Create database if not exists
CREATE DATABASE IF NOT EXISTS focusmind_studyflow;
USE focusmind_studyflow;

-- Study sessions table
CREATE TABLE IF NOT EXISTS study_sessions (
    session_id INT PRIMARY KEY AUTO_INCREMENT,
    task_name VARCHAR(100) NOT NULL,
    task_type VARCHAR(50) NOT NULL,
    difficulty_level INT NOT NULL CHECK (difficulty_level BETWEEN 1 AND 5),
    fatigue_level INT NOT NULL CHECK (fatigue_level BETWEEN 1 AND 5),
    duration_minutes INT NOT NULL CHECK (duration_minutes > 0),
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    notes TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT check_end_after_start CHECK (end_time > start_time)
);

-- Chat messages table
CREATE TABLE IF NOT EXISTS chat_messages (
    message_id INT PRIMARY KEY AUTO_INCREMENT,
    session_id INT,
    message TEXT NOT NULL,
    response TEXT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES study_sessions(session_id) ON DELETE SET NULL
);

-- Adaptation rules table
CREATE TABLE IF NOT EXISTS adapt_rules (
    rule_id INT PRIMARY KEY AUTO_INCREMENT,
    condition_text VARCHAR(255) NOT NULL,
    adjustment_minutes INT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_study_sessions_start_time ON study_sessions(start_time);
CREATE INDEX idx_study_sessions_task_type ON study_sessions(task_type);
CREATE INDEX idx_chat_messages_session_id ON chat_messages(session_id);
CREATE INDEX idx_adapt_rules_is_active ON adapt_rules(is_active); 