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

    public void init() {

        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

    	String loginId = request.getParameter("loginId");
    	String password = request.getParameter("password");
    	String role = request.getParameter("role");

        User user = userDAO.validateUser(loginId,password,role);

        if (user != null) {

            HttpSession session = request.getSession();

            session.setAttribute("user", user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("role", user.getRole());
            response.sendRedirect("dashboard");
            
        } else {
            request.setAttribute("error","Invalid login ID, password or role.");

            RequestDispatcher reqDis =request.getRequestDispatcher("index.jsp");
            reqDis.forward(request, response);
        }
    }
}