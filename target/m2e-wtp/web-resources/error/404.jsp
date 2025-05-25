<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>404 - Page Not Found</title>
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
    </style>
  </head>
  <body class="bg-light">
    <div class="error-container">
      <div class="container">
        <i class="fas fa-exclamation-circle error-icon"></i>
        <h1 class="display-4 mb-4">404 - Page Not Found</h1>
        <p class="lead mb-4">
          Oops! The page you're looking for doesn't exist.
        </p>
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
