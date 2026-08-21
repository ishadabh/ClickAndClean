package com.clickandclean.controller;

import com.clickandclean.dao.UserDAO;
import com.clickandclean.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {

        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

    	String loginId = request.getParameter("loginId") != null ? request.getParameter("loginId").trim() : "";
    	String password = request.getParameter("password") != null ? request.getParameter("password").trim() : "";
    	String role = request.getParameter("role") != null ? request.getParameter("role").trim() : "";

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
      
        User user = userDAO.validateUser(loginId,password,role);

        if (user != null) {

            HttpSession session = request.getSession();

            session.setAttribute("user", user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("role", user.getRole());

            out.print("""
                    {
                        "success": true,
                        "message": "Login successful",
                        "userId": %d,
                        "name": "%s",
                        "role": "%s",
                        "points": %d
                    }
                    """.formatted(
                    user.getUserId(),
                    user.getName(),
                    user.getRole(),
                    user.getPoints()
            ));

        } else {

            out.print("""
                    {
                        "success": false,
                        "message": "Invalid login ID, password or role"
                    }
                    """);
        }
    }
}