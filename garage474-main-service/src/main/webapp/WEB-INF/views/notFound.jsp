<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="includes/navbar.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
        <title>Garage474 Labs</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f8f9fa;
                display: flex;
                flex-direction: column;
                height: 100vh;
                margin: 0;
            }
            .error-container {
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: center;
                width: 100%;
            }
            .error-code {
                font-size: 8rem;
                font-weight: 700;
                color: #dc3545;
            }
            .error-message {
                font-size: 1.5rem;
                margin-bottom: 1rem;
            }
        </style>
</head>
<body>
<div class="error-container">
    <div class="error-code">404</div>
    <div class="error-message">Ops! Página não encontrada.</div>
    <p class="text-muted mb-4">
        O recurso que você tentou acessar não existe ou foi movido.
    </p>
    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">
        <i class="bi bi-house"></i> Voltar ao Dashboard
    </a>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- Ícones do Bootstrap -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

</body>
</html>
