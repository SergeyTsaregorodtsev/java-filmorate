package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

@Data
public class User {
    private int id;
    @Email
    @NotBlank(message = "Электронная почта не может быть пустой.")
    private final String email;
    @NotBlank @Pattern(regexp = "^\\S*$")   // Любое количество непробельных символов
    private final String login;
    @NonNull
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем.")
    private LocalDate birthday;
    private final Set<Integer> friends = new HashSet<>();
}
