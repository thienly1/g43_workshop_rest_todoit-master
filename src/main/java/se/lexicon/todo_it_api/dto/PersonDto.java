package se.lexicon.todo_it_api.dto;

import lombok.*;
import se.lexicon.todo_it_api.model.entity.TodoItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class PersonDto {

    private Integer personId;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private List<TodoItem> todoItems;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonDto)) return false;
        PersonDto personDto = (PersonDto) o;
        return Objects.equals(getFirstName(), personDto.getFirstName()) && Objects.equals(getLastName(), personDto.getLastName()) && Objects.equals(getBirthDate(), personDto.getBirthDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getBirthDate());
    }
}
