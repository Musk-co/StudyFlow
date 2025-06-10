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
        response.setContentType("application/json");
        JsonObject jsonResponse = new JsonObject();
        try {
            // Read request body
            BufferedReader reader = request.getReader();
            JsonObject requestJson = JsonParser.parseReader(reader).getAsJsonObject();
            String message = requestJson.get("message").getAsString();
            String apiKey = requestJson.get("apiKey").getAsString();
            Long sessionId = requestJson.has("sessionId") ? requestJson.get("sessionId").getAsLong() : null;

            // Check for greeting messages
            if (message.toLowerCase().contains("hello") || message.toLowerCase().contains("hi")) {
                jsonResponse.addProperty("response", "Hello! I'm your study assistant. How can I help you today?");
                response.getWriter().write(jsonResponse.toString());
                return;
            }

            // Prepare context for Gemini API
            String context = "";
            if (sessionId != null) {
                try {
                    StudySession session = studySessionDAO.getById(sessionId.intValue());
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
                } catch (Exception e) {
                    e.printStackTrace();
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
            jsonResponse.addProperty("response", geminiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.addProperty("response", "Error communicating with Gemini API: " + e.getMessage());
        }
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

        int status = conn.getResponseCode();
        BufferedReader reader;
        if (status >= 200 && status < 300) {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Parse Gemini response
        JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
        if (jsonResponse.has("candidates")) {
            // Adjust this according to the actual Gemini API response structure
            try {
                // Gemini API returns: { candidates: [ { content: { parts: [ { text: "..." } ] } } ] }
                JsonObject candidate = jsonResponse.getAsJsonArray("candidates").get(0).getAsJsonObject();
                JsonObject content = candidate.getAsJsonObject("content");
                String text = content.getAsJsonArray("parts").get(0).getAsJsonObject().get("text").getAsString();
                return text;
            } catch (Exception e) {
                return "Gemini API response parsing error: " + e.getMessage();
            }
        } else if (jsonResponse.has("error")) {
            return "Gemini API error: " + jsonResponse.get("error").getAsJsonObject().get("message").getAsString();
        } else {
            return "Unknown response from Gemini API.";
        }
    }
} 