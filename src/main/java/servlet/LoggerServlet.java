package servlet;

import dao.StudySessionDAO;
import dao.AdaptRuleDAO;
import dao.impl.StudySessionDAOImpl;
import dao.impl.AdaptRuleDAOImpl;
import model.StudySession;
import model.AdaptRule;
import util.RuleEngine;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/LoggerServlet")
public class LoggerServlet extends HttpServlet {
    private StudySessionDAO studySessionDAO;
    private AdaptRuleDAO adaptRuleDAO;

    @Override
    public void init() throws ServletException {
        studySessionDAO = new StudySessionDAOImpl();
        adaptRuleDAO = new AdaptRuleDAOImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Get form data with validation
            String taskName = request.getParameter("taskName");
            if (taskName == null || taskName.trim().isEmpty()) {
                throw new IllegalArgumentException("Task name cannot be empty");
            }

            String taskType = request.getParameter("taskType");
            if (taskType == null || taskType.trim().isEmpty()) {
                throw new IllegalArgumentException("Task type must be selected");
            }

            int difficultyLevel;
            try {
                difficultyLevel = Integer.parseInt(request.getParameter("difficultyLevel"));
                if (difficultyLevel < 1 || difficultyLevel > 5) {
                    throw new IllegalArgumentException("Difficulty level must be between 1 and 5");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid difficulty level format");
            }

            int fatigueLevel;
            try {
                fatigueLevel = Integer.parseInt(request.getParameter("fatigueLevel"));
                if (fatigueLevel < 1 || fatigueLevel > 5) {
                    throw new IllegalArgumentException("Fatigue level must be between 1 and 5");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid fatigue level format");
            }

            int durationMinutes;
            try {
                durationMinutes = Integer.parseInt(request.getParameter("duration"));
                if (durationMinutes < 1 || durationMinutes > 480) {
                    throw new IllegalArgumentException("Duration must be between 1 and 480 minutes");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid duration format");
            }

            String notes = request.getParameter("notes");

            // Create study session
            StudySession session = new StudySession();
            session.setTaskName(taskName);
            session.setTaskType(taskType);
            session.setDifficultyLevel(difficultyLevel);
            session.setFatigueLevel(fatigueLevel);
            session.setDurationMinutes(durationMinutes);
            session.setNotes(notes);
            session.setStartTime(LocalDateTime.now());
            session.setEndTime(LocalDateTime.now().plusMinutes(durationMinutes));

            // Get active rules and adjust duration
            List<AdaptRule> activeRules = adaptRuleDAO.getActiveRules();
            int adjustedDuration = RuleEngine.evaluateAndAdjustDuration(session, activeRules);
            session.setDurationMinutes(adjustedDuration);
            session.setEndTime(session.getStartTime().plusMinutes(adjustedDuration));

            // Save session
            studySessionDAO.create(session);

            // Get recent sessions and stats
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
            
            List<StudySession> todaySessions = studySessionDAO.getByDateRange(startOfDay, now);
            List<StudySession> recentSessions = studySessionDAO.getByDateRange(
                now.minusDays(1),
                now
            );

            // Calculate stats
            int sessionsToday = todaySessions.size();
            double averageFatigue = todaySessions.stream()
                .mapToInt(StudySession::getFatigueLevel)
                .average()
                .orElse(0.0);
            int activeRulesCount = activeRules.size();

            // Set attributes for JSP
            request.setAttribute("todaySessions", sessionsToday);
            request.setAttribute("averageFatigue", String.format("%.1f", averageFatigue));
            request.setAttribute("activeRules", activeRulesCount);
            request.setAttribute("recentSessions", recentSessions);

            // Forward to study_session.jsp to show countdown and UI elements
            request.getRequestDispatcher("study_session.jsp").forward(request, response);

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Validation Error: " + e.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (Exception e) {
            // Log the full stack trace for debugging
            e.printStackTrace();
            request.setAttribute("error", "System Error: An unexpected error occurred. Please try again later.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
} 