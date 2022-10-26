package se.lexicon.todo_it_api.form;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TodoItemForm {
    private String title;
    private String description;
    private LocalDate deadLine;
    private boolean done;

}
