<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, br.dev.garage474.legacy.models.User" %>
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
        <h1 class="h3 mb-0">Lista de Usuários</h1>
        <a href="${pageContext.request.contextPath}/users?action=form" class="btn btn-primary btn-sm">Novo Usuário</a>
    </div>

    <%
        // ----- Parâmetros de paginação -----
        int currentPage = 1;
        int perPage = 10; // valor padrão
        int totalUsers = request.getAttribute("totalUsers") != null
                ? (Integer) request.getAttribute("totalUsers")
                : 0;

        try {
            if (request.getParameter("currentPage") != null) {
                currentPage = Integer.parseInt(request.getParameter("currentPage"));
            }
            if (request.getParameter("perPage") != null) {
                perPage = Integer.parseInt(request.getParameter("perPage"));
            }
        } catch (NumberFormatException e) {
            // mantém valores padrão
        }

        int totalPages = (int) Math.ceil((double) totalUsers / perPage);
        if (totalPages == 0) totalPages = 1;
    %>

    <div class="card shadow-sm">
        <div class="card-body p-0">
            <table class="table table-striped table-hover mb-0 align-middle">
                <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>E-mail</th>
                    <th>Ativo</th>
                </tr>
                </thead>
                <tbody>
                <%
                    List<User> users = (List<User>) request.getAttribute("users");
                    if (users != null && !users.isEmpty()) {
                        for (User u : users) {
                %>
                <tr>
                    <td><%= u.getId() %></td>
                    <td><%= u.getName() %></td>
                    <td><%= u.getEmail() %></td>
                    <td>
                        <% if (u.isActive()) { %>
                        <span class="badge bg-success">Sim</span>
                        <% } else { %>
                        <span class="badge bg-secondary">Não</span>
                        <% } %>
                    </td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr>
                    <td colspan="4" class="text-center text-muted py-3">
                        Nenhum usuário encontrado.
                    </td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Paginação -->
    <div class="d-flex justify-content-between align-items-center mt-3">
        <div>
            <span class="text-muted">
                Página <%= currentPage %> de <%= totalPages %>
            </span>
        </div>
        <nav>
            <ul class="pagination mb-0">
                <li class="page-item <%= (currentPage <= 1) ? "disabled" : "" %>">
                    <a class="page-link" href="?action=list&currentPage=<%= currentPage - 1 %>&perPage=<%= perPage %>">Anterior</a>
                </li>
                <li class="page-item <%= (currentPage >= totalPages) ? "disabled" : "" %>">
                    <a class="page-link" href="?action=list&currentPage=<%= currentPage + 1 %>&perPage=<%= perPage %>">Próximo</a>
                </li>
            </ul>
        </nav>
    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
