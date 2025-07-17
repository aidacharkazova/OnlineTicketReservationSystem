// src/main/resources/static/js/app.js

const API_BASE_URL = 'http://localhost:8080'; // Ensure this matches your backend URL
const JWT_STORAGE_KEY = 'jwt_token';

// Utility Functions
function getToken() {
    return localStorage.getItem(JWT_STORAGE_KEY);
}

function setToken(token) {
    localStorage.setItem(JWT_STORAGE_KEY, token);
}

function removeToken() {
    localStorage.removeItem(JWT_STORAGE_KEY);
    // Redirect to login or home after logout
    window.location.href = '/login';
}

function getAuthHeaders() {
    const token = getToken();
    return token ? { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' } : { 'Content-Type': 'application/json' };
}

function displayMessage(element, message, type = 'success') {
    element.textContent = message;
    element.className = `message ${type}-message`;
    element.style.display = 'block';
}

function hideMessage(element) {
    element.textContent = '';
    element.className = 'message';
    element.style.display = 'none';
}

async function fetchData(url, options = {}) {
    try {
        const response = await fetch(url, {
            ...options,
            headers: {
                ...getAuthHeaders(), // Always include auth headers
                ...options.headers,
            },
        });

        if (!response.ok) {
            // Handle 401/403 specifically for redirection
            if (response.status === 401 || response.status === 403) {
                removeToken(); // Clear invalid token
                window.location.href = `/login?redirect=${encodeURIComponent(window.location.pathname)}`;
                return; // Prevent further processing
            }
            const errorData = await response.json().catch(() => ({ message: response.statusText }));
            throw new Error(errorData.message || 'An unexpected error occurred.');
        }
        return await response.json();
    } catch (error) {
        console.error('API call failed:', error);
        throw error; // Re-throw to be caught by specific page logic
    }
}

async function postData(url, data) {
    return fetchData(url, {
        method: 'POST',
        body: JSON.stringify(data),
    });
}

async function putData(url, data) {
    return fetchData(url, {
        method: 'PUT',
        body: JSON.stringify(data),
    });
}

async function deleteData(url) {
    return fetchData(url, {
        method: 'DELETE',
    });
}

// Date formatting utility
function formatDateTime(isoString) {
    if (!isoString) return 'N/A';
    try {
        // Parse the ISO 8601 string to a Date object
        const date = new Date(isoString);

        // Options for formatting
        const options = {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
            hour12: false // Use 24-hour format
        };

        // Format to a locale-specific string
        return date.toLocaleString(undefined, options);
    } catch (e) {
        console.error("Error formatting date:", e);
        return isoString; // Fallback to original string if error
    }
}

// --- Page Specific Logic ---
document.addEventListener('DOMContentLoaded', () => {
    // Add logout functionality to a common logout button/link if it exists
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', (e) => {
            e.preventDefault();
            removeToken();
        });
    }

    const path = window.location.pathname;

    if (path === '/' || path.includes('/events') && !path.includes('/events/')) {
        loadEventsList();
    } else if (path.includes('/login')) {
        setupLoginPage();
    } else if (path.includes('/register')) {
        setupRegisterPage();
    } else if (path.match(/\/events\/\d+$/)) { // Match /events/{id}
        setupEventDetailPage();
    } else if (path.includes('/my-tickets')) {
        setupMyTicketsPage();
    } else if (path.includes('/events/create')) {
        setupCreateEventPage();
    } else if (path.includes('/venues/create')) {
        setupCreateVenuePage();
    }
});

// Event List Page (Home)
async function loadEventsList() {
    const eventsListDiv = document.getElementById('eventsList');
    if (!eventsListDiv) return;

    try {
        const events = await fetchData(`${API_BASE_URL}/api/events`);
        eventsListDiv.innerHTML = ''; // Clear previous content

        if (events.length === 0) {
            eventsListDiv.innerHTML = '<p>No events found. Check back later!</p>';
            return;
        }

        events.forEach(event => {
            const eventItem = document.createElement('div');
            eventItem.className = 'event-item';
            eventItem.innerHTML = `
                <div class="event-details">
                    <h3>${event.title}</h3>
                    <p>Category: ${event.category}</p>
                    <p>Description: ${event.description}</p>
                    <p>Price: $${event.price.toFixed(2)}</p>
                </div>
                <div class="item-actions">
                    <a href="/events/${event.id}">View Details</a>
                </div>
            `;
            eventsListDiv.appendChild(eventItem);
        });
    } catch (error) {
        eventsListDiv.innerHTML = `<p class="error-message">Failed to load events: ${error.message}</p>`;
    }
}

