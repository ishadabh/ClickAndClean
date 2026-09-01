<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>ClickAndClean - Register</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>

<body>

<div class="auth-container">

    <!-- HEADER -->
    <div style="text-align:center; margin-bottom:25px;">
        <div class="logo"
             style="justify-content:center; font-size:1.8rem;">
            <i class="fa-solid fa-recycle"></i>
            ClickAndClean
        </div>
        <p class="auth-subtitle">
            Create your account
        </p>
    </div>

    <form action="register" method="post">

        <div class="form-group">

            <label for="name"> Full Name </label>
            <input type="text" id="name" name="name" placeholder="Enter your full name" required>
        </div>

        <div class="form-group">

            <label for="loginId"> Email or Phone Number </label>
            <input type="text" id="loginId" name="loginId" placeholder="user@cleancity.com" required>
        </div>
        <div class="form-group">

            <label for="password"> Password </label>

            <input type="password" id="password" name="password" placeholder="Enter password" required>
        </div>
        
        <div class="form-group">
            <label for="role">
                Role
            </label>
            <select id="role" name="role" required>
                <option value="" disabled selected>
                    Select your role
                </option>
                <option value="user">
                    Resident
                </option>
                <option value="driver">
                    Collector
                </option>
                <option value="admin">
                    Admin
                </option>
            </select>
        </div>

        <% if (request.getAttribute("error") != null) { %>

            <p class="error-message">
                <%= request.getAttribute("error") %>
            </p>

        <% } %>

        <button type="submit" class="btn" style="width:100%;">
            <i class="fa-solid fa-user-plus"></i>
            Create Account
        </button>
        
        <div class="register-link">
            Already have an account?
            <a href="${pageContext.request.contextPath}/index.jsp">
                Login
            </a>
        </div>
    </form>
</div>

</body>
</html>
