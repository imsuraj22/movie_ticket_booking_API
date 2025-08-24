

<h1 align="center"Movie Ticket Booking API - RESTful Backend</h1>

<p align="center">
A scalable and modular movie ticket booking system built with REST APIs that supports theater registration, seat layout management, movie scheduling, and intelligent group booking. Designed with real-world features and concurrency handling.
</p>

---

### 🧾 Features
🏢 Register multiple theaters and halls
🎬 Add and manage movies
🪑 Customizable hall layout with variable rows and seats
📅 Schedule shows across different halls and times
👥 Group booking with adjacent seat allocation
⚙️ Intelligent seat suggestion if preferred seats are not available
🔒 Prevents concurrent seat booking collisions
📈 Analytics for ticket sales and GMV (Gross Merchandise Value)

---

### 📚 Base URL

https://movieticketbookingapi-production-4807.up.railway.app


---

### 🛠️ Tech Stack
- **Language**: Java,Spring boot,Postgres SQL



### 🧠 API Endpoints
### POST /api/movies
{
  "title": "Inception",
  "description": "A sci-fi thriller about dreams within dreams.",
  "price": 250.50,
  "duration": 148
}

### 🏢 Theaters
### ➕ Register Theater

### POST /api/theaters
{
  "name": "India Pride",
  "location": "Pune"
}

### 🏟️ Halls
### ➕ Add Hall to Theater
### POST /api/halls
{
  "name": "Grand Theater",
  "rows": 5,
  "rowSeatCounts": {
    "1": 10,
    "2": 12
  },
  "theaterId": 1
}


### 📅 Shows
### ➕ Create Show
### POST /api/shows
{
  "movieId": 1,
  "hallId": 1,
  "startTime": "2025-08-25T18:30:00"
}



### GET /api/shows/:id
Example:
### GET /api/shows/1

### 🪑 Bookings
### ✅ Book Seats for a Show
POST /api/booking/confirm/:showId
Example:
### POST /api/booking/confirm/1
- [4, 5]

If seats are already booked:
Suggests alternative movies or times with adjacent seat availability

### 📊 Analytics (Bonus)
### 📈 Movie Performance Metrics
### GET /api/analytics/movie/:movieId?from=YYYY-MM-DD&to=YYYY-MM-DD
{
  "movieId": 1,
  "title": "Inception",
  "totalTicketsSold": 1200,
  "gmv": 300000
}






