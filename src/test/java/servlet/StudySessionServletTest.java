package servlet;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.StudySession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class StudySessionServletTest {
    private StudySessionServlet studySessionServlet;
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        studySessionServlet = new StudySessionServlet();
        studySessionServlet.init();
    }
    
    @Test
    void testDoGetGetAll() throws Exception {
        // Mock request parameters
        when(request.getParameter("action")).thenReturn("getAll");
        
        // Mock response writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        
        // Call the servlet
        studySessionServlet.doGet(request, response);
        
        // Verify response
        writer.flush();
        String responseJson = stringWriter.toString();
        JsonObject responseObj = JsonParser.parseString(responseJson).getAsJsonObject();
        
        assertTrue(responseObj.has("success"));
        assertTrue(responseObj.has("sessions"));
        assertEquals("true", responseObj.get("success").getAsString());
    }
    
    @Test
    void testDoGetGetById() throws Exception {
        // Mock request parameters
        when(request.getParameter("action")).thenReturn("getById");
        when(request.getParameter("id")).thenReturn("1");
        
        // Mock response writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        
        // Call the servlet
        studySessionServlet.doGet(request, response);
        
        // Verify response
        writer.flush();
        String responseJson = stringWriter.toString();
        JsonObject responseObj = JsonParser.parseString(responseJson).getAsJsonObject();
        
        assertTrue(responseObj.has("success"));
        assertTrue(responseObj.has("session"));
        assertEquals("true", responseObj.get("success").getAsString());
    }
    
    @Test
    void testDoGetInvalidAction() throws Exception {
        // Mock request parameters
        when(request.getParameter("action")).thenReturn("invalid");
        
        // Mock response writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        
        // Call the servlet
        studySessionServlet.doGet(request, response);
        
        // Verify response
        writer.flush();
        String responseJson = stringWriter.toString();
        JsonObject responseObj = JsonParser.parseString(responseJson).getAsJsonObject();
        
        assertTrue(responseObj.has("success"));
        assertTrue(responseObj.has("error"));
        assertEquals("false", responseObj.get("success").getAsString());
        assertEquals("Invalid action parameter", responseObj.get("error").getAsString());
        
        // Verify status code
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
    
    @Test
    void testDoPostWithValidInput() throws Exception {
        // Prepare test data
        String jsonInput = "{\"taskName\":\"Test Task\",\"taskType\":\"Test Type\"," +
                          "\"difficultyLevel\":3,\"fatigueLevel\":2,\"durationMinutes\":30," +
                          "\"notes\":\"Test notes\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        
        // Mock request and response
        when(request.getReader()).thenReturn(reader);
        when(response.getWriter()).thenReturn(writer);
        
        // Call the servlet
        studySessionServlet.doPost(request, response);
        
        // Verify response
        writer.flush();
        String responseJson = stringWriter.toString();
        JsonObject responseObj = JsonParser.parseString(responseJson).getAsJsonObject();
        
        assertTrue(responseObj.has("success"));
        assertTrue(responseObj.has("message"));
        assertTrue(responseObj.has("session"));
        assertEquals("true", responseObj.get("success").getAsString());
        assertEquals("Study session saved successfully", responseObj.get("message").getAsString());
    }
    
    @Test
    void testDoPostWithMissingFields() throws Exception {
        // Prepare test data with missing fields
        String jsonInput = "{\"taskName\":\"Test Task\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        
        // Mock request and response
        when(request.getReader()).thenReturn(reader);
        when(response.getWriter()).thenReturn(writer);
        
        // Call the servlet
        studySessionServlet.doPost(request, response);
        
        // Verify response
        writer.flush();
        String responseJson = stringWriter.toString();
        JsonObject responseObj = JsonParser.parseString(responseJson).getAsJsonObject();
        
        assertTrue(responseObj.has("success"));
        assertTrue(responseObj.has("error"));
        assertEquals("false", responseObj.get("success").getAsString());
        assertEquals("Missing required fields", responseObj.get("error").getAsString());
        
        // Verify status code
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
    
    @Test
    void testDoPostWithInvalidInput() throws Exception {
        // Prepare test data with invalid input
        String jsonInput = "{\"taskName\":\"Test Task\",\"taskType\":\"Test Type\"," +
                          "\"difficultyLevel\":6,\"fatigueLevel\":2,\"durationMinutes\":30," +
                          "\"notes\":\"Test notes\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        
        // Mock request and response
        when(request.getReader()).thenReturn(reader);
        when(response.getWriter()).thenReturn(writer);
        
        // Call the servlet
        studySessionServlet.doPost(request, response);
        
        // Verify response
        writer.flush();
        String responseJson = stringWriter.toString();
        JsonObject responseObj = JsonParser.parseString(responseJson).getAsJsonObject();
        
        assertTrue(responseObj.has("success"));
        assertTrue(responseObj.has("error"));
        assertEquals("false", responseObj.get("success").getAsString());
        assertEquals("Invalid difficulty level", responseObj.get("error").getAsString());
        
        // Verify status code
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
    
    @Test
    void testDoDelete() throws Exception {
        // Mock request parameters
        when(request.getParameter("id")).thenReturn("1");
        
        // Mock response writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        
        // Call the servlet
        studySessionServlet.doDelete(request, response);
        
        // Verify response
        writer.flush();
        String responseJson = stringWriter.toString();
        JsonObject responseObj = JsonParser.parseString(responseJson).getAsJsonObject();
        
        assertTrue(responseObj.has("success"));
        assertTrue(responseObj.has("message"));
        assertEquals("true", responseObj.get("success").getAsString());
        assertEquals("Study session deleted successfully", responseObj.get("message").getAsString());
    }
    
    @Test
    void testDoDeleteWithMissingId() throws Exception {
        // Mock request parameters
        when(request.getParameter("id")).thenReturn(null);
        
        // Mock response writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        
        // Call the servlet
        studySessionServlet.doDelete(request, response);
        
        // Verify response
        writer.flush();
        String responseJson = stringWriter.toString();
        JsonObject responseObj = JsonParser.parseString(responseJson).getAsJsonObject();
        
        assertTrue(responseObj.has("success"));
        assertTrue(responseObj.has("error"));
        assertEquals("false", responseObj.get("success").getAsString());
        assertEquals("Session ID is required", responseObj.get("error").getAsString());
        
        // Verify status code
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
    
    @Test
    void testDoDeleteWithInvalidId() throws Exception {
        // Mock request parameters
        when(request.getParameter("id")).thenReturn("invalid");
        
        // Mock response writer
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        
        // Call the servlet
        studySessionServlet.doDelete(request, response);
        
        // Verify response
        writer.flush();
        String responseJson = stringWriter.toString();
        JsonObject responseObj = JsonParser.parseString(responseJson).getAsJsonObject();
        
        assertTrue(responseObj.has("success"));
        assertTrue(responseObj.has("error"));
        assertEquals("false", responseObj.get("success").getAsString());
        assertEquals("Invalid ID format", responseObj.get("error").getAsString());
        
        // Verify status code
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
