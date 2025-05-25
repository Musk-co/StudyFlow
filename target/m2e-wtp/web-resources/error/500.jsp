<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>500 - Server Error</title>
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
      rel="stylesheet"
    />
    <link
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
      rel="stylesheet"
    />
    <style>
      .error-container {
        min-height: 100vh;
        display: flex;
        align-items: center;
        justify-content: center;
        text-align: center;
      }
      .error-icon {
        font-size: 6rem;
        color: #dc3545;
        margin-bottom: 1rem;
      }
      .error-details {
        max-width: 600px;
        margin: 2rem auto;
        padding: 1rem;
        background-color: #f8f9fa;
        border-radius: 0.5rem;
        text-align: left;
        font-family: monospace;
        font-size: 0.9rem;
        overflow-x: auto;
      }
    </style>
  </head>
  <body class="bg-light">
    <div class="error-container">
      <div class="container">
        <i class="fas fa-exclamation-triangle error-icon"></i>
        <h1 class="display-4 mb-4">500 - Server Error</h1>
        <p class="lead mb-4">Oops! Something went wrong on our end.</p>

        <% if (exception != null) { %>
        <div class="error-details">
          <strong>Error Details:</strong><br />
          <%= exception.getMessage() %>
        </div>
        <% } %>

        <a
          href="${pageContext.request.contextPath}/index.jsp"
          class="btn btn-primary"
        >
          <i class="fas fa-home me-2"></i>Return to Home
        </a>
      </div>
    </div>
  </body>
</html>
