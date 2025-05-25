package servlet;

import dao.StudySessionDAO;
import dao.AdaptRuleDAO;
import dao.impl.StudySessionDAOImpl;
import dao.impl.AdaptRuleDAOImpl;
import model.StudySession;
import model.AdaptRule;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {
    private StudySessionDAO studySessionDAO;
    private AdaptRuleDAO adaptRuleDAO;

    @Override
    public void init() throws ServletException {
        studySessionDAO = new StudySessionDAOImpl();
        adaptRuleDAO = new AdaptRuleDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Get date range for statistics
            LocalDateTime end = LocalDateTime.now();
            LocalDateTime start = end.minusDays(7);

            // Get sessions for the date range
            List<StudySession> sessions = studySessionDAO.getByDateRange(start, end);
            List<StudySession> todaySessions = sessions.stream()
                    .filter(s -> s.getStartTime().toLocalDate().equals(LocalDateTime.now().toLocalDate()))
                    .collect(Collectors.toList());

            // Calculate statistics
            int totalStudyTime = calculateTotalStudyTime(sessions);
            int sessionsToday = todaySessions.size();
            double averageFatigue = calculateAverageFatigue(sessions);
            int activeRules = adaptRuleDAO.getActiveRules().size();

            // Prepare chart data
            List<String> fatigueLabels = new ArrayList<>();
            List<Integer> fatigueData = new ArrayList<>();
            Map<String, Integer> taskDistribution = new HashMap<>();

            // Group sessions by date and calculate average fatigue
            Map<LocalDateTime, Double> dailyFatigue = sessions.stream()
                    .collect(Collectors.groupingBy(
                            s -> s.getStartTime().toLocalDate().atStartOfDay(),
                            Collectors.averagingDouble(StudySession::getFatigueLevel)
                    ));

            // Sort by date and prepare fatigue chart data
            dailyFatigue.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        fatigueLabels.add(entry.getKey().format(DateTimeFormatter.ofPattern("MM/dd")));
                        fatigueData.add((int) Math.round(entry.getValue()));
                    });

            // Calculate task distribution
            sessions.forEach(session -> {
                taskDistribution.merge(session.getTaskType(), session.getDurationMinutes(), Integer::sum);
            });

            // Set attributes for JSP
            request.setAttribute("totalStudyTime", totalStudyTime);
            request.setAttribute("sessionsToday", sessionsToday);
            request.setAttribute("averageFatigue", String.format("%.1f", averageFatigue));
            request.setAttribute("activeRules", activeRules);
            request.setAttribute("fatigueLabels", fatigueLabels);
            request.setAttribute("fatigueData", fatigueData);
            request.setAttribute("taskTypes", new ArrayList<>(taskDistribution.keySet()));
            request.setAttribute("taskDistribution", new ArrayList<>(taskDistribution.values()));
            request.setAttribute("recentSessions", sessions.stream()
                    .limit(5)
                    .collect(Collectors.toList()));

            // Forward to dashboard JSP
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Error loading dashboard: " + e.getMessage());
            request.getRequestDispatcher("error/500.jsp").forward(request, response);
        }
    }

    private int calculateTotalStudyTime(List<StudySession> sessions) {
        return sessions.stream()
                .mapToInt(StudySession::getDurationMinutes)
                .sum() / 60; // Convert to hours
    }

    private double calculateAverageFatigue(List<StudySession> sessions) {
        if (sessions.isEmpty()) return 0.0;
        return sessions.stream()
                .mapToInt(StudySession::getFatigueLevel)
                .average()
                .orElse(0.0);
    }
} 