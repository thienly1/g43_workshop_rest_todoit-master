package se.lexicon.todo_it_api.dto;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TodoItemDtoSmall {
    private Integer todoItemId;
    private String title;
    private String description;
    private LocalDate deadLine;
    private boolean done;
}
