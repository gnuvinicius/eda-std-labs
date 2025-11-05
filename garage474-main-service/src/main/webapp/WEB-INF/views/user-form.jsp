<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="includes/navbar.jsp" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Garage474 Labs</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="h3 mb-0">Cadastrar Novo Usuário</h1>
        <a href="${pageContext.request.contextPath}/users?action=list" class="btn btn-secondary btn-sm">
            <i class="bi bi-arrow-left"></i> Voltar
        </a>
    </div>

    <div class="card shadow-sm">
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/users?action=create" method="post">
                <div class="mb-3">
                    <label for="name" class="form-label">Nome</label>
                    <input type="text" id="name" name="name" class="form-control" required placeholder="Digite o nome completo">
                </div>

                <div class="mb-3">
                    <label for="email" class="form-label">E-mail</label>
                    <input type="email" id="email" name="email" class="form-control" required placeholder="exemplo@dominio.com">
                </div>

                <div class="form-check form-switch mb-4">
                    <input class="form-check-input" type="checkbox" id="active" name="active" checked>
                    <label class="form-check-label" for="active">Usuário ativo</label>
                </div>

                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-save"></i> Salvar Usuário
                </button>
            </form>
        </div>
    </div>
</div>

<!-- Bootstrap e ícones -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
</body>
</html>
