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
}
