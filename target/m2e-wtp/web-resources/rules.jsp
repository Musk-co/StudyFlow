<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Rules - FocusMind StudyFlow</title>
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
      rel="stylesheet"
    />
    <link
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
      rel="stylesheet"
    />
    <style>
      .rule-card {
        transition: transform 0.2s;
      }
      .rule-card:hover {
        transform: translateY(-5px);
      }
      .rule-condition {
        font-family: monospace;
        background-color: #f8f9fa;
        padding: 0.5rem;
        border-radius: 0.25rem;
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
              <a class="nav-link" href="dashboard.jsp"
                ><i class="fas fa-chart-line me-1"></i>Dashboard</a
              >
            </li>
            <li class="nav-item">
              <a class="nav-link active" href="#"
                ><i class="fas fa-cogs me-1"></i>Rules</a
              >
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <div class="container mt-5">
      <div class="row mb-4">
        <div class="col-md-8">
          <h2>Adaptation Rules</h2>
          <p class="text-muted">
            Manage rules that automatically adjust study session durations based
            on fatigue and difficulty levels.
          </p>
        </div>
        <div class="col-md-4 text-end">
          <button
            type="button"
            class="btn btn-primary"
            data-bs-toggle="modal"
            data-bs-target="#newRuleModal"
          >
            <i class="fas fa-plus me-1"></i>New Rule
          </button>
        </div>
      </div>

      <div class="row">
        <c:forEach items="${rules}" var="rule">
          <div class="col-md-6 mb-4">
            <div class="card rule-card shadow-sm">
              <div class="card-body">
                <div
                  class="d-flex justify-content-between align-items-start mb-3"
                >
                  <h5 class="card-title mb-0">${rule.ruleName}</h5>
                  <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox"
                    ${rule.active ? 'checked' : ''}
                    onchange="toggleRule(${rule.ruleId}, this)">
                  </div>
                </div>
                <div class="mb-3">
                  <label class="form-label text-muted">Condition:</label>
                  <div class="rule-condition">${rule.conditionText}</div>
                </div>
                <div class="mb-3">
                  <label class="form-label text-muted">Action:</label>
                  <div class="rule-condition">${rule.actionText}</div>
                </div>
                <div class="d-flex justify-content-end">
                  <button
                    class="btn btn-sm btn-outline-primary me-2"
                    onclick="editRule(${rule.ruleId})"
                  >
                    <i class="fas fa-edit me-1"></i>Edit
                  </button>
                  <button
                    class="btn btn-sm btn-outline-danger"
                    onclick="deleteRule(${rule.ruleId})"
                  >
                    <i class="fas fa-trash me-1"></i>Delete
                  </button>
                </div>
              </div>
            </div>
          </div>
        </c:forEach>
      </div>
    </div>

    <!-- New Rule Modal -->
    <div class="modal fade" id="newRuleModal" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Create New Rule</h5>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
            ></button>
          </div>
          <div class="modal-body">
            <form id="newRuleForm" action="RuleServlet" method="post">
              <div class="mb-3">
                <label for="ruleName" class="form-label">Rule Name</label>
                <input
                  type="text"
                  class="form-control"
                  id="ruleName"
                  name="ruleName"
                  required
                />
              </div>
              <div class="mb-3">
                <label for="conditionText" class="form-label">Condition</label>
                <input
                  type="text"
                  class="form-control"
                  id="conditionText"
                  name="conditionText"
                  placeholder="e.g., fatigue_level > 3 AND difficulty_level > 2"
                  required
                />
                <div class="form-text">
                  Use fatigue_level and difficulty_level with comparison
                  operators.
                </div>
              </div>
              <div class="mb-3">
                <label for="actionText" class="form-label">Action</label>
                <input
                  type="text"
                  class="form-control"
                  id="actionText"
                  name="actionText"
                  placeholder="e.g., reduce_duration_by 20%"
                  required
                />
                <div class="form-text">
                  Use reduce_duration_by or increase_duration_by with
                  percentage.
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button
              type="button"
              class="btn btn-secondary"
              data-bs-dismiss="modal"
            >
              Cancel
            </button>
            <button type="submit" form="newRuleForm" class="btn btn-primary">
              Create Rule
            </button>
          </div>
        </div>
      </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
      function toggleRule(ruleId, checkbox) {
        fetch("RuleServlet", {
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded",
          },
          body: `action=toggle&ruleId=${ruleId}`,
        })
          .then((response) => response.json())
          .then((data) => {
            if (!data.success) {
              checkbox.checked = !checkbox.checked;
              alert("Failed to toggle rule status");
            }
          })
          .catch((error) => {
            checkbox.checked = !checkbox.checked;
            alert("Error: " + error.message);
          });
      }

      function editRule(ruleId) {
        // Implement edit functionality
        alert("Edit functionality to be implemented");
      }

      function deleteRule(ruleId) {
        if (confirm("Are you sure you want to delete this rule?")) {
          fetch("RuleServlet", {
            method: "POST",
            headers: {
              "Content-Type": "application/x-www-form-urlencoded",
            },
            body: `action=delete&ruleId=${ruleId}`,
          })
            .then((response) => response.json())
            .then((data) => {
              if (data.success) {
                location.reload();
              } else {
                alert("Failed to delete rule");
              }
            })
            .catch((error) => {
              alert("Error: " + error.message);
            });
        }
      }
    </script>
  </body>
</html>
