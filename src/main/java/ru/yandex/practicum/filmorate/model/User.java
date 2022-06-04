package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;

@Data
public class User {
    private int id;
    @Email
    @NotBlank(message = "Электронная почта не может быть пустой.")
    private final String email;
    @NotBlank
    private final String login;
    @NonNull
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем.")
    private LocalDate birthday;
}
