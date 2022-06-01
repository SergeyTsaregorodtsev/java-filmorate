package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class User {
    private int id;
    //@Email
    private final String email;
    @NotBlank
    private final String login;
    @NonNull
    private String name;
    private LocalDate birthday;

    private void fillEmptyFields() {
        if (name.isBlank()) {
            name = login;
        }
    }

    public void validate() throws UserValidationException {
        fillEmptyFields();
        if (email.isBlank() || !email.contains("@")) {
            throw new UserValidationException("Электронная почта не может быть пустой и должна содержать символ @.");
        }
        if (login.isBlank() || login.contains(" ")) {
            throw new UserValidationException("Логин не может быть пустым или содержать пробелы.");
        }
        if (birthday != null && birthday.isAfter(LocalDate.now())) {
            throw new UserValidationException("Дата рождения не может быть в будущем.");
        }
    }

    public static class UserValidationException extends IllegalArgumentException{
        public UserValidationException(String message){
            super(message);
        }
    }

    public static class UserExistException extends IllegalArgumentException{
        public UserExistException(String message){
            super(message);
        }
    }
}
