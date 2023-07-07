# String patterns finding application

## What does it do?

* The application finds given patterns in strings, such as AAB pattern in BAAB string (pattern offset 1).
* If no full match is found, the best partial match is returned, with the number of typos differing from full match.

## API specification

* POST /tasks:
  - input - JSON with 3 parameters (string, pattern, delayInMilliseconds (optional, default 2000)). The delay is applied at a stage of pattern finding algorithm, the number of stages is equal to the number of letters in the string.
  - output - task id string, returns HTTP 201 CREATED 
  
* GET /tasks
  - input - query parameters - page number (optional, starting from 1, default 1), page size (optional, default 10)
  - output - JSON list of tasks sorted from the newest one to the oldest one, containing:
    * task id
    * pattern
    * string
    * progress (0-100)
    * patternFound (boolean)
    * patternOffset (starting from 0)
    * numberOfTypos
    * created (date and time)

* GET /task
  - input - query parameter - id - UUID of the task to be retrieved, required. 
  - output - JSON of the task requested (or 404 if it doesn't exist), containing:
    * task id
    * pattern
    * string
    * progress (0-100)
    * patternFound (boolean)
    * patternOffset (starting from 0)
    * numberOfTypos
    * created (date and time)
    
## Algorithm overview

* It's a simple algorithm iterating through all the letters of the string, trying to match the pattern at each letter. The number of matches is stored and used to find the best match. Time complexity of the algorithm is O(n*m), where n is the length of the string and m is the length of the pattern.

## Getting started

* Please install [Docker](https://www.docker.com/)  in order to run the application
* Run `docker-compose up flyway` to create the Postgres database and initialise it with a table.
* Run `.\gradlew build` to generate JOOQ classes and create the jar of the application
* Run `docker-compose up ll-string-patterns` to start the application

## Additional information
* [Flyway](https://flywaydb.org/) was used to initialise the database with the required table
* [JOOQ](https://www.jooq.org/) was used to provide the mapping between the database and Java objects
* [Test Containers](https://java.testcontainers.org/) were used in integration tests
* [Docker](https://www.docker.com/) is used to host a [PostgreSQL](https://www.postgresql.org/) database
* The project was structured according to Domain Driven Design
