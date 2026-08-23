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

// Map API from LEAFLET
/* 2. DASHBOARD LOCATION MAP, GPS & FILE UPLOAD LOGIC */
document.addEventListener("DOMContentLoaded", function () {
    const mapContainer = document.getElementById('map');
    if (!mapContainer) return; // Safely exit if not on dashboard page

    // Default coordinates (e.g., New Delhi coordinates)
    const defaultLat = 28.6139;
    const defaultLng = 77.2090;

    // Initialize Leaflet Map
    const map = L.map('map').setView([defaultLat, defaultLng], 13);

	L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
	        maxZoom: 19
	    }).addTo(map);

    let marker = null;

    // Helper: Formats coordinates into "lat, lng" and populates input fields
    function updateLocation(lat, lng) {
        const formattedCoords = `${lat.toFixed(6)}, ${lng.toFixed(6)}`;

        // Populate main location text box
        const locInput = document.getElementById('location');
        if (locInput) locInput.value = formattedCoords;

        // Populate hidden inputs
        const latInput = document.getElementById('latitude');
        const lngInput = document.getElementById('longitude');
        if (latInput) latInput.value = lat.toFixed(6);
        if (lngInput) lngInput.value = lng.toFixed(6);

        // Place or move map marker
        const coords = [lat, lng];
        if (marker) {
            marker.setLatLng(coords);
        } else {
            marker = L.marker(coords).addTo(map);
        }
        map.setView(coords, 15);
    }

    // --- OPTION 1: Map Click Event ---
    map.on('click', function (e) {
        updateLocation(e.latlng.lat, e.latlng.lng);
    });

    // --- OPTION 2: Detect GPS Button Event ---
    const gpsBtn = document.getElementById('btn-detect-gps');
    if (gpsBtn) {
        gpsBtn.addEventListener('click', function () {
            if (!navigator.geolocation) {
                alert("Geolocation is not supported by your browser.");
                return;
            }

            gpsBtn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Locating...';

            navigator.geolocation.getCurrentPosition(
                (position) => {
                    updateLocation(position.coords.latitude, position.coords.longitude);
                    gpsBtn.innerHTML = '<i class="fa-solid fa-location-crosshairs"></i> Detect Current GPS';
                },
                (error) => {
                    alert("Unable to fetch location. Please allow GPS permissions in your browser.");
                    gpsBtn.innerHTML = '<i class="fa-solid fa-location-crosshairs"></i> Detect Current GPS';
                },
                { enableHighAccuracy: true, timeout: 10000 }
            );
        });
    }
});
