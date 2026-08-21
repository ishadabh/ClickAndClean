let currentRole = 'user';
let userPoints = 250;

function selectRole(role, btn) {
    currentRole = role;
    document.querySelectorAll('.role-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');

    const label = document.getElementById('login-label');
    const input = document.getElementById('login-id');
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
async function handleLogin(e) {

    e.preventDefault();

    const loginId =
        document.getElementById('login-id').value;

    const password =
        document.getElementById('password').value;

    const formData = new URLSearchParams();

    formData.append('loginId', loginId);
    formData.append('password', password);
    formData.append('role', currentRole);
	
    try {
        const response = await fetch('login', {
            method: 'POST',
            headers: {
                'Content-Type':
                    'application/x-www-form-urlencoded'
            },
            body: formData
        });

        const result = await response.json();

        if (result.success) {

            showDashboard(result);

        } else {

            alert(result.message);
        }

    } catch (error) {

        console.error('Login error:', error);

        alert('Unable to connect to server.');
    }
}

/* SHOW DASHBOARD */

function showDashboard(user) {

    document.getElementById('auth-screen').style.display = 'none';

    document.getElementById('app-screen').style.display = 'block';

    /* CONFIGURE PROFILE */

    document.getElementById('active-user-name').innerText =
        user.name;

    document.getElementById('active-user-role').innerText =
        user.role.toUpperCase() + ' ACCESS';

    /* SHOW ROLE PANEL */

    document.querySelectorAll('.role-panel').forEach(panel => {
        panel.classList.remove('active');
    });

    document.getElementById(
        `panel-${user.role}`
    ).classList.add('active');

    /* UPDATE POINTS */

    if (user.role === 'user') {

        document.getElementById('user-points').innerHTML =
            `<i class="fa-solid fa-coins"></i> ${user.points} PTS`;
    }
}
function handleLogout() {
    document.getElementById('app-screen').style.display = 'none';
    document.getElementById('auth-screen').style.display = 'block';
}

function previewImage(input) {
    if (input.files && input.files[0]) {
        document.getElementById('upload-label').innerText = "Attached: " + input.files[0].name;
    }
}

function autoDetectLocation() {
    const gpsLabel = document.getElementById('gps-status');
    gpsLabel.innerText = "Locating Coordinates...";
    setTimeout(() => {
        gpsLabel.innerText = "Lat: 28.6139° N, Long: 77.2090° E";
    }, 800);
}

function handleReportSubmit(e) {
    e.preventDefault();
    const desc = document.getElementById('issue-desc').value;
    const newId = '#GR-' + Math.floor(100 + Math.random() * 900);

    // User Table Update
    const userTable = document.getElementById('user-reports-table');
    userTable.insertAdjacentHTML('afterbegin', `<tr>
                <td>${newId}</td>
                <td>${desc}</td>
                <td><span class="badge badge-pending">Pending</span></td>
                <td>Pending</td>
            </tr>`);

    // Admin Queue Update
    const adminTable = document.getElementById('admin-table-body');
    adminTable.insertAdjacentHTML('afterbegin', `<tr data-status="Pending">
                <td>${newId}</td>
                <td>${desc}</td>
                <td><span class="badge badge-pending">Pending</span></td>
                <td>
                    <select class="team-assignee">
                        <option value="">Select Vehicle Unit...</option>
                        <option value="Truck Alpha">Truck Alpha (North)</option>
                        <option value="Truck Beta">Truck Beta (South)</option>
                    </select>
                </td>
                <td><button class="btn" onclick="assignTask(this, '${newId}')">Dispatch</button></td>
            </tr>`);

    userPoints += 50;
    document.getElementById('user-points').innerHTML = `<i class="fa-solid fa-coins"></i> ${userPoints} PTS`;
    document.getElementById('issue-desc').value = '';
    document.getElementById('upload-label').innerText = "Click to select photo";
    alert(`Report ${newId} logged successfully!`);
}

function assignTask(btn, taskId) {
    const row = btn.closest('tr');
    const selectedTeam = row.querySelector('.team-assignee').value;

    if (!selectedTeam) {
        alert("Please select a driver vehicle before dispatching.");
        return;
    }

    row.setAttribute('data-status', 'Assigned');
    row.cells[2].innerHTML = '<span class="badge badge-assigned">Assigned</span>';
    btn.disabled = true;
    btn.innerText = "Dispatched";

    // Add to Driver Queue
    const driverTable = document.getElementById('driver-table-body');
    const desc = row.cells[1].innerText;
    driverTable.insertAdjacentHTML('afterbegin', `<tr>
                <td>${taskId}</td>
                <td>${desc}</td>
                <td><button class="btn btn-outline"><i class="fa-solid fa-diamond-turn-right"></i> Navigate</button></td>
                <td><button class="btn" onclick="completeTask(this)">Mark Collected</button></td>
            </tr>`);
}

function completeTask(btn) {
    const row = btn.closest('tr');
    row.style.opacity = '0.5';
    btn.disabled = true;
    btn.innerText = "Completed";
}

function filterAdminTable() {
    const val = document.getElementById('admin-filter').value;
    const rows = document.querySelectorAll('#admin-table-body tr');
    rows.forEach(row => {
        row.style.display = (val === 'all' || row.getAttribute('data-status') === val) ? '' : 'none';
    });
}