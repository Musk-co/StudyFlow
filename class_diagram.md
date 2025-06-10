# FocusMind StudyFlow - Class Diagram

```mermaid
classDiagram
    class StudySession {
        -int id
        -String taskType
        -int difficulty
        -int fatigueLevel
        -int duration
        -String notes
        -LocalDateTime createdAt
        +StudySession()
        +StudySession(taskType, difficulty, fatigueLevel, duration, notes)
        +getId() int
        +getTaskType() String
        +getDifficulty() int
        +getFatigueLevel() int
        +getDuration() int
        +getNotes() String
        +getCreatedAt() LocalDateTime
        +setTaskType(String)
        +setDifficulty(int)
        +setFatigueLevel(int)
        +setDuration(int)
        +setNotes(String)
    }

    class AdaptRule {
        -int id
        -String condition
        -int adjustment
        -boolean isActive
        -LocalDateTime createdAt
        +AdaptRule()
        +AdaptRule(condition, adjustment)
        +getId() int
        +getCondition() String
        +getAdjustment() int
        +isActive() boolean
        +getCreatedAt() LocalDateTime
        +setCondition(String)
        +setAdjustment(int)
        +setActive(boolean)
    }

    class ChatMessage {
        -int id
        -int sessionId
        -String message
        -String response
        -LocalDateTime createdAt
        +ChatMessage()
        +ChatMessage(sessionId, message, response)
        +getId() int
        +getSessionId() int
        +getMessage() String
        +getResponse() String
        +getCreatedAt() LocalDateTime
        +setMessage(String)
        +setResponse(String)
    }

    class StudySessionDAO {
        <<interface>>
        +save(StudySession) void
        +findById(int) StudySession
        +findAll() List~StudySession~
        +update(StudySession) void
        +delete(int) void
    }

    class AdaptRuleDAO {
        <<interface>>
        +save(AdaptRule) void
        +findById(int) AdaptRule
        +findAll() List~AdaptRule~
        +findActive() List~AdaptRule~
        +update(AdaptRule) void
        +delete(int) void
    }

    class ChatMessageDAO {
        <<interface>>
        +save(ChatMessage) void
        +findBySessionId(int) List~ChatMessage~
        +findAll() List~ChatMessage~
        +update(ChatMessage) void
        +delete(int) void
    }

    class RuleEngine {
        -List~AdaptRule~ rules
        +RuleEngine()
        +addRule(AdaptRule) void
        +removeRule(int) void
        +applyRules(StudySession) int
        +getActiveRules() List~AdaptRule~
    }

    class DBUtil {
        -static String URL
        -static String USER
        -static String PASSWORD
        +getConnection() Connection
        +closeConnection(Connection) void
    }

    class LoggerServlet {
        -StudySessionDAO sessionDAO
        -RuleEngine ruleEngine
        +doPost(HttpServletRequest, HttpServletResponse) void
        +doGet(HttpServletRequest, HttpServletResponse) void
    }

    class DashboardServlet {
        -StudySessionDAO sessionDAO
        -AdaptRuleDAO ruleDAO
        +doGet(HttpServletRequest, HttpServletResponse) void
    }

    class RuleServlet {
        -AdaptRuleDAO ruleDAO
        +doPost(HttpServletRequest, HttpServletResponse) void
        +doGet(HttpServletRequest, HttpServletResponse) void
    }

    class ChatServlet {
        -ChatMessageDAO chatDAO
        -StudySessionDAO sessionDAO
        +doPost(HttpServletRequest, HttpServletResponse) void
        +doGet(HttpServletRequest, HttpServletResponse) void
    }

    StudySessionDAO <|.. StudySession
    AdaptRuleDAO <|.. AdaptRule
    ChatMessageDAO <|.. ChatMessage
    LoggerServlet --> StudySessionDAO
    LoggerServlet --> RuleEngine
    DashboardServlet --> StudySessionDAO
    DashboardServlet --> AdaptRuleDAO
    RuleServlet --> AdaptRuleDAO
    ChatServlet --> ChatMessageDAO
    ChatServlet --> StudySessionDAO
    RuleEngine --> AdaptRuleDAO
```

## Class Descriptions

### Model Classes

- **StudySession**: Represents a study session with task details and cognitive load metrics
- **AdaptRule**: Defines rules for adapting study sessions based on conditions
- **ChatMessage**: Stores chat interactions related to study sessions

### DAO Interfaces

- **StudySessionDAO**: Data access interface for StudySession entities
- **AdaptRuleDAO**: Data access interface for AdaptRule entities
- **ChatMessageDAO**: Data access interface for ChatMessage entities

### Utility Classes

- **RuleEngine**: Manages and applies adaptation rules to study sessions
- **DBUtil**: Handles database connection management

### Servlet Classes

- **LoggerServlet**: Handles study session logging and rule application
- **DashboardServlet**: Manages dashboard data and statistics
- **RuleServlet**: Handles rule management operations
- **ChatServlet**: Manages chat interactions and AI responses

## Relationships

1. **DAO to Model**: Each DAO interface is implemented for its corresponding model class
2. **Servlet to DAO**: Servlets use DAOs to interact with the database
3. **RuleEngine to DAO**: RuleEngine uses AdaptRuleDAO to access rules
4. **Servlet to RuleEngine**: LoggerServlet uses RuleEngine to apply rules

## Notes

- All model classes include getters and setters for their properties
- DAO interfaces define standard CRUD operations
- Servlets handle HTTP requests and responses
- RuleEngine implements the business logic for rule application
- DBUtil provides centralized database connection management
