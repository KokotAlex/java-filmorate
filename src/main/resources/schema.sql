-- Создадим таблицу "genre"
CREATE TABLE IF NOT EXISTS genre (
    genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name VARCHAR(15) NOT NULL
);

-- Создадим таблицу "mpa"
CREATE TABLE IF NOT EXISTS mpa (
    mpa_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    mpa_name VARCHAR (10) NOT NULL,
    description VARCHAR (150)
);

-- Создадим таблицу films
CREATE TABLE IF NOT EXISTS films (
    film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_name varchar(100),
    description varchar(200),
    release_date date DEFAULT '1.1.1',
    duration INTEGER DEFAULT 0,
    mpa_id INTEGER REFERENCES mpa(mpa_id)
);

-- Создадим таблицу "film_genre"
CREATE TABLE IF NOT EXISTS film_genre (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id INTEGER NOT NULL REFERENCES FILMS(FILM_ID) ON DELETE CASCADE,
    genre_id INTEGER NOT NULL REFERENCES GENRE(GENRE_ID) ON DELETE NO ACTION
);

-- Создадим таблицу users
CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email varchar(100),
    login varchar(50),
    user_name varchar(100),
    birthday date DEFAULT '1.1.1'
);

-- создадим таблицу film_likes
CREATE TABLE IF NOT EXISTS film_likes (
    film_id INTEGER NOT NULL REFERENCES films(film_id) ON DELETE CASCADE,
    user_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, user_id)
);

-- создадим таблицу friends
CREATE TABLE IF NOT EXISTS friends (
    user_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE NO ACTION,
    friend_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE NO ACTION,
    approved boolean DEFAULT false,
    PRIMARY KEY (user_id, friend_id)
);