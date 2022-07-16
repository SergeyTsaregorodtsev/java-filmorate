package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor (onConstructor_ = @Autowired)
class FilmorateApplicationTests {
	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;
	private final JdbcTemplate jdbcTemplate;

	@BeforeEach
	public void addData() {
		User user1 = new User(0,"1@mail.ru", "1", "name1", LocalDate.now(), null);
		userStorage.add(user1);
		User user2 = new User(0,"2@mail.ru", "2", "name2",LocalDate.now(), null);
		userStorage.add(user2);
		User user3 = new User(0,"3@mail.ru", "3", "name3",LocalDate.now(), null);
		userStorage.add(user3);

		LocalDate releaseDate = LocalDate.now();
		Film film1 = new Film(1,"name1", "film1Desc.", releaseDate, 100,null,null);
		film1.setMpa(new Mpa(1,"Комедия"));
		filmStorage.add(film1);
		Film film2 = new Film(2,"name2", "film2Desc.", releaseDate,100, null,null);
		film2.setMpa(new Mpa(2,"Драма"));
		filmStorage.add(film2);

		userStorage.addFriend(1,2);
		userStorage.addFriend(3,2);
	}

	@AfterEach
	public void dropData() {
		String[] sqlQuery = {
				"DELETE FROM film_likes",
				"DELETE FROM friends",
				"DELETE FROM films",
				"ALTER TABLE films ALTER COLUMN film_id RESTART WITH 1",
				"DELETE FROM users",
				"ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1",
				};
		for (String query : sqlQuery) {
			jdbcTemplate.update(query);
		}
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void testAddUser() {
		User user4 = new User(0,"4@mail.ru", "4", "name4",LocalDate.now(), null);
		userStorage.add(user4);
		User newUser = userStorage.getById(4);
		assertNotNull(newUser);
		assertEquals(4, newUser.getId());
		assertEquals("4@mail.ru", newUser.getEmail());
	}

	@Test
	public void testRemoveUser() {
		userStorage.remove(1);
		UserNotFoundException e = assertThrows(UserNotFoundException.class,
				new Executable() {
					@Override
					public void execute() throws IOException, InterruptedException {
						User deletedUser = userStorage.getById(1);
					}
				});
		assertEquals("Пользователь ID 1 не найден.", e.getMessage());
	}

	@Test
	public void testGetAllUsers() {
		List<User> users = userStorage.getAll();
		assertNotNull(users);
		assertEquals(3, users.size());
		assertEquals("1@mail.ru", users.get(0).getEmail());
	}

	@Test
	public void testUpdateUser() {
		User updatedUser = new User(2,"2@mail.ru", "2", "name2new",LocalDate.now(), null);
		userStorage.update(updatedUser);
		User user = userStorage.getById(2);
		assertNotNull(user);
		assertEquals("name2new", user.getName());
	}

	@Test
	public void testGetUserById() {
		User user = userStorage.getById(1);
		assertNotNull(user);
		assertEquals(1, user.getId());
		assertEquals("1@mail.ru", user.getEmail());
	}

	@Test
	public void testFindUserByBadId() {
		UserNotFoundException e = assertThrows(UserNotFoundException.class,
				new Executable() {
					@Override
					public void execute() throws IOException, InterruptedException {
						User user = userStorage.getById(4);;
					}
				});
		assertEquals("Пользователь ID 4 не найден.", e.getMessage());
	}

	@Test
	public void testAddFilm() {
		Film film3 = new Film(3,"name3", "film3Desc.", LocalDate.now(),100, null,null);
		film3.setMpa(new Mpa(2,"Драма"));
		filmStorage.add(film3);
		Film newFilm = filmStorage.getById(3);
		assertNotNull(newFilm);
		assertEquals(3, newFilm.getId());
	}

	@Test
	public void testRemoveFilm() {
		filmStorage.remove(1);
		FilmNotFoundException e = assertThrows(FilmNotFoundException.class,
				new Executable() {
					@Override
					public void execute() throws IOException, InterruptedException {
						Film deletedFilm = filmStorage.getById(1);
					}
				});
		assertEquals("Фильм ID 1 не найден.", e.getMessage());
	}

	@Test
	public void testGetFilmById() {
		Film film = filmStorage.getById(1);
		assertNotNull(film);
		assertEquals(1, film.getId());
		assertEquals("film1Desc.", film.getDescription());
	}

	@Test
	public void testGetFilmByBadId() {
		FilmNotFoundException e = assertThrows(FilmNotFoundException.class,
				new Executable() {
					@Override
					public void execute() throws IOException, InterruptedException {
						Film film = filmStorage.getById(-1);;
					}
				});
		assertEquals("Фильм ID -1 не найден.", e.getMessage());
	}

	@Test
	public void testUpdateFilm() {
		LocalDate releaseDate = LocalDate.now();
		Film updatedFilm = new Film(1,"name1upd", "description",releaseDate, 100, null,null);
		updatedFilm.setMpa(new Mpa(1,"Комедия"));
		filmStorage.update(updatedFilm);
		Film film = filmStorage.getById(1);
		assertNotNull(film);
		assertEquals("name1upd", film.getName());
	}

	@Test
	public void testGetAllFilms() {
		List<Film> films = filmStorage.getAll();
		assertNotNull(films);
		assertEquals(2, films.size());
		assertEquals("film2Desc.", films.get(1).getDescription());
	}

	@Test
	public void testAddAndGetFriends() {
		userStorage.addFriend(3,1);
		User user = userStorage.getById(3);
		List<Integer> friends = user.getFriends();
		assertNotNull(friends);
		assertEquals(2, friends.size());
	}

	@Test
	public void testRemoveFriend() {
		userStorage.removeFriend(1,2);
		User user = userStorage.getById(1);
		List<User> friends = userStorage.getFriends(1);
		assertEquals(0, friends.size());
	}

	@Test
	public void testGetCommonFriends() {
		List<User> friends = userStorage.getCommonFriends(1,3);
		assertNotNull(friends);
		assertEquals(1, friends.size());
		assertEquals(2, friends.get(0).getId());
	}

	@Test
	public void testGetRemoveLikesAndFavorite() {
		List<Film> favourites;
		filmStorage.addLike(1,1);
		favourites = filmStorage.getFavorite(2);
		assertEquals(1, favourites.get(0).getId());

		filmStorage.addLike(2,1);
		filmStorage.addLike(2,2);
		favourites = filmStorage.getFavorite(2);
		assertEquals(2, favourites.get(0).getId());

		filmStorage.removeLike(2,1);
		filmStorage.removeLike(2,2);
		favourites = filmStorage.getFavorite(1);
		assertEquals(1, favourites.get(0).getId());
	}

	@Test
	public void testGetMpaAndGetAllMpa() {
		List<Mpa> mpas = filmStorage.getAllMpa();
		assertEquals(5, mpas.size());
		assertEquals("NC-17", mpas.get(4).getName());

		Mpa mpa = filmStorage.getMpa(5);
		assertEquals("NC-17", mpa.getName());
	}

	@Test
	public void testGetGenreAndGetAllGenres() {
		List<Genre> genres = filmStorage.getAllGenres();
		assertEquals(6, genres.size());
		assertEquals("Боевик", genres.get(5).getName());

		Genre genre = filmStorage.getGenre(6);
		assertEquals("Боевик", genre.getName());
	}
}