// Login Page
function setupLoginPage() {
    const loginForm = document.getElementById('loginForm');
    const messageDiv = document.getElementById('message');

    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            hideMessage(messageDiv);

            const email = loginForm.email.value;
            const password = loginForm.password.value;

            try {
                const response = await postData(`${API_BASE_URL}/auth/login`, { email, password });
                setToken(response.token);
                displayMessage(messageDiv, 'Login successful!', 'success');

                // Redirect to the page they were trying to access, or home
                const urlParams = new URLSearchParams(window.location.search);
                const redirect = urlParams.get('redirect') || '/';
                window.location.href = redirect;

            } catch (error) {
                displayMessage(messageDiv, `Login failed: ${error.message}`, 'error');
            }
        });
    }
}

// Register Page
function setupRegisterPage() {
    const registerForm = document.getElementById('registerForm');
    const messageDiv = document.getElementById('message');

    if (registerForm) {
        registerForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            hideMessage(messageDiv);

            const userName = registerForm.userName.value;
            const email = registerForm.email.value;
            const password = registerForm.password.value;

            try {
                await postData(`${API_BASE_URL}/auth/signUp`, { userName, email, password });
                displayMessage(messageDiv, 'Registration successful! You can now log in.', 'success');
                registerForm.reset();
                // Optional: Redirect to login page
                // setTimeout(() => window.location.href = '/login', 2000);
            } catch (error) {
                displayMessage(messageDiv, `Registration failed: ${error.message}`, 'error');
            }
        });
    }
}

// Event Detail Page
async function setupEventDetailPage() {
    const eventId = document.getElementById('eventId').value; // Hidden input in Thymeleaf
    const eventDetailsDiv = document.getElementById('eventDetails');
    const schedulesListDiv = document.getElementById('schedulesList');
    const messageDiv = document.getElementById('bookingMessage');

    if (!eventId || !eventDetailsDiv || !schedulesListDiv) return;

    try {
        const event = await fetchData(`${API_BASE_URL}/api/events/${eventId}`);
        if (event) {
            eventDetailsDiv.innerHTML = `
                <h2>${event.title}</h2>
                <p><strong>Category:</strong> ${event.category}</p>
                <p><strong>Description:</strong> ${event.description}</p>
                <p><strong>Price:</strong> $${event.price.toFixed(2)}</p>
            `;
            loadSchedulesForEvent(eventId, schedulesListDiv, messageDiv);
        } else {
            eventDetailsDiv.innerHTML = `<p class="error-message">Event not found.</p>`;
        }
    } catch (error) {
        eventDetailsDiv.innerHTML = `<p class="error-message">Failed to load event details: ${error.message}</p>`;
    }
}

async function loadSchedulesForEvent(eventId, schedulesListDiv, messageDiv) {
    schedulesListDiv.innerHTML = '<h3>Available Schedules</h3>';
    try {
        const allSchedules = await fetchData(`${API_BASE_URL}/api/schedules`);
        const eventSchedules = allSchedules.filter(schedule => schedule.eventId === parseInt(eventId));

        if (eventSchedules.length === 0) {
            schedulesListDiv.innerHTML += '<p>No schedules available for this event yet.</p>';
            return;
        }

        for (const schedule of eventSchedules) {
            // Fetch venue details for each schedule
            const venue = await fetchData(`${API_BASE_URL}/api/venues/${schedule.venueId}`);
            const venueName = venue ? venue.name : 'Unknown Venue';
            const venueLocation = venue ? venue.location : 'N/A';

            const scheduleItem = document.createElement('div');
            scheduleItem.className = 'schedule-item';
            scheduleItem.innerHTML = `
                <div class="schedule-details">
                    <p><strong>Venue:</strong> ${venueName} (${venueLocation})</p>
                    <p><strong>Starts:</strong> ${formatDateTime(schedule.startTime)}</p>
                    <p><strong>Ends:</strong> ${formatDateTime(schedule.endTime)}</p>
                    <div class="seats-list" id="seats-schedule-${schedule.id}">
                        <p>Loading seats...</p>
                    </div>
                </div>
            `;
            schedulesListDiv.appendChild(scheduleItem);

            // Load seats for this schedule
            await loadSeatsForSchedule(schedule.id, document.getElementById(`seats-schedule-${schedule.id}`), messageDiv);
        }
    } catch (error) {
        schedulesListDiv.innerHTML = `<p class="error-message">Failed to load schedules: ${error.message}</p>`;
    }
}


async function loadSeatsForSchedule(scheduleId, seatsListDiv, messageDiv) {
    seatsListDiv.innerHTML = '<h4>Available Seats:</h4>';
    try {
        const seats = await fetchData(`${API_BASE_URL}/api/seats?scheduleId=${scheduleId}`);

        if (seats.length === 0) {
            seatsListDiv.innerHTML += '<p>No seats available for this schedule.</p>';
            return;
        }

        const availableSeats = seats.filter(seat => !seat.isBooked); // Assuming 'isBooked' property for seat availability
        if (availableSeats.length === 0) {
            seatsListDiv.innerHTML += '<p>All seats are currently booked.</p>';
            return;
        }

        availableSeats.forEach(seat => {
            const seatItem = document.createElement('div');
            seatItem.className = 'seat-item';
            seatItem.innerHTML = `
                <div class="seat-details">
                    <p>Seat: ${seat.row}${seat.seatNumber}</p>
                </div>
                <div class="item-actions">
                    <button class="book-seat-btn" data-seat-id="${seat.id}" data-schedule-id="${scheduleId}">Book Seat</button>
                </div>
            `;
            seatsListDiv.appendChild(seatItem);
        });

        seatsListDiv.querySelectorAll('.book-seat-btn').forEach(button => {
            button.addEventListener('click', async (e) => {
                hideMessage(messageDiv);
                const seatId = e.target.dataset.seatId;
                const scheduleId = e.target.dataset.scheduleId;

                try {
                    // userId is assumed to be handled by backend from JWT
                    await postData(`${API_BASE_URL}/api/tickets`, { seatId: parseInt(seatId), scheduleId: parseInt(scheduleId) });
                    displayMessage(messageDiv, 'Ticket booked successfully!', 'success');
                    // Refresh seats for this schedule
                    await loadSeatsForSchedule(scheduleId, seatsListDiv, messageDiv);
                } catch (error) {
                    displayMessage(messageDiv, `Booking failed: ${error.message}`, 'error');
                }
            });
        });
    } catch (error) {
        seatsListDiv.innerHTML = `<p class="error-message">Failed to load seats: ${error.message}</p>`;
    }
}


// My Tickets Page
async function setupMyTicketsPage() {
    const myTicketsListDiv = document.getElementById('myTicketsList');
    if (!myTicketsListDiv) return;

    try {
        const tickets = await fetchData(`${API_BASE_URL}/api/tickets/my`);
        myTicketsListDiv.innerHTML = '';

        if (tickets.length === 0) {
            myTicketsListDiv.innerHTML = '<p>You have no tickets booked yet.</p>';
            return;
        }

        for (const ticket of tickets) {
            const schedule = await fetchData(`${API_BASE_URL}/api/schedules/${ticket.scheduleId}`);
            const event = schedule ? await fetchData(`${API_BASE_URL}/api/events/${schedule.eventId}`) : null;
            const seat = await fetchData(`${API_BASE_URL}/api/seats/${ticket.seatId}`);
            const venue = schedule ? await fetchData(`${API_BASE_URL}/api/venues/${schedule.venueId}`) : null;


            const ticketItem = document.createElement('div');
            ticketItem.className = 'ticket-item';
            ticketItem.innerHTML = `
                <div class="ticket-details">
                    <h3>Event: ${event ? event.title : 'N/A'}</h3>
                    <p>Venue: ${venue ? venue.name : 'N/A'} (${venue ? venue.location : 'N/A'})</p>
                    <p>Seat: ${seat ? seat.row + seat.seatNumber : 'N/A'}</p>
                    <p>Time: ${schedule ? formatDateTime(schedule.startTime) : 'N/A'} - ${schedule ? formatDateTime(schedule.endTime) : 'N/A'}</p>
                    <p>Booking Date: ${formatDateTime(ticket.bookingDate)}</p>
                </div>
            `;
            myTicketsListDiv.appendChild(ticketItem);
        }
    } catch (error) {
        myTicketsListDiv.innerHTML = `<p class="error-message">Failed to load your tickets: ${error.message}</p>`;
    }
}

// Create Event Page
function setupCreateEventPage() {
    const createEventForm = document.getElementById('createEventForm');
    const messageDiv = document.getElementById('message');

    if (createEventForm) {
        createEventForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            hideMessage(messageDiv);

            const title = createEventForm.title.value;
            const description = createEventForm.description.value;
            const category = createEventForm.category.value;
            const price = parseFloat(createEventForm.price.value);

            try {
                await postData(`${API_BASE_URL}/api/events`, { title, description, category, price });
                displayMessage(messageDiv, 'Event created successfully!', 'success');
                createEventForm.reset();
            } catch (error) {
                displayMessage(messageDiv, `Failed to create event: ${error.message}`, 'error');
            }
        });
    }
}

// Create Venue Page
function setupCreateVenuePage() {
    const createVenueForm = document.getElementById('createVenueForm');
    const messageDiv = document.getElementById('message');

    if (createVenueForm) {
        createVenueForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            hideMessage(messageDiv);

            const name = createVenueForm.name.value;
            const location = createVenueForm.location.value;

            try {
                await postData(`${API_BASE_URL}/api/venues`, { name, location });
                displayMessage(messageDiv, 'Venue created successfully!', 'success');
                createVenueForm.reset();
            } catch (error) {
                displayMessage(messageDiv, `Failed to create venue: ${error.message}`, 'error');
            }
        });
    }
}