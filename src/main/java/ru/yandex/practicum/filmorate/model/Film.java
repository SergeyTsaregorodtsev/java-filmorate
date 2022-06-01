package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
public class Film {
    private int id;
    @NotBlank
    @NonNull
    private String name;
    private String description;
    @NonNull
    private LocalDate releaseDate;
    private int duration;

    private void fillEmptyFields() {
        if (description == null) {
            description = "";
        }
    }

    public void validate() throws FilmValidationException {
        fillEmptyFields();
        if (name.isBlank()) {
            throw new FilmValidationException("Название фильма не может быть пустым.");
        }
        if (description.length() > 200) {
            throw new FilmValidationException("Максимальная длина описания - 200 символов.");
        }
        if (releaseDate.isBefore(LocalDate.of(1895,12,28))) {
            throw new FilmValidationException("Дата релиза - не раньше 28 декабря 1895 года.");
        }
        if (duration <= 0) {
            throw new FilmValidationException("Продолжительность фильма должна быть положительной.");
        }
    }

    public static class FilmValidationException extends IllegalArgumentException{
        public FilmValidationException(String message){
            super(message);
        }
    }

    public static class FilmExistException extends IllegalArgumentException{
        public FilmExistException(String message){
            super(message);
        }
    }
}
