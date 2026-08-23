<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="com.clickandclean.model.User" %>
<%@ page import="com.clickandclean.model.Report" %>

<%@ page import="java.util.List" %>
<%
    User user = (User) session.getAttribute("user");

    if (user == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    List<Report> reports = (List<Report>) request.getAttribute("reports");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>ClickAndClean Dashboard</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
    
</head>

<body>

<div class="app-container" style="display:block;">

    <header class="navbar">
        <div class="logo">
            <i class="fa-solid fa-recycle"></i>
            ClickAndClean
        </div>
        <div class="user-profile">
            <div>
                <span class="active-user-name">
                    <%= user.getName() %>
                </span>
                <span class="active-user-role">
                    <%= user.getRole().toUpperCase() %> ACCESS
                </span>
            </div>
            <a href="logout" class="btn btn-danger">
                <i class="fa-solid fa-power-off"></i>
                Logout
            </a>
        </div>
    </header>
    
    <!-- USER DASHBOARD -->
    <% if ("user".equalsIgnoreCase(user.getRole())) { %>
    <div class="panel-split">
       <div class="card">
            <div class="card-title">
                <span>
                    <i class="fa-solid fa-camera"></i>
                    Report Garbage Issue
                </span>
                <span class="points">
                    <i class="fa-solid fa-coins"></i>
                    <%= user.getPoints() %> PTS
                </span>
            </div>
            <form action="report" method="post" enctype="multipart/form-data">
    
                <div class="upload-dropzone" onclick="document.getElementById('file-input').click()">
                    <i class="fa-solid fa-cloud-arrow-up" style="font-size: 2rem; color: var(--primary); margin-bottom: 8px;"></i>
                    <p id="upload-label">Click to select photo</p>
                    <input type="file" id="file-input" name="imageFile" hidden accept="image/*" onchange="previewImage(this)">
                </div>

                <div class="form-group">
                    <label for="issueDescription">Issue Description</label>
                    <textarea id="issueDescription" name="issueDescription" rows="3" required placeholder="Describe garbage type and situation..."></textarea>
                </div>

                <!-- Location & Map -->
               <div class="form-group">
                   <label for="location">Location</label>
                   <input type="text" name="location" id="location" placeholder="e.g. 28.6139, 77.2090" required>
        
                   <!-- Hidden inputs for raw numeric coordinates -->
                   <input type="hidden" name="latitude" id="latitude">
                   <input type="hidden" name="longitude" id="longitude">

                   <!-- Leaflet Map Container -->
                   <div id="map" class="map-container"></div>

                   <!-- GPS Button -->
                   <button type="button" id="btn-detect-gps" class="btn btn-outline" style="width: 100%; height: 45px; margin-top: 10px;">
                       <i class="fa-solid fa-location-crosshairs"></i> Detect Current GPS
                   </button>
              </div>

              <button type="submit" class="btn" style="width: 100%; height: 50px;">
                  <i class="fa-solid fa-paper-plane"></i>
                  Submit Incident(+50 PTS)
              </button>
            </form>

        </div>

        <div class="card">
            <div class="card-title">
                <span>
                    <i class="fa-solid fa-clock-rotate-left"></i>
                    My Reported Complaints
                </span>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Description</th>
                        <th>Status</th>
                        <th>Reward</th>
                    </tr>
                </thead>
                <tbody>
                <% for (Report report : reports) { %>
                    <tr>
                        <td>
                            #GR-<%= report.getReportId() %>
                        </td>
                        <td>
                            <%= report.getDescription() %>
                        </td>
                        <td>
                            <span class="badge badge-<%= report.getStatus().toLowerCase() %>">
                                <%= report.getStatus() %>
                            </span>
                        </td>
                        <td>
                            +<%= report.getReward() %> PTS
                        </td>
                    </tr>
                <% } %>
                </tbody>
            </table>
          </div>
        </div>
    <% } %>

    <!-- ADMIN DASHBOARD -->
    <% if ("admin".equalsIgnoreCase(user.getRole())) { %>
        <div class="card">
            <div class="card-title">
                <span>
                    <i class="fa-solid fa-chart-pie"></i>
                    Master Complaints Queue
                </span>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Description</th>
                        <th>Status</th>
                        <th>Assign Fleet Unit</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                <% for (Report report : reports) { %>
                    <tr>
                        <td>
                            #GR-<%= report.getReportId() %>
                        </td>
                        <td>
                            <%= report.getDescription() %>
                        </td>

                        <td>
                            <%= report.getStatus() %>
                        </td>
                        <td>
                            <form action="admin" method="post">
                                <input type="hidden" name="action" value="assign">
                                <input type="hidden" name="reportId" value="<%= report.getReportId() %>">
                                <select name="driver">
                                    <option value="">
                                        Select Vehicle
                                    </option>

                                    <option value="DRIVER-UNIT-01">
                                        Truck Alpha
                                    </option>

                                    <option value="DRIVER-UNIT-02">
                                        Truck Beta
                                    </option>

                                </select>

                                <button type="submit" class="btn">
                                    Dispatch
                                </button>
                            </form>
                        </td>
                        <td>
                            <%= report.getAssignedDriver() == null ? "Not Assigned" : report.getAssignedDriver() %>
                        </td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    <% } %>

    <!-- DRIVER DASHBOARD -->
    <% if ("driver".equalsIgnoreCase(user.getRole())) { %>
        <div class="card">
            <div class="card-title">
                <span>
                    <i class="fa-solid fa-truck"></i>
                    Driver Tasks
                </span>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Description</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                <% for (Report report : reports) { %>
                    <% if (user.getLoginId().equals(report.getAssignedDriver())) { %>
                        <tr>
                            <td>
                                #GR-<%= report.getReportId() %>
                            </td>
                            <td>
                                <%= report.getDescription() %>
                            </td>

                            <td>
                                <%= report.getStatus() %>
                            </td>
                            <td>
                                <form action="driver"  method="post">
                                    <input type="hidden" name="reportId" value="<%= report.getReportId() %>">
                                    <button type="submit" class="btn">
                                        Mark Collected
                                    </button>
                                </form>
                            </td>
                        </tr>

                    <% } %>
                <% } %>
                </tbody>
            </table>
        </div> 
        <% } %>
</div>
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
<script src="${pageContext.request.contextPath}/script.js"></script>

</body>
</html>