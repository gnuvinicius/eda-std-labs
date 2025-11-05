<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Object user = session.getAttribute("user");

    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/auth");
    } else {
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
%>
