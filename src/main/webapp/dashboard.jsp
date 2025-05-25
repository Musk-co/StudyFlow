<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Dashboard - FocusMind StudyFlow</title>
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
      rel="stylesheet"
    />
    <link
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
      rel="stylesheet"
    />
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
      .chart-container {
        position: relative;
        height: 300px;
        margin-bottom: 20px;
      }
      .stat-card {
        transition: transform 0.2s;
      }
      .stat-card:hover {
        transform: translateY(-5px);
      }
    </style>
  </head>
  <body class="bg-light">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
      <div class="container">
        <a class="navbar-brand" href="index.jsp">
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
              <a class="nav-link" href="index.jsp"
                ><i class="fas fa-home me-1"></i>Home</a
              >
            </li>
            <li class="nav-item">
              <a class="nav-link active" href="#"
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
      <div class="row mb-4">
        <div class="col-md-3">
          <div class="card stat-card bg-primary text-white">
            <div class="card-body">
              <h5 class="card-title">Total Study Time</h5>
              <h2 class="card-text">${totalStudyTime} hrs</h2>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card stat-card bg-success text-white">
            <div class="card-body">
              <h5 class="card-title">Sessions Today</h5>
              <h2 class="card-text">${sessionsToday}</h2>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card stat-card bg-warning text-dark">
            <div class="card-body">
              <h5 class="card-title">Avg. Fatigue</h5>
              <h2 class="card-text">${averageFatigue}/5</h2>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card stat-card bg-info text-white">
            <div class="card-body">
              <h5 class="card-title">Active Rules</h5>
              <h2 class="card-text">${activeRules}</h2>
            </div>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="col-md-8">
          <div class="card shadow-sm mb-4">
            <div class="card-body">
              <h3 class="card-title">Fatigue Trend</h3>
              <div class="chart-container">
                <canvas id="fatigueChart"></canvas>
              </div>
            </div>
          </div>
          <div class="card shadow-sm">
            <div class="card-body">
              <h3 class="card-title">Study Distribution</h3>
              <div class="chart-container">
                <canvas id="studyDistributionChart"></canvas>
              </div>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card shadow-sm mb-4">
            <div class="card-body">
              <h3 class="card-title">Recent Sessions</h3>
              <div class="list-group">
                <c:forEach items="${recentSessions}" var="session">
                  <div class="list-group-item">
                    <div class="d-flex w-100 justify-content-between">
                      <h6 class="mb-1">${session.taskName}</h6>
                      <small>${session.durationMinutes} min</small>
                    </div>
                    <p class="mb-1">${session.taskType}</p>
                    <div
                      class="d-flex justify-content-between align-items-center"
                    >
                      <small>Fatigue: ${session.fatigueLevel}/5</small>
                      <small>Difficulty: ${session.difficultyLevel}/5</small>
                    </div>
                  </div>
                </c:forEach>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
      // Fatigue Trend Chart
      const fatigueCtx = document.getElementById('fatigueChart').getContext('2d');
      new Chart(fatigueCtx, {
          type: 'line',
          data: {
              labels: ${fatigueLabels},
              datasets: [{
                  label: 'Fatigue Level',
                  data: ${fatigueData},
                  borderColor: 'rgb(255, 99, 132)',
                  tension: 0.1
              }]
          },
          options: {
              responsive: true,
              maintainAspectRatio: false,
              scales: {
                  y: {
                      beginAtZero: true,
                      max: 5
                  }
              }
          }
      });

      // Study Distribution Chart
      const distributionCtx = document.getElementById('studyDistributionChart').getContext('2d');
      new Chart(distributionCtx, {
          type: 'doughnut',
          data: {
              labels: ${taskTypes},
              datasets: [{
                  data: ${taskDistribution},
                  backgroundColor: [
                      'rgb(255, 99, 132)',
                      'rgb(54, 162, 235)',
                      'rgb(255, 205, 86)',
                      'rgb(75, 192, 192)'
                  ]
              }]
          },
          options: {
              responsive: true,
              maintainAspectRatio: false
          }
      });
    </script>
  </body>
</html>
