package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.*;
import javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Film {
    private int id;
    @NotBlank
    @NotNull
    private String name;
    @Size(max = 200, message = "Максимальная длина описания - 200 символов.")
    private String description;
    @NotNull
    @PastOrPresent(message = "Дата релиза фильма не может быть в будущем.")
    private LocalDate releaseDate;
    @PositiveOrZero(message = "Продолжительность фильма должна быть положительной.")
    private int duration;
    private Set<Genre> genres = new HashSet<>();
    private Mpa mpa;
}