package br.dev.garage474.legacy.controller;

import java.io.IOException;
import java.util.List;

import br.dev.garage474.legacy.models.User;
import br.dev.garage474.legacy.repositories.UserRepository;
import jakarta.ejb.EJB;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/users")
public class UserController extends HttpServlet {

    @EJB
    private UserRepository repository;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");


        List<User> users = repository.findAllUsers();

        req.setAttribute("users", users);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/users.jsp");
        dispatcher.forward(req, resp);
    }
}
