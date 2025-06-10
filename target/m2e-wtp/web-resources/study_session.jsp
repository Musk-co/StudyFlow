<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>Study Session</title>
    <style>
      body {
        font-family: Arial, sans-serif;
        text-align: center;
      }
      #countdown {
        font-size: 48px;
        margin: 20px;
      }
      #controls {
        margin: 20px;
      }
      button {
        padding: 10px 20px;
        font-size: 16px;
      }
    </style>
  </head>
  <body>
    <h1>Study Session</h1>
    <div id="countdown">00:00:00</div>
    <div id="controls">
      <button onclick="startTimer()">Start</button>
      <button onclick="pauseTimer()">Pause</button>
      <button onclick="resetTimer()">Reset</button>
    </div>
    <script>
      let timeLeft = <%= request.getAttribute("durationMinutes") %> * 60;
      let timerId;

      function updateCountdown() {
          const hours = Math.floor(timeLeft / 3600);
          const minutes = Math.floor((timeLeft % 3600) / 60);
          const seconds = timeLeft % 60;
          document.getElementById('countdown').innerText =
              `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
          if (timeLeft > 0) {
              timeLeft--;
          } else {
              clearInterval(timerId);
              alert('Study session completed!');
          }
      }

      function startTimer() {
          if (!timerId) {
              timerId = setInterval(updateCountdown, 1000);
          }
      }

      function pauseTimer() {
          clearInterval(timerId);
          timerId = null;
      }

      function resetTimer() {
          clearInterval(timerId);
          timerId = null;
          timeLeft = <%= request.getAttribute("durationMinutes") %> * 60;
          updateCountdown();
      }

      updateCountdown();
    </script>
  </body>
</html>
