package se.lexicon.todo_it_api.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonDtoSmall {

    private Integer personId;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

}
