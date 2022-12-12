
# Yoga Ktor Backend

Backend for booking Yoga Classes built on Ktor-Server

### Approach
- Built *HTTP POST* end points for the login, signup and register features with corresponding models for the Response request and Body.
- Implemented the *HTTP GET* end point for authentication, which contains the JWT Auth token in header and unique user id in the query param.
- Used Kotlin Coroutines for multithreading
- Used salting and hashing techniques and algorithms like SHA256 & HMAC signature, to protect the user data and privacy.
- Used a clean architecture and basic design patterns for scalability.

### Tools & Technologies Used -
- Backend: Kotlin Ktor-Server
- Database: KMongo Atlas
- Container: Docker
- IDE: IntelliJ Idea
- API Testing: Postman

### Database Schema and ER Diagram

Schema for the NoSQL collection -

![db-schema](https://github.com/Lord-Lava/ktor-yoga-backend/blob/master/db-schema.png?raw=true)

ER Diagram -

![db-schema](https://github.com/Lord-Lava/ktor-yoga-backend/blob/master/er-diagram.png?raw=true)

## API Reference

- Base url - https://ktor-yoga.onrender.com/

Refer this url for the postman documentation -

- [Yoga API](https://documenter.getpostman.com/view/19052498/2s8YzTViRd)



## Deployment

- The app contains a Dockerfile which can be used to containerize the application and is thus Cloud Native.
- The app is currently deployed on [renderer](https://render.com/) & the database is hosted on AWS.


  Refer to this url for steps to containerize this app using docker -
- [ktor-docker](https://ktor.io/docs/docker.html)



## Troubleshooting

- Note: Since the app is hosted using a free plan of render, the server stops after being idle for some time.
- Desi Jugaad: Make sure to hit the [base end point](https://ktor-yoga.onrender.com/) a couple of times before using the app to wake the server up from sleep and make it up and running!
## My Learnings

- Learnt the complete backend development using Kotlin Ktor Server and KMongo Atlas NoSQL database.
- Explored Docker to make the app cloud native and for ease of deployment.
- Learnt to host the app on a hosting service like renderer.
- Learnt how to use postman for making API calls and to build an attractive API documentation.
- Learnt about Database Schema and ER Diagram.
- Concepts like routing, serialization, hashing, salting

## Author

- [@Lord-Lava](https://github.com/Lord-Lava/)