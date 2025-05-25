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
            // Get form data
            String taskName = request.getParameter("taskName");
            String taskType = request.getParameter("taskType");
            int difficultyLevel = Integer.parseInt(request.getParameter("difficultyLevel"));
            int fatigueLevel = Integer.parseInt(request.getParameter("fatigueLevel"));
            int durationMinutes = Integer.parseInt(request.getParameter("duration"));
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

            // Forward to index.jsp to show updated data
            request.getRequestDispatcher("index.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Error creating study session: " + e.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
} 