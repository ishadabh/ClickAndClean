let currentRole = 'user';

function selectRole(role, btn) {
    currentRole = role;
    document.querySelectorAll('.role-btn').forEach(button => {
        button.classList.remove('active');
    });
    btn.classList.add('active');

    // Target the hidden input id="role"
    // here i get wrong ans of user for admin and driver  
    const hiddenRoleInput = document.getElementById('role');
    if (hiddenRoleInput) {
        hiddenRoleInput.value = role;
    }

    const label = document.getElementById('login-label');
    const input = document.getElementById('login-id');

    if (!label || !input) return; 

    if (role === 'user') {
        label.innerText = 'Email or Phone Number';
        input.value = 'resident@cleancity.com';
    } else if (role === 'admin') {
        label.innerText = 'Admin Username';
        input.value = 'admin.supervisor';
    } else {
        label.innerText = 'Driver/Fleet Badge ID';
        input.value = 'DRIVER-UNIT-01';
    }
}

/* LOGIN */
function handleLogin(event) {
    event.preventDefault();

    const form = event.target;
    const formData = new FormData(form);

    formData.append('role', currentRole);

    const urlEncodedData = new URLSearchParams();
    formData.forEach((value, key) => {
        urlEncodedData.append(key, value);
    });

    fetch('login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: urlEncodedData
    })
    .then(response => {
        if (response.redirected) {
            window.location.href = response.url;
        }
    })
    .catch(error => {
        console.error('Login error:', error);
        alert('Unable to connect to server.');
    });
}