CREATE TABLE IF NOT EXISTS events(
    id varchar(36) PRIMARY KEY,
    title text NOT NULL,
    description text NOT NULL
);

CREATE TABLE IF NOT EXISTS users(
    id varchar(36) PRIMARY KEY,
    name text NOT NULL,
    age integer NOT NULL
);

CREATE TABLE IF NOT EXISTS user_events(
    user_id varchar(36) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    event_id varchar(36) NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, event_id)
);