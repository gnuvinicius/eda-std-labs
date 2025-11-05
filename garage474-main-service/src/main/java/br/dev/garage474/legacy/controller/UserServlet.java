package br.dev.garage474.legacy.controller;

import java.io.IOException;
import java.util.List;

import br.dev.garage474.legacy.models.User;
import br.dev.garage474.legacy.repositories.UserRepository;
import jakarta.inject.Inject;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    @Inject
    private UserRepository repository;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null) {
            req.getRequestDispatcher("/WEB-INF/views/not-found.jsp").forward(req, resp);
        } else {
            switch (action) {
                case "list" -> listUsers(req, resp);
                case "form" -> req.getRequestDispatcher("/WEB-INF/views/user-form.jsp").forward(req, resp);
                default -> req.getRequestDispatcher("/WEB-INF/views/not-found.jsp").forward(req, resp);
            }
        }
    }

    private void listUsers(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<User> users = repository.findAllUsers();

        req.setAttribute("users", users);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/users.jsp");
        dispatcher.forward(req, resp);
    }

}
