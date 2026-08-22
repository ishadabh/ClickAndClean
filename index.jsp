<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ClickAndClean</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<div class="auth-container">
    <div style="text-align:center; margin-bottom:25px;">
        <div class="logo"
             style="justify-content:center; font-size:1.8rem;">
            <i class="fa-solid fa-recycle"></i>
            ClickAndClean
        </div>

        <p class="auth-subtitle">
            Select portal role to access system
        </p>
    </div>

    <div class="role-selector">
        <button type="button" class="role-btn active" onclick="selectRole('user', this)">
            <i class="fa-solid fa-user"></i>
            Resident
        </button>

        <button type="button" class="role-btn" onclick="selectRole('admin', this)">
            <i class="fa-solid fa-user-shield"></i>
            Admin
        </button>
        
        <button type="button" class="role-btn" onclick="selectRole('driver', this)">
            <i class="fa-solid fa-truck"></i>
            Collector
        </button>
    </div>

    <form action="login" method="post">
        <input type="hidden" id="role" name="role" value="user">

        <div class="form-group">
            <label id="login-label"> Email or Phone Number </label>
            
            <input type="text" id="login-id" name="loginId" placeholder="user@cleancity.com" required>
        </div>

        <div class="form-group">
            <label for="password">
                Password
            </label>
            <input type="password" id="password" name="password" placeholder="Enter Password" required>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <p class="error-message">
                <%= request.getAttribute("error") %>
            </p> 
            <% } %>
        <button type="submit" class="btn" style="width:100%;">
            <i class="fa-solid fa-right-to-bracket"></i>
            Login to Workspace
        </button>
    </form>
</div>
<script src="${pageContext.request.contextPath}/script.js"></script>

</body>

</html>