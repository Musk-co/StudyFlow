package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.StudySessionDAO;
import dao.impl.StudySessionDAOImpl;
import model.StudySession;
import util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/StudySessionServlet")
public class StudySessionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final Gson gson = new Gson();
    private StudySessionDAO studySessionDAO;

    @Override
    public void init() throws ServletException {
        studySessionDAO = new StudySessionDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        JsonObject jsonResponse = new JsonObject();
        
        try {
            String action = request.getParameter("action");
            
            if ("getAll".equals(action)) {
                List<StudySession> sessions = studySessionDAO.getAll();
                jsonResponse.addProperty("success", true);
                jsonResponse.add("sessions", gson.toJsonTree(sessions));
            } else if ("getById".equals(action)) {
                String idStr = request.getParameter("id");
                if (idStr == null || idStr.trim().isEmpty()) {
                    throw new IllegalArgumentException("Session ID is required");
                }
                
                int id = Integer.parseInt(idStr);
                StudySession session = studySessionDAO.getById(id);
                
                if (session == null) {
                    throw new IllegalArgumentException("Session not found");
                }
                
                jsonResponse.addProperty("success", true);
                jsonResponse.add("session", gson.toJsonTree(session));
            } else {
                throw new IllegalArgumentException("Invalid action parameter");
            }
            
        } catch (NumberFormatException e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("error", "Invalid ID format");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("error", e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("error", "An unexpected error occurred");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log("Error in StudySessionServlet GET: " + e.getMessage());
        }
        
        response.getWriter().write(jsonResponse.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        JsonObject jsonResponse = new JsonObject();
        
        try {
            BufferedReader reader = request.getReader();
            JsonObject requestJson = JsonParser.parseReader(reader).getAsJsonObject();
            
            // Validate required fields
            if (!requestJson.has("taskName") || !requestJson.has("taskType") || 
                !requestJson.has("difficultyLevel") || !requestJson.has("fatigueLevel") || 
                !requestJson.has("durationMinutes")) {
                throw new IllegalArgumentException("Missing required fields");
            }
            
            String taskName = requestJson.get("taskName").getAsString();
            String taskType = requestJson.get("taskType").getAsString();
            int difficultyLevel = requestJson.get("difficultyLevel").getAsInt();
            int fatigueLevel = requestJson.get("fatigueLevel").getAsInt();
            int durationMinutes = requestJson.get("durationMinutes").getAsInt();
            String notes = requestJson.has("notes") ? requestJson.get("notes").getAsString() : null;
            
            // Validate input
            if (!ValidationUtil.isValidTaskName(taskName)) {
                throw new IllegalArgumentException("Invalid task name");
            }
            if (!ValidationUtil.isValidTaskType(taskType)) {
                throw new IllegalArgumentException("Invalid task type");
            }
            if (!ValidationUtil.isValidDifficultyLevel(difficultyLevel)) {
                throw new IllegalArgumentException("Invalid difficulty level");
            }
            if (!ValidationUtil.isValidFatigueLevel(fatigueLevel)) {
                throw new IllegalArgumentException("Invalid fatigue level");
            }
            if (!ValidationUtil.isValidDurationMinutes(durationMinutes)) {
                throw new IllegalArgumentException("Invalid duration");
            }
            if (!ValidationUtil.isValidNotes(notes)) {
                throw new IllegalArgumentException("Notes too long");
            }
            
            StudySession session = new StudySession();
            session.setTaskName(taskName);
            session.setTaskType(taskType);
            session.setDifficultyLevel(difficultyLevel);
            session.setFatigueLevel(fatigueLevel);
            session.setDurationMinutes(durationMinutes);
            session.setNotes(notes);
            
            studySessionDAO.create(session);
            
            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("message", "Study session saved successfully");
            jsonResponse.add("session", gson.toJsonTree(session));
            
        } catch (IllegalArgumentException e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("error", e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("error", "An unexpected error occurred");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log("Error in StudySessionServlet POST: " + e.getMessage());
        }
        
        response.getWriter().write(jsonResponse.toString());
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        JsonObject jsonResponse = new JsonObject();
        
        try {
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Session ID is required");
            }
            
            int id = Integer.parseInt(idStr);
            studySessionDAO.delete(id);
            
            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("message", "Study session deleted successfully");
            
        } catch (NumberFormatException e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("error", "Invalid ID format");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("error", e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("error", "An unexpected error occurred");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log("Error in StudySessionServlet DELETE: " + e.getMessage());
        }
        
        response.getWriter().write(jsonResponse.toString());
    }
} 