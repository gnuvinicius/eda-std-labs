<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="includes/navbar.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>Garage474 Labs</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container">
    <h1 class="mb-4">Dashboard</h1>
    <div class="card mb-3">
        <div class="card-body">
            <h5>Total de Usuários: ${totalUsers}</h5>
            <h5>Sessões Ativas: ${activeSessions}</h5>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
