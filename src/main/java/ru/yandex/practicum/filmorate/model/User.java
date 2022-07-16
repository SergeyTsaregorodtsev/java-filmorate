package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.*;
import javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private int id;
    @Email
    @NotBlank(message = "Электронная почта не может быть пустой.")
    private final String email;
    @NotBlank @Pattern(regexp = "^\\S*$")   // Любое количество непробельных символов
    private final String login;
    @NotNull
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем.")
    private LocalDate birthday;
    private List<Integer> friends = new ArrayList<>();
}
