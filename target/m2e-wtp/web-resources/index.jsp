<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>FocusMind StudyFlow</title>
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
      rel="stylesheet"
    />
    <link
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
      rel="stylesheet"
    />
    <style>
      .emoji-slider {
        width: 100%;
        margin: 20px 0;
      }
      .task-card {
        transition: transform 0.2s;
      }
      .task-card:hover {
        transform: translateY(-5px);
      }
      .fatigue-indicator {
        font-size: 2rem;
        margin: 10px 0;
      }
      .chat-container {
        height: 300px;
        overflow-y: auto;
        border: 1px solid #dee2e6;
        border-radius: 0.25rem;
        padding: 1rem;
        margin-bottom: 1rem;
        background-color: #f8f9fa;
      }
      .chat-message {
        margin-bottom: 1rem;
        padding: 0.5rem 1rem;
        border-radius: 1rem;
        max-width: 80%;
      }
      .user-message {
        background-color: #007bff;
        color: white;
        margin-left: auto;
      }
      .ai-message {
        background-color: #e9ecef;
        color: #212529;
      }
      .chat-input {
        display: flex;
        gap: 0.5rem;
      }
      .chat-input textarea {
        resize: none;
      }
      .session-item {
        cursor: pointer;
        transition: background-color 0.2s;
      }
      .session-item:hover {
        background-color: #f8f9fa;
      }
      .stats-card {
        background: linear-gradient(45deg, #007bff, #0056b3);
        color: white;
      }
      .stats-value {
        font-size: 1.5rem;
        font-weight: bold;
      }
    </style>
  </head>
  <body class="bg-light">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
      <div class="container">
        <a class="navbar-brand" href="#">
          <i class="fas fa-brain me-2"></i>FocusMind StudyFlow
        </a>
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav ms-auto">
            <li class="nav-item">
              <a class="nav-link active" href="#"
                ><i class="fas fa-home me-1"></i>Home</a
              >
            </li>
            <li class="nav-item">
              <a class="nav-link" href="dashboard.jsp"
                ><i class="fas fa-chart-line me-1"></i>Dashboard</a
              >
            </li>
            <li class="nav-item">
              <a class="nav-link" href="rules.jsp"
                ><i class="fas fa-cogs me-1"></i>Rules</a
              >
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <div class="container mt-5">
      <div class="row">
        <div class="col-md-8">
          <div class="card shadow-sm mb-4">
            <div class="card-body">
              <h2 class="card-title mb-4">Start New Study Session</h2>
              <form action="LoggerServlet" method="post">
                <div class="mb-3">
                  <label for="taskName" class="form-label">Task Name</label>
                  <input
                    type="text"
                    class="form-control"
                    id="taskName"
                    name="taskName"
                    required
                  />
                </div>
                <div class="mb-3">
                  <label for="taskType" class="form-label">Task Type</label>
                  <select
                    class="form-select"
                    id="taskType"
                    name="taskType"
                    required
                  >
                    <option value="reading">Reading</option>
                    <option value="writing">Writing</option>
                    <option value="coding">Coding</option>
                    <option value="problem_solving">Problem Solving</option>
                  </select>
                </div>
                <div class="mb-3">
                  <label class="form-label">Difficulty Level</label>
                  <div class="d-flex align-items-center">
                    <input
                      type="range"
                      class="form-range"
                      id="difficultyLevel"
                      name="difficultyLevel"
                      min="1"
                      max="5"
                      value="3"
                    />
                    <span class="ms-3" id="difficultyValue">3</span>
                  </div>
                </div>
                <div class="mb-3">
                  <label class="form-label">Fatigue Level</label>
                  <div class="fatigue-indicator text-center" id="fatigueEmoji">
                    😊
                  </div>
                  <input
                    type="range"
                    class="form-range emoji-slider"
                    id="fatigueLevel"
                    name="fatigueLevel"
                    min="1"
                    max="5"
                    value="1"
                  />
                </div>
                <div class="mb-3">
                  <label for="duration" class="form-label"
                    >Duration (minutes)</label
                  >
                  <input
                    type="number"
                    class="form-control"
                    id="duration"
                    name="duration"
                    min="15"
                    max="180"
                    value="60"
                    required
                  />
                </div>
                <div class="mb-3">
                  <label for="notes" class="form-label">Notes</label>
                  <textarea
                    class="form-control"
                    id="notes"
                    name="notes"
                    rows="3"
                  ></textarea>
                </div>
                <button type="submit" class="btn btn-primary">
                  <i class="fas fa-play me-1"></i>Start Session
                </button>
              </form>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card shadow-sm mb-4 stats-card">
            <div class="card-body">
              <h3 class="card-title text-white">Quick Stats</h3>
              <div class="row text-center">
                <div class="col-6 mb-3">
                  <div class="stats-value">${todaySessions}</div>
                  <small>Today's Sessions</small>
                </div>
                <div class="col-6 mb-3">
                  <div class="stats-value">${averageFatigue}</div>
                  <small>Avg. Fatigue</small>
                </div>
                <div class="col-6">
                  <div class="stats-value">${activeRules}</div>
                  <small>Active Rules</small>
                </div>
                <div class="col-6">
                  <div class="stats-value" id="currentTime">00:00</div>
                  <small>Current Time</small>
                </div>
              </div>
            </div>
          </div>
          <div class="card shadow-sm mb-4">
            <div class="card-body">
              <h3 class="card-title">Recent Sessions</h3>
              <div class="list-group">
                <c:forEach items="${recentSessions}" var="session">
                  <div
                    class="list-group-item session-item"
                    onclick="startSessionChat(${session.sessionId}, '${session.taskName}')"
                  >
                    <div class="d-flex w-100 justify-content-between">
                      <h6 class="mb-1">${session.taskName}</h6>
                      <small>${session.durationMinutes} min</small>
                    </div>
                    <p class="mb-1">${session.taskType}</p>
                    <small>Fatigue: ${session.fatigueLevel}/5</small>
                  </div>
                </c:forEach>
              </div>
            </div>
          </div>
          <div class="card shadow-sm">
            <div class="card-body">
              <h3 class="card-title">Study Assistant</h3>
              <div class="chat-container" id="chatContainer">
                <div class="chat-message ai-message">
                  Hello! I'm your study assistant. How can I help you today?
                </div>
              </div>
              <div class="chat-input">
                <textarea
                  class="form-control"
                  id="messageInput"
                  rows="2"
                  placeholder="Type your message here..."
                ></textarea>
                <button class="btn btn-primary" onclick="sendMessage()">
                  <i class="fas fa-paper-plane"></i>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
      // Update difficulty value display
      document
        .getElementById("difficultyLevel")
        .addEventListener("input", function () {
          document.getElementById("difficultyValue").textContent = this.value;
        });

      // Update fatigue emoji
      const fatigueEmojis = ["😊", "🙂", "😐", "😕", "😩"];
      document
        .getElementById("fatigueLevel")
        .addEventListener("input", function () {
          const index = parseInt(this.value) - 1;
          document.getElementById("fatigueEmoji").textContent =
            fatigueEmojis[index];
        });

      // Update current time
      function updateTime() {
        const now = new Date();
        const timeString = now.toLocaleTimeString("en-US", {
          hour12: false,
          hour: "2-digit",
          minute: "2-digit",
        });
        document.getElementById("currentTime").textContent = timeString;
      }
      setInterval(updateTime, 1000);
      updateTime();

      // Chat functionality
      const GEMINI_API_KEY = "AIzaSyBP0BTKp97HpdOZE-XCVEQP5XhaCTGtSxc";
      const chatContainer = document.getElementById("chatContainer");
      const messageInput = document.getElementById("messageInput");
      let currentSessionId = null;

      function addMessage(message, isUser = false) {
        const messageDiv = document.createElement("div");
        messageDiv.className = `chat-message ${
          isUser ? "user-message" : "ai-message"
        }`;
        messageDiv.textContent = message;
        chatContainer.appendChild(messageDiv);
        chatContainer.scrollTop = chatContainer.scrollHeight;
      }

      function startSessionChat(sessionId, taskName) {
        currentSessionId = sessionId;
        addMessage(`Starting chat about session: ${taskName}`, false);
        addMessage("What would you like to discuss about this session?", false);
      }

      async function sendMessage() {
        const message = messageInput.value.trim();
        if (!message) return;

        addMessage(message, true);
        messageInput.value = "";

        try {
          const response = await fetch("ChatServlet", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify({
              message: message,
              apiKey: GEMINI_API_KEY,
              sessionId: currentSessionId,
            }),
          });

          const data = await response.json();
          addMessage(data.response);
        } catch (error) {
          addMessage("Sorry, I encountered an error. Please try again.");
          console.error("Error:", error);
        }
      }

      messageInput.addEventListener("keypress", function (e) {
        if (e.key === "Enter" && !e.shiftKey) {
          e.preventDefault();
          sendMessage();
        }
      });
    </script>
  </body>
</html>
