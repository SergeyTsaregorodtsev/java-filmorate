package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.*;

import lombok.Data;
import lombok.NonNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
public class Film {
    private int id;
    @NotBlank
    @NonNull
    private String name;
    @Size(max = 200, message = "Максимальная длина описания - 200 символов.")
    private String description;
    @NonNull
    @PastOrPresent(message = "Дата релиза фильма не может быть в будущем.")
    private LocalDate releaseDate;
    @PositiveOrZero(message = "Продолжительность фильма должна быть положительной.")
    private int duration;
    private Set<Genre> genres = new HashSet<>();
    private Mpa mpa;
}