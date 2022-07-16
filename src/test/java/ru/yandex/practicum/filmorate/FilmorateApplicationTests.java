package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor (onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmorateApplicationTests {
	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;

	@Test @Order(1)
	void contextLoads() {
	}

	@Test @Order(2)
	public void testAddUser() {
		User user1 = new User(1,"1@mail.ru", "1", "name1", LocalDate.now(), null);
		userStorage.add(user1);
		User user2 = new User(2,"2@mail.ru", "2", "name2",LocalDate.now(), null);
		userStorage.add(user2);
		User user3 = new User(3,"3@mail.ru", "3", "name3",LocalDate.now(), null);
		userStorage.add(user3);
		User newUser = userStorage.getById(3);
		assertNotNull(newUser);
		assertEquals(3, newUser.getId());
		assertEquals("3@mail.ru", newUser.getEmail());
	}

	@Test @Order(3)
	public void testGetAllUsers() {
		List<User> users = userStorage.getAll();
		assertNotNull(users);
		assertEquals(3, users.size());
		assertEquals("2@mail.ru", users.get(1).getEmail());
	}

	@Test @Order(4)
	public void testUpdateUser() {
		User updatedUser = new User(2,"2@mail.ru", "2", "name2new",LocalDate.now(), null);
		userStorage.update(updatedUser);
		User user = userStorage.getById(2);
		assertNotNull(user);
		assertEquals("name2new", user.getName());
	}

	@Test @Order(5)
	public void testAddFriend() {
		userStorage.addFriend(1,2);
		User user = userStorage.getById(1);
		List<Integer> friends = user.getFriends();
		assertNotNull(friends);
		assertEquals(1, friends.size());
		assertEquals(2, friends.get(0));
	}

	@Test @Order(6)
	public void testFindUserById() {
		User user = userStorage.getById(1);
		assertNotNull(user);
		assertEquals(1, user.getId());
		assertEquals("1@mail.ru", user.getEmail());
	}

	@Test @Order(7)
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

	@Test @Order(8)
	public void testGetFriends() {
		userStorage.addFriend(1,3);
		List<User> friends = userStorage.getFriends(1);
		assertNotNull(friends);
		assertEquals(2, friends.size());
		assertEquals(3, friends.get(1).getId());
	}

	@Test @Order(9)
	public void testGetCommonFriends() {
		userStorage.addFriend(2,3);
		List<User> friends = userStorage.getCommonFriends(1,2);
		assertNotNull(friends);
		assertEquals(1, friends.size());
		assertEquals(3, friends.get(0).getId());
	}

	@Test @Order(10)
	public void testRemoveFriend() {
		userStorage.removeFriend(1,2);
		User user = userStorage.getById(1);
		List<User> friends = userStorage.getFriends(1);
		assertEquals(1, friends.size());
	}

	@Test @Order(11)
	public void testAddFilm() {
		LocalDate releaseDate = LocalDate.now();
		Film film1 = new Film(1,"name1", "film1Desc.", releaseDate, 100,null,null);
		film1.setMpa(new Mpa(1,"Комедия"));
		filmStorage.add(film1);
		Film film2 = new Film(2,"name2", "film2Desc.", releaseDate,100, null,null);
		film2.setMpa(new Mpa(2,"Драма"));
		filmStorage.add(film2);
		Film newFilm = filmStorage.getById(2);
		assertNotNull(newFilm);
		assertEquals(2, newFilm.getId());
		assertEquals("name2", newFilm.getName());
	}

	@Test @Order(12)
	public void testGetAll() {
		List<Film> films = filmStorage.getAll();
		assertNotNull(films);
		assertEquals(2, films.size());
		assertEquals("film2Desc.", films.get(1).getDescription());
	}
	@Test @Order(13)
	public void testGetById() {
		Film film = filmStorage.getById(1);
		assertNotNull(film);
		assertEquals(1, film.getId());
		assertEquals("film1Desc.", film.getDescription());
	}

	@Test @Order(14)
	public void testGetByBadId() {
		FilmNotFoundException e = assertThrows(FilmNotFoundException.class,
				new Executable() {
					@Override
					public void execute() throws IOException, InterruptedException {
						Film film = filmStorage.getById(-1);;
					}
				});
		assertEquals("Фильм ID -1 не найден.", e.getMessage());
	}

	@Test @Order(15)
	public void testUpdate() {
		LocalDate releaseDate = LocalDate.now();
		Film updatedFilm = new Film(1,"name1upd", "description",releaseDate, 100, null,null);
		updatedFilm.setMpa(new Mpa(1,"Комедия"));
		filmStorage.update(updatedFilm);
		Film film = filmStorage.getById(1);
		assertNotNull(film);
		assertEquals("name1upd", film.getName());
	}

	@Test @Order(16)
	public void testGetRemoveLikesAndFavorite() {
		List<Film> favourites = new ArrayList<>();
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

	@Test @Order(17)
	public void testGetMpaAndGetAllMpa() {
		List<Mpa> mpas = filmStorage.getAllMpa();
		assertEquals(5, mpas.size());
		assertEquals("NC-17", mpas.get(4).getName());

		Mpa mpa = filmStorage.getMpa(5);
		assertEquals("NC-17", mpa.getName());
	}

	@Test @Order(18)
	public void testGetGenreAndGetAllGenres() {
		List<Genre> genres = filmStorage.getAllGenres();
		assertEquals(6, genres.size());
		assertEquals("Боевик", genres.get(5).getName());

		Genre genre = filmStorage.getGenre(6);
		assertEquals("Боевик", genre.getName());
	}
}
