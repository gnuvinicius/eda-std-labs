<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Login - Legacy App</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

    <style>
        body {
            background: #f8f9fa;
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .login-card {
            width: 100%;
            max-width: 400px;
            border-radius: 12px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }

        .login-header {
            text-align: center;
            margin-bottom: 1.5rem;
        }

        .login-header h1 {
            font-size: 1.8rem;
            font-weight: 600;
        }

        .form-control:focus {
            box-shadow: 0 0 0 0.2rem rgba(13,110,253,.25);
        }
    </style>
</head>
<body>
<div class="card login-card p-4">
    <div class="login-header">
        <i class="bi bi-person-circle fs-1 text-primary mb-2"></i>
        <h1>Bem-vindo</h1>
        <p class="text-muted mb-0">Acesse sua conta</p>
    </div>

    <form action="${pageContext.request.contextPath}/auth" method="post">
        <div class="mb-3">
            <label for="username" class="form-label">Usuário ou E-mail</label>
            <input type="text" id="username" name="username" class="form-control" required placeholder="Digite seu usuário">
        </div>

        <div class="mb-3">
            <label for="password" class="form-label">Senha</label>
            <input type="password" id="password" name="password" class="form-control" required placeholder="Digite sua senha">
        </div>

        <%-- Caso queira exibir erros --%>
        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <div class="alert alert-danger py-2"><%= error %></div>
        <%
            }
        %>

        <div class="d-grid">
            <button type="submit" class="btn btn-primary">
                <i class="bi bi-box-arrow-in-right"></i> Entrar
            </button>
        </div>
    </form>

    <div class="text-center mt-3">
        <small class="text-muted">© 2025 Legacy App</small>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
