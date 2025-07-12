let currentUserLatLng = null;
let currentRouteLine = null;

const hcmBounds = L.latLngBounds([10.676, 106.520], [11.100, 107.020]);

const map = L.map("map", {
    maxBounds: hcmBounds,
    maxBoundsViscosity: 1.0,
    minZoom: 12,
    maxZoom: 18
}).setView([10.7769, 106.7009], 13);

L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: '© OpenStreetMap contributors'
}).addTo(map);

fetch("/json/metro-line1.json")
    .then(res => res.json())
    .then(data => {
        L.geoJSON(data, {
            style: {
                color: "red",
                weight: 5,
                opacity: 0.9
            }
        }).addTo(map);
    });

const stationMarkers = {};

stations.forEach(station => {
    const marker = L.marker([station.latitude, station.longitude]).addTo(map);
    const popup = createPopupHTML(station);
    marker.bindPopup(popup);
    stationMarkers[station.stationCode] = marker;
});

function createPopupHTML(station) {
    let html = `<strong>🚉 Ga ${station.name}</strong><br/>
                Mã ga: ${station.stationCode}<br/>
                Địa chỉ: ${station.address || 'Không có dữ liệu'}<br/><br/>
                <strong>🚌 Tuyến buýt gần:</strong><ul>`;

    if (station.nearbyBusRoutes?.length) {
        station.nearbyBusRoutes.forEach((route, index) => {
            html += `<li>
                    <strong>${route.routeNumber}</strong>: ${route.name}<br/>
                    📍 ${route.description || 'Không rõ vị trí'}<br/>
                    🚶 ${route.walkingDistanceM}m ~ ${route.walkingTimeMinutes} phút<br/>
                    <button onclick="toggleDetails('${station.stationCode}', ${index})" style="margin-top:4px;padding:4px 8px;font-size:12px;">Xem chi tiết</button>
                    <div id="details-${station.stationCode}-${index}" style="display:none;margin-top:6px;"></div>
                </li><br/>`;
        });
    } else {
        html += "<li>Không có dữ liệu</li>";
    }

    html += "</ul>";
    return html;
}

function toggleDetails(stationCode, index) {
    const divId = `details-${stationCode}-${index}`;
    const div = document.getElementById(divId);
    if (!div) return;

    const route = stations.find(s => s.stationCode === stationCode)?.nearbyBusRoutes[index];
    if (!route) {
        div.innerHTML = "Không tìm thấy thông tin chi tiết.";
        return;
    }

    if (div.style.display === "none") {
        let detailHTML = `⏱ Tần suất: ${route.frequency || 'Không rõ'}<br/>`;
        if (route.directionForward && route.directionBackward) {
            detailHTML += `🔁 <u>Lượt đi</u>: ${route.directionForward}<br/>🔄 <u>Lượt về</u>: ${route.directionBackward}<br/>`;
        } else if (route.direction) {
            detailHTML += `🔁 <u>Chiều tuyến</u>: ${route.direction}<br/>`;
        } else {
            detailHTML += `❓ Không có dữ liệu chiều tuyến.<br/>`;
        }
        div.innerHTML = detailHTML;
        div.style.display = "block";
    } else {
        div.style.display = "none";
    }
}

function drawRouteToStation(station) {
    if (!currentUserLatLng) {
        alert("Không xác định được vị trí của bạn.");
        return;
    }

    const start = currentUserLatLng;
    const end = [station.latitude, station.longitude];

    const url = `https://router.project-osrm.org/route/v1/foot/${start[1]},${start[0]};${end[1]},${end[0]}?overview=full&geometries=geojson`;

    fetch(url)
        .then(res => res.json())
        .then(data => {
            const coords = data.routes[0].geometry.coordinates.map(c => [c[1], c[0]]);
            if (currentRouteLine) map.removeLayer(currentRouteLine);
            currentRouteLine = L.polyline(coords, {
                color: 'blue',
                weight: 4,
                opacity: 0.8
            }).addTo(map);
        })
        .catch(err => {
            console.error("Lỗi khi vẽ đường đi:", err);
            alert("Không thể tính được đường đi.");
        });
}

// Xác định vị trí người dùng
if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(pos => {
        currentUserLatLng = [pos.coords.latitude, pos.coords.longitude];
        const icon = L.icon({
            iconUrl: "https://cdn-icons-png.flaticon.com/512/684/684908.png",
            iconSize: [30, 30],
            iconAnchor: [15, 30],
            popupAnchor: [0, -30]
        });
        L.marker(currentUserLatLng, { icon }).addTo(map)
            .bindPopup("📍 Vị trí của bạn").openPopup();
        map.setView(currentUserLatLng, 15);
    }, err => {
        console.warn("Không lấy được vị trí:", err.message);
    });
}

const selectEl = document.getElementById("stationSelect");
const infoEl = document.getElementById("stationInfo");

selectEl.addEventListener("change", () => {
    const code = selectEl.value;
    if (code && stationMarkers[code]) {
        const marker = stationMarkers[code];
        map.setView(marker.getLatLng(), 15);
        marker.openPopup();

        const station = stations.find(s => s.stationCode === code);
        if (station) {
            infoEl.innerHTML = createPopupHTML(station);
            drawRouteToStation(station);
        }
    } else {
        infoEl.innerHTML = "🔍 Chọn một ga để xem thông tin chi tiết ở đây.";
    }
});

const searchInput = document.getElementById("searchInput");
const searchResults = document.getElementById("searchResults");

// Gõ để tìm ga
searchInput.addEventListener("input", () => {
    const keyword = searchInput.value.trim().toLowerCase();
    searchResults.innerHTML = "";

    if (!keyword) return;

    const matched = stations.filter(st => st.name.toLowerCase().includes(keyword));

    matched.forEach(station => {
        const li = document.createElement("li");
        li.textContent = station.name;
        li.onclick = () => {
            selectStationByCode(station.stationCode);
            searchInput.value = "";
            searchResults.innerHTML = "";
        };
        searchResults.appendChild(li);
    });
});

// Chọn ga theo mã code
function selectStationByCode(code) {
    const marker = stationMarkers[code];
    const station = stations.find(s => s.stationCode === code);
    if (!marker || !station) return;

    marker.openPopup();
    map.setView(marker.getLatLng(), 15);
    infoEl.innerHTML = createPopupHTML(station);
    drawRouteToStation(station);
}