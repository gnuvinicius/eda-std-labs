<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="br.dev.garage474.legacy.models.User, java.util.List" %>

<!DOCTYPE html>
<html>
<head>
    <title>Lista de Usuários (Server-Side)</title>
    <style>
        body { font-family: Arial; margin: 40px; }
        table { border-collapse: collapse; width: 60%; margin: auto; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background-color: #f0f0f0; }
    </style>
</head>
<body>
<h2 style="text-align:center;">Usuários renderizados no servidor</h2>

<table>
    <thead>
        <tr><th>ID</th><th>Nome</th><th>Email</th></tr>
    </thead>
    <tbody>
    <%
        List<User> users = (List<User>) request.getAttribute("users");
        if (users != null) {
            for (User u : users) {
    %>
        <tr>
            <td><%= u.getId() %></td>
            <td><%= u.getName() %></td>
            <td><%= u.getEmail() %></td>
        </tr>
    <%
            }
        }
    %>
    </tbody>
</table>

</body>
</html>