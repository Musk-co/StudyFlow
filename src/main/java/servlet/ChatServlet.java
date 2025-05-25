package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.StudySessionDAO;
import dao.impl.StudySessionDAOImpl;
import model.StudySession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ChatServlet")
public class ChatServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent";
    private final Gson gson = new Gson();
    private StudySessionDAO studySessionDAO;

    @Override
    public void init() throws ServletException {
        studySessionDAO = new StudySessionDAOImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Read request body
        BufferedReader reader = request.getReader();
        JsonObject requestJson = JsonParser.parseReader(reader).getAsJsonObject();
        String message = requestJson.get("message").getAsString();
        String apiKey = requestJson.get("apiKey").getAsString();
        Long sessionId = requestJson.has("sessionId") ? requestJson.get("sessionId").getAsLong() : null;

        // Prepare context for Gemini API
        String context = "";
        if (sessionId != null) {
            StudySession session = studySessionDAO.getById(sessionId);
            if (session != null) {
                context = String.format(
                    "Context: This is a study session about '%s' (Type: %s). " +
                    "Difficulty: %d/5, Fatigue: %d/5, Duration: %d minutes. " +
                    "Notes: %s\n\nUser message: ",
                    session.getTaskName(),
                    session.getTaskType(),
                    session.getDifficultyLevel(),
                    session.getFatigueLevel(),
                    session.getDurationMinutes(),
                    session.getNotes() != null ? session.getNotes() : "No notes"
                );
            }
        }

        // Prepare request to Gemini API
        JsonObject requestBody = new JsonObject();
        JsonObject contents = new JsonObject();
        JsonObject text = new JsonObject();
        text.addProperty("text", context + message);
        contents.add("parts", gson.toJsonTree(new Object[]{text}));
        requestBody.add("contents", gson.toJsonTree(new Object[]{contents}));

        // Call Gemini API
        String geminiResponse = callGeminiAPI(requestBody.toString(), apiKey);

        // Send response back to client
        response.setContentType("application/json");
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("response", geminiResponse);
        response.getWriter().write(jsonResponse.toString());
    }

    private String callGeminiAPI(String requestBody, String apiKey) throws IOException {
        URL url = new URL(GEMINI_API_URL + "?key=" + apiKey);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // Send request
        try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
            writer.write(requestBody);
            writer.flush();
        }

        // Read response
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        // Parse response
        JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
        return jsonResponse.getAsJsonObject("candidates")
                         .getAsJsonArray("content")
                         .get(0)
                         .getAsJsonObject()
                         .getAsJsonObject("parts")
                         .getAsJsonArray("text")
                         .get(0)
                         .getAsString();
    }
} 