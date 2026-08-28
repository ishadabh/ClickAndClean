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

import java.util.Collection;
import java.io.IOException;

@WebServlet("/report")

// annotation to deals with file upload in servlet
@MultipartConfig( location = "/Users/shadabhussain/Desktop/ClickAndClean_report_img",
                    fileSizeThreshold = 1024 * 1024 * 2,       // 2MB 
					    maxFileSize = 1024 * 1024 * 10,        	 // 10MB per file
						    maxRequestSize = 1024 * 1024 * 50    // 50MB total request size limit
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
        Collection<Part> parts = request.getParts();
        StringBuilder imagePaths = new StringBuilder();

        for(Part part : parts) {
            if("imageFile".equals(part.getName()) && part.getSize() > 0) {
                String originalName = extractFileName(part);

                if(originalName != null && !(originalName.isEmpty())) {

                    String fileName = System.currentTimeMillis() + "_" + originalName;

                    // Save file using location from @MultipartConfig
                    part.write(fileName);

                    if(fileName.length() > 0) {
                        imagePaths.append(",");
                    }
                    imagePaths.append("uploads/").append(fileName);
                }
            }
        }

        String imagePath = imagePaths.toString();
        
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
            	int startIdx = s.indexOf("filename=") + 10;
            	int endIdx = s.length() -1;
                return s.substring(startIdx, endIdx);
            }
        }
        return null;
    }
}