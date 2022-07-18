CREATE TABLE IF NOT EXISTS users(
            user_id int PRIMARY KEY AUTO_INCREMENT,
            email varchar(255) NOT NULL,
            login varchar(255) NOT NULL,
            name varchar(255) NOT NULL,
            birthday date
);
CREATE TABLE IF NOT EXISTS mpa(
            mpa_id int PRIMARY KEY AUTO_INCREMENT,
            name varchar(255) UNIQUE
);
CREATE TABLE IF NOT EXISTS films(
            film_id int PRIMARY KEY AUTO_INCREMENT,
            name varchar(255) NOT NULL,
            description varchar(255),
            release_date date,
            duration int,
            mpa_id int references MPA(mpa_id)
);
CREATE TABLE IF NOT EXISTS genres(
            genre_id int PRIMARY KEY AUTO_INCREMENT,
            name varchar(255) UNIQUE
);
CREATE TABLE IF NOT EXISTS film_genres(
            film_id int references FILMS(film_id),
            genre_id int references GENRES(genre_id),
            PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS friend_statuses(
            friend_status_id int PRIMARY KEY AUTO_INCREMENT,
            status varchar(255) UNIQUE
);
CREATE TABLE IF NOT EXISTS film_likes(
            film_id int references FILMS(film_id),
            user_id int references USERS(user_id),
            PRIMARY KEY (film_id, user_id)
);
CREATE TABLE IF NOT EXISTS friends(
            user_id int references USERS(user_id),
            friend_id int references USERS(user_id),
            friend_status_id int references FRIEND_STATUSES(friend_status_id),
            PRIMARY KEY (user_id, friend_id)
);