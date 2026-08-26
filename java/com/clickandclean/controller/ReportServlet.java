package com.clickandclean.controller;

import com.clickandclean.dao.ReportDAO;
import com.clickandclean.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;

@WebServlet("/report")

// annotation to deals with file upload in servlet
@MultipartConfig( fileSizeThreshold = 1024 * 1024 * 2,       // 2MB
					maxFileSize = 1024 * 1024 * 10,        	 // 10MB
						maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class ReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReportDAO reportDAO;

    public void init() {
        reportDAO = new ReportDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        
        int userId = user.getUserId(); // from User Class
        String description = request.getParameter("issueDescription");
        String location = request.getParameter("location"); // Received as "28.613900, 77.209000"

        // 2. Handle File Upload
        Part filePart = request.getPart("imageFile");
        String imagePath = null;

        if (filePart != null && filePart.getSize() > 0) {
            String fileName = System.currentTimeMillis() + "_" + extractFileName(filePart);
            
            // Set storage directory path inside webapp/uploads
            String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            // Save file to server disk
            filePart.write(uploadPath + File.separator + fileName);
            imagePath = "uploads/" + fileName;
        }

        // 3. Save to Database via DAO
        boolean success = reportDAO.createReport(userId, description, location, imagePath);

        if (success) {
            response.sendRedirect("dashboard");
        } else {
            request.setAttribute("error", "Unable to submit report.");
            response.sendRedirect("dashboard");
        }
    }

    // Helper method to parse uploaded file name
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String s : contentDisp.split(";")) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "incident.jpg";
    }
}