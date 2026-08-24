package com.clickandclean.controller;

import com.clickandclean.dao.ReportDAO;
import com.clickandclean.model.Report;
import com.clickandclean.model.User;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/dashboard")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
	private ReportDAO reportDAO;

    public void init() {

        reportDAO = new ReportDAO();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");

        List<Report> reports;

        if ("user".equalsIgnoreCase(user.getRole())) {

            reports = reportDAO.getReportsByUser(user.getUserId());
        } else {

            reports = reportDAO.getAllReports();
        }
        request.setAttribute("reports", reports);

        RequestDispatcher reqDis=request.getRequestDispatcher("dashboard.jsp");
        reqDis.forward(request, response);
    }
}