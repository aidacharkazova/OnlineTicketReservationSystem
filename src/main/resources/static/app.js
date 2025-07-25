// src/main/resources/static/js/app.js

const API_BASE_URL = 'http://localhost:8080'; // Ensure this matches your backend URL
const JWT_ACCESS_TOKEN_KEY = 'jwt_access_token';
const JWT_REFRESH_TOKEN_KEY = 'jwt_refresh_token';

// --- Utility Functions ---

function getAccessToken() {
    return localStorage.getItem(JWT_ACCESS_TOKEN_KEY);
}

function setAccessToken(token) {
    localStorage.setItem(JWT_ACCESS_TOKEN_KEY, token);
}

function removeAccessToken() {
    localStorage.removeItem(JWT_ACCESS_TOKEN_KEY);
}

function getRefreshToken() {
    return localStorage.getItem(JWT_REFRESH_TOKEN_KEY);
}

function setRefreshToken(token) {
    localStorage.setItem(JWT_REFRESH_TOKEN_KEY, token);
}

function removeRefreshToken() {
    localStorage.removeItem(JWT_REFRESH_TOKEN_KEY);
}

function isLoggedIn() {
    return !!getAccessToken(); // Returns true if token exists, false otherwise
}

function displayMessage(elementId, message, type = 'success') {
    const element = document.getElementById(elementId);
    if (element) {
        element.textContent = message;
        element.className = `message ${type}-message`;
        element.style.display = 'block';
    }
}

function hideMessage(elementId) {
    const element = document.getElementById(elementId);
    if (element) {
        element.textContent = '';
        element.className = 'message'; // Reset classes
        element.style.display = 'none';
    }
}

async function apiCall(endpoint, method = 'GET', data = null) {
    const headers = {
        'Content-Type': 'application/json',
    };

    const accessToken = getAccessToken();
    if (accessToken) {
        headers['Authorization'] = `Bearer ${accessToken}`;
    }

    const options = {
        method,
        headers,
    };

    if (data) {
        options.body = JSON.stringify(data);
    }

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, options);

        if (response.status === 401 || response.status === 403) {
            // Attempt to refresh token or redirect to login
            const refreshToken = getRefreshToken();
            if (refreshToken) {
                try {
                    const refreshResponse = await fetch(`${API_BASE_URL}/auth/refresh`, {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ token: refreshToken })
                    });
                    if (refreshResponse.ok) {
                        const refreshData = await refreshResponse.json();
                        setAccessToken(refreshData.accessToken);
                        // Retry the original request with the new token
                        headers['Authorization'] = `Bearer ${refreshData.accessToken}`;
                        const retryResponse = await fetch(`${API_BASE_URL}${endpoint}`, options);
                        if (retryResponse.ok) {
                            return retryResponse.status === 204 ? null : await retryResponse.json();
                        }
                    }
                } catch (refreshError) {
                    console.error('Token refresh failed:', refreshError);
                }
            }
            // If refresh failed or no refresh token, force logout
            logoutUser();
            return; // Stop further execution
        }

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({ message: response.statusText || 'Unknown error' }));
            throw new Error(errorData.message || 'API request failed.');
        }

        if (response.status === 204) { // No Content
            return null;
        }

        return await response.json();

    } catch (error) {
        console.error('API call error:', error);
        throw error; // Re-throw to be handled by specific page logic
    }
}

// Date formatting utility
function formatDateTime(isoString) {
    if (!isoString) return 'N/A';
    try {
        const date = new Date(isoString);
        if (isNaN(date.getTime())) { // Check for invalid date
            return isoString;
        }
        const options = {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
            hour12: false // Use 24-hour format
        };
        return date.toLocaleString(undefined, options);
    } catch (e) {
        console.error("Error formatting date:", e);
        return isoString; // Fallback to original string if error
    }
}

function logoutUser() {
    removeAccessToken();
    removeRefreshToken();
    updateAuthLinks(); // Update navbar
    window.location.href = '/login'; // Redirect to login page
}

// --- Dynamic UI Updates ---

function updateAuthLinks() {
    const authLinksContainer = document.getElementById('auth-links');
    const userLinksContainer = document.getElementById('user-links');

    if (!authLinksContainer || !userLinksContainer) return;

    if (isLoggedIn()) {
        authLinksContainer.classList.add('hidden');
        userLinksContainer.classList.remove('hidden');
    } else {
        authLinksContainer.classList.remove('hidden');
        userLinksContainer.classList.add('hidden');
    }
}

// --- Page Specific Logic (DOMContentLoaded ensures elements are ready) ---

document.addEventListener('DOMContentLoaded', () => {
    updateAuthLinks(); // Initial update of navbar links

    // Logout button handler (if present in layout.html)
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', (e) => {
            e.preventDefault();
            logoutUser();
        });
    }

    // --- Login Page Logic ---
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            hideMessage('loginMessage');
            const email = loginForm.email.value;
            const password = loginForm.password.value;

            try {
                const data = await apiCall('/auth/login', 'POST', { email, password });
                setAccessToken(data.accessToken);
                setRefreshToken(data.refreshToken);
                displayMessage('loginMessage', 'Login successful!', 'success');
                updateAuthLinks(); // Update navbar
                // Redirect to home or original requested page
                const urlParams = new URLSearchParams(window.location.search);
                const redirectPath = urlParams.get('redirect') || '/';
                window.location.href = redirectPath;
            } catch (error) {
                displayMessage('loginMessage', `Login failed: ${error.message}`, 'error');
            }
        });
    }

    // --- Register Page Logic ---
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            hideMessage('registerMessage');
            const username = registerForm.username.value;
            const email = registerForm.email.value;
            const password = registerForm.password.value;

            try {
                await apiCall('/auth/signUp', 'POST', { username, email, password });
                displayMessage('registerMessage', 'Registration successful! You can now log in.', 'success');
                registerForm.reset();
                // Optional: Redirect to login page after a short delay
                // setTimeout(() => window.location.href = '/login', 2000);
            } catch (error) {
                displayMessage('registerMessage', `Registration failed: ${error.message}`, 'error');
            }
        });
    }

    // --- Home Page (Event List) Logic ---
    const eventsListDiv = document.getElementById('eventsList');
    if (eventsListDiv) {
        const fetchEvents = async () => {
            eventsListDiv.innerHTML = '<p class="text-center text-gray-600">Loading events...</p>';
            try {
                const events = await apiCall('/api/events');
                eventsListDiv.innerHTML = ''; // Clear loading message

                if (events.length === 0) {
                    eventsListDiv.innerHTML = '<p class="text-center text-gray-600">No events found. Check back later!</p>';
                    return;
                }

                events.forEach(event => {
                    const eventCard = document.createElement('div');
                    eventCard.className = 'bg-white p-6 rounded-lg shadow-lg hover:shadow-xl transition-shadow duration-300';
                    eventCard.innerHTML = `
                        <img src="https://placehold.co/400x200/ADD8E6/000000?text=Event+Image" alt="Event Image" class="w-full h-48 object-cover rounded-md mb-4 shadow-sm">
                        <h3 class="text-2xl font-bold text-blue-700 mb-2">${event.name}</h3>
                        <p class="text-gray-700 mb-2">${event.description}</p>
                        <p class="text-sm text-gray-500 mb-3">Category: ${event.category}</p>
                        <p class="text-xl font-semibold text-green-600 mb-4">Price: $${event.price.toFixed(2)}</p>
                        <a href="/events/${event.id}" class="inline-block bg-blue-500 hover:bg-blue-600 text-white px-5 py-2 rounded-lg transition-colors duration-200 shadow-md">
                            View Details
                        </a>
                    `;
                    eventsListDiv.appendChild(eventCard);
                });
            } catch (error) {
                eventsListDiv.innerHTML = `<p class="text-center text-red-500">Error loading events: ${error.message}</p>`;
            }
        };
        fetchEvents();
    }

    // --- Event Detail Page Logic ---
    const eventDetailContainer = document.getElementById('eventDetailContainer');
    if (eventDetailContainer) {
        const eventId = eventDetailContainer.dataset.eventId; // Get event ID from data attribute
        const eventDetailsDiv = document.getElementById('eventDetails');
        const schedulesListDiv = document.getElementById('schedulesList');
        const bookingMessageDiv = document.getElementById('bookingMessage');

        const loadEventDetails = async () => {
            if (!eventId) {
                eventDetailsDiv.innerHTML = `<p class="text-center text-red-500">Event ID not found.</p>`;
                return;
            }
            try {
                const event = await apiCall(`/api/events/${eventId}`);
                eventDetailsDiv.innerHTML = `
                    <div class="bg-white p-8 rounded-lg shadow-lg mb-6">
                        <img src="https://placehold.co/800x400/ADD8E6/000000?text=Event+Banner" alt="Event Banner" class="w-full h-64 object-cover rounded-md mb-6 shadow-md">
                        <h2 class="text-4xl font-extrabold text-gray-900 mb-3">${event.name}</h2>
                        <p class="text-gray-700 text-lg mb-4">${event.description}</p>
                        <div class="flex items-center space-x-6 text-gray-600 text-md">
                            <p class="flex items-center"><span class="font-semibold text-blue-600 mr-2">Category:</span> ${event.category}</p>
                            <p class="flex items-center"><span class="font-semibold text-green-600 mr-2">Price:</span> $${event.price.toFixed(2)}</p>
                        </div>
                    </div>
                `;
                loadSchedulesForEvent(eventId, schedulesListDiv, bookingMessageDiv);
            } catch (error) {
                eventDetailsDiv.innerHTML = `<p class="text-center text-red-500">Failed to load event details: ${error.message}</p>`;
            }
        };
        loadEventDetails();

        async function loadSchedulesForEvent(eventId, schedulesListDiv, messageDiv) {
            schedulesListDiv.innerHTML = '<h3 class="text-2xl font-semibold mb-4 text-gray-800">Available Schedules</h3><p class="text-center text-gray-600">Loading schedules...</p>';
            try {
                const allSchedules = await apiCall(`${API_BASE_URL}/api/schedules`);
                // Filter schedules for this specific event
                const eventSchedules = allSchedules.filter(schedule => schedule.eventId === parseInt(eventId));

                schedulesListDiv.innerHTML = '<h3 class="text-2xl font-semibold mb-4 text-gray-800">Available Schedules</h3>'; // Clear loading message

                if (eventSchedules.length === 0) {
                    schedulesListDiv.innerHTML += '<p class="text-center text-gray-600">No schedules available for this event yet.</p>';
                    return;
                }

                for (const schedule of eventSchedules) {
                    const venue = await apiCall(`${API_BASE_URL}/api/venues/${schedule.venueId}`);
                    const venueName = venue ? venue.name : 'Unknown Venue';
                    const venueLocation = venue ? venue.location : 'N/A';

                    const scheduleItem = document.createElement('div');
                    scheduleItem.className = 'bg-white p-6 rounded-lg shadow-md mb-4';
                    scheduleItem.innerHTML = `
                        <h4 class="text-xl font-bold text-gray-800 mb-2">Venue: ${venueName} (<span class="text-blue-500">${venueLocation}</span>)</h4>
                        <p class="text-gray-700 mb-1">Starts: ${formatDateTime(schedule.startTime)}</p>
                        <p class="text-gray-700 mb-3">Ends: ${formatDateTime(schedule.endTime)}</p>
                        <div class="seats-list mt-4 grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-3" id="seats-schedule-${schedule.id}">
                            <p class="col-span-full text-center text-gray-600">Loading seats...</p>
                        </div>
                    `;
                    schedulesListDiv.appendChild(scheduleItem);

                    await loadSeatsForSchedule(schedule.id, document.getElementById(`seats-schedule-${schedule.id}`), messageDiv);
                }
            } catch (error) {
                schedulesListDiv.innerHTML = `<p class="text-center text-red-500">Failed to load schedules: ${error.message}</p>`;
            }
        }

        async function loadSeatsForSchedule(scheduleId, seatsListDiv, messageDiv) {
            seatsListDiv.innerHTML = '<h5 class="col-span-full text-lg font-semibold text-gray-700 mb-2">Available Seats:</h5><p class="col-span-full text-center text-gray-600">Loading seats...</p>';
            try {
                const seats = await apiCall(`${API_BASE_URL}/api/seats/venue/${scheduleId}`); // Assuming scheduleId can be used for venueId to get seats associated with that schedule's venue
                // NOTE: Your API has GET /api/seats?scheduleId=3 and GET /api/seats/venue/{venueId}
                // For this scenario, we need seats available for a specific schedule.
                // Assuming `GET /api/seats?scheduleId=3` returns seats with an `isBooked` property.
                // If not, you'll need to adjust the backend or this fetch.
                // For now, I'll use the `scheduleId` as a query param for seats.
                // Let's adjust to your provided API: `GET /api/seats?scheduleId=3`
                const seatsForSchedule = await apiCall(`/api/seats?scheduleId=${scheduleId}`);


                seatsListDiv.innerHTML = '<h5 class="col-span-full text-lg font-semibold text-gray-700 mb-2">Available Seats:</h5>'; // Clear loading message


                if (seatsForSchedule.length === 0) {
                    seatsListDiv.innerHTML += '<p class="col-span-full text-center text-gray-600">No seats available for this schedule.</p>';
                    return;
                }

                const availableSeats = seatsForSchedule.filter(seat => !seat.isBooked); // Assuming 'isBooked' property
                if (availableSeats.length === 0) {
                    seatsListDiv.innerHTML += '<p class="col-span-full text-center text-gray-600">All seats are currently booked.</p>';
                    return;
                }

                availableSeats.forEach(seat => {
                    const seatButton = document.createElement('button');
                    seatButton.className = 'bg-green-500 hover:bg-green-600 text-white font-bold py-2 px-4 rounded-md transition-colors duration-200 shadow-sm';
                    seatButton.textContent = `${seat.row}${seat.seatNumber}`;
                    seatButton.dataset.seatId = seat.id;
                    seatButton.dataset.scheduleId = scheduleId; // Pass scheduleId for booking
                    seatButton.addEventListener('click', async (e) => {
                        if (!isLoggedIn()) {
                            displayMessage('bookingMessage', 'Please log in to book a ticket.', 'error');
                            setTimeout(() => window.location.href = `/login?redirect=${encodeURIComponent(window.location.pathname)}`, 1500);
                            return;
                        }
                        hideMessage('bookingMessage');
                        const seatId = parseInt(e.target.dataset.seatId);
                        const currentScheduleId = parseInt(e.target.dataset.scheduleId);

                        try {
                            // userId is assumed to be handled by backend from JWT
                            await apiCall('/api/tickets', 'POST', { seatId: seatId, scheduleId: currentScheduleId });
                            displayMessage('bookingMessage', `Ticket for seat ${seat.row}${seat.seatNumber} booked successfully!`, 'success');
                            // Refresh seats for this schedule to show updated availability
                            await loadSeatsForSchedule(currentScheduleId, seatsListDiv, messageDiv);
                        } catch (error) {
                            displayMessage('bookingMessage', `Booking failed: ${error.message}`, 'error');
                        }
                    });
                    seatsListDiv.appendChild(seatButton);
                });
            } catch (error) {
                seatsListDiv.innerHTML = `<p class="col-span-full text-center text-red-500">Failed to load seats: ${error.message}</p>`;
            }
        }
    }

    // --- My Tickets Page Logic ---
    const myTicketsListDiv = document.getElementById('myTicketsList');
    if (myTicketsListDiv) {
        const fetchMyTickets = async () => {
            if (!isLoggedIn()) {
                myTicketsListDiv.innerHTML = `<p class="text-center text-red-500">Please log in to view your tickets.</p>`;
                return;
            }
            myTicketsListDiv.innerHTML = '<p class="text-center text-gray-600">Loading your tickets...</p>';
            try {
                // Your API has /api/tickets/user/{userId} but also /api/tickets/my
                // The spec says "Get My Tickets - Requires auth — gets all tickets of the currently logged-in user."
                // So, assuming /api/tickets/my is the correct endpoint for authenticated user's tickets.
                const tickets = await apiCall('/api/tickets/my'); // Use the /my endpoint if available and handles user ID from JWT
                // If /api/tickets/my is not implemented, you'd need to fetch userId from backend first, then use /api/tickets/user/{userId}
                myTicketsListDiv.innerHTML = ''; // Clear loading message

                if (tickets.length === 0) {
                    myTicketsListDiv.innerHTML = '<p class="text-center text-gray-600">You have no tickets booked yet.</p>';
                    return;
                }

                for (const ticket of tickets) {
                    // Fetch related details for each ticket
                    const schedule = await apiCall(`/api/schedules/${ticket.scheduleId}`);
                    const event = schedule ? await apiCall(`/api/events/${schedule.eventId}`) : null;
                    const seat = await apiCall(`/api/seats/${ticket.seatId}`);
                    const venue = schedule ? await apiCall(`/api/venues/${schedule.venueId}`) : null;

                    const ticketCard = document.createElement('div');
                    ticketCard.className = 'bg-white p-6 rounded-lg shadow-md mb-4 flex justify-between items-center';
                    ticketCard.innerHTML = `
                        <div>
                            <h3 class="text-xl font-bold text-blue-700 mb-1">Event: ${event ? event.name : 'N/A'}</h3>
                            <p class="text-gray-700">Venue: ${venue ? venue.name : 'N/A'} (${venue ? venue.location : 'N/A'})</p>
                            <p class="text-gray-700">Seat: ${seat ? seat.row + seat.seatNumber : 'N/A'}</p>
                            <p class="text-gray-600 text-sm">Time: ${schedule ? formatDateTime(schedule.startTime) : 'N/A'} - ${schedule ? formatDateTime(schedule.endTime) : 'N/A'}</p>
                            <p class="text-gray-600 text-sm">Booking Date: ${formatDateTime(ticket.bookingDate)}</p>
                        </div>
                        <button class="cancel-ticket-btn bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-md transition-colors duration-200" data-ticket-id="${ticket.id}">
                            Cancel Ticket
                        </button>
                    `;
                    myTicketsListDiv.appendChild(ticketCard);
                }

                // Add event listeners for cancel buttons
                myTicketsListDiv.querySelectorAll('.cancel-ticket-btn').forEach(button => {
                    button.addEventListener('click', async (e) => {
                        const ticketId = e.target.dataset.ticketId;
                        if (confirm('Are you sure you want to cancel this ticket?')) {
                            try {
                                await apiCall(`/api/tickets/${ticketId}`, 'DELETE');
                                displayMessage('myTicketsMessage', 'Ticket cancelled successfully!', 'success');
                                fetchMyTickets(); // Reload tickets
                            } catch (error) {
                                displayMessage('myTicketsMessage', `Failed to cancel ticket: ${error.message}`, 'error');
                            }
                        }
                    });
                });

            } catch (error) {
                myTicketsListDiv.innerHTML = `<p class="text-center text-red-500">Error loading your tickets: ${error.message}</p>`;
            }
        };
        fetchMyTickets();
    }

    // --- Create Event Page Logic ---
    const createEventForm = document.getElementById('createEventForm');
    if (createEventForm) {
        createEventForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            hideMessage('createEventMessage');
            const title = createEventForm.title.value;
            const description = createEventForm.description.value;
            const category = createEventForm.category.value;
            const price = parseFloat(createEventForm.price.value);

            try {
                await apiCall('/api/events', 'POST', { name: title, description, category, price }); // Note: 'name' in backend, 'title' in frontend form
                displayMessage('createEventMessage', 'Event created successfully!', 'success');
                createEventForm.reset();
            } catch (error) {
                displayMessage('createEventMessage', `Failed to create event: ${error.message}`, 'error');
            }
        });
    }

    // --- Create Venue Page Logic ---
    const createVenueForm = document.getElementById('createVenueForm');
    if (createVenueForm) {
        createVenueForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            hideMessage('createVenueMessage');
            const name = createVenueForm.name.value;
            const location = createVenueForm.location.value;

            try {
                await apiCall('/api/venues', 'POST', { name, location });
                displayMessage('createVenueMessage', 'Venue created successfully!', 'success');
                createVenueForm.reset();
            } catch (error) {
                displayMessage('createVenueMessage', `Failed to create venue: ${error.message}`, 'error');
            }
        });
    }
});