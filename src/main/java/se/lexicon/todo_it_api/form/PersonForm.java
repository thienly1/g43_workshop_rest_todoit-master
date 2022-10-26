package se.lexicon.todo_it_api.form;

import lombok.*;
import se.lexicon.todo_it_api.model.entity.TodoItem;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonForm {

    private String firstName;
    private String lastName;
    private LocalDate birthDate;

}
