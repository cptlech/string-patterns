CREATE TABLE TASK
(
    ID uuid PRIMARY KEY,
    STRING  text NOT NULL,
    PATTERN text NOT NULL,
    PROGRESS integer NOT NULL,
    PATTERN_FOUND boolean NOT NULL,
    PATTERN_OFFSET integer,
    NUMBER_OF_TYPOS integer,
    CREATED timestamp
);

