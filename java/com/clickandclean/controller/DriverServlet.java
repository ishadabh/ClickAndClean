package com.clickandclean.controller;

import com.clickandclean.dao.ReportDAO;
import com.clickandclean.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/driver")
public class DriverServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
	private ReportDAO reportDAO;

    public void init() {
        reportDAO = new ReportDAO();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {

            response.sendRedirect("index.jsp");
            return;
        }

        User user =(User) session.getAttribute("user");

        if (!"driver".equalsIgnoreCase(user.getRole())) {

            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        String temp = request.getParameter("reportId");
        int reportId = Integer.parseInt(temp);
        reportDAO.completeReport(reportId);
        response.sendRedirect("dashboard");
    }
}