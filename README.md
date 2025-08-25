



<h1 align="center">Movie booking API</h1>

<p align="center">
Base URL:- https://movieticketbookingapi-production.up.railway.app/
</p>

---

### 🧾 Features
CRUD APIs for movie
CRUD APIs for theater hall layout with booked and empty seats
CRUD APIs for theater details
CRUD APIs for movie price
Booking seats as a group for a movie at a specific time and theater
Seats in a group booking should be together
Suggest alternate movies and timings if desired seats can't be booked together
Handle concurrency to prevent double booking of the same seat
Register movie theaters and halls with layout details (rows, seats per row)
Movie halls can have any number of seats in a row, minimum 6

---


---

### 🛠️ Tech Stack
- **Language**: Java,Spring Boot,postgres SQL

---



---

### 🧠 Important API endpoints
### POST https://movieticketbookingapi-production.up.railway.app/api/theaters
{
    "name":"India pride",
    "location":"pune"
}

###  POST https://movieticketbookingapi-production.up.railway.app/api/movies
{
  "title": "Inception",
  "description": "A sci-fi thriller about dreams within dreams.",
  "price": 250.50,
  "duration": 148
}

### POST https://movieticketbookingapi-production.up.railway.app/api/halls
{
  "name": "Grand Theater",
  "rows": 5,
  "rowSeatCounts": {
    "1": 10,
    "2": 12
  },
  "theaterId": 1
}

### POST https://movieticketbookingapi-production.up.railway.app/api/shows
{
  "movieId": 1,
  "hallId": 1,
  "startTime": "2025-08-25T18:30:00"
}

### POST https://movieticketbookingapi-production.up.railway.app/api/booking/confirm/1
[4,5]

###  GET https://movieticketbookingapi-production.up.railway.app/api/shows/1
(shows booked and empty seats as well)

### GET https://movieticketbookingapi-production.up.railway.app/api/analytics/movie/1?startDate=2025-08-01&endDate=2025-08-26 (for ananlysis purpose)
### output
{
    "movie": "Inception",
    "period": "2025-08-01 to 2025-08-26",
    "gmv": 501,
    "totalTicketsBooked": 2
}


<p align="center">
You can access other endpoints by exploring controller classes
</p>


---


### ⭐ Show some love!

Thanks for reading

---




