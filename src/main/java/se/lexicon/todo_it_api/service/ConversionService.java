package se.lexicon.todo_it_api.service;

import org.springframework.stereotype.Service;
import se.lexicon.todo_it_api.dto.PersonDto;
import se.lexicon.todo_it_api.dto.PersonDtoSmall;
import se.lexicon.todo_it_api.dto.TodoItemDto;
import se.lexicon.todo_it_api.dto.TodoItemDtoSmall;
import se.lexicon.todo_it_api.form.PersonForm;
import se.lexicon.todo_it_api.form.TodoItemForm;
import se.lexicon.todo_it_api.model.entity.Person;
import se.lexicon.todo_it_api.model.entity.TodoItem;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConversionService {

    public Person toPerson(PersonForm personForm){
        return new Person(0, personForm.getFirstName(), personForm.getLastName(),
                personForm.getBirthDate(), new ArrayList<>());
    }
    public TodoItem toTodoItem(TodoItemForm form){
        return new TodoItem(0, form.getTitle(), form.getDescription(), form.getDeadLine(),
                form.isDone(), null);
    }

    public TodoItemDto toTodoItemDto(TodoItem todoItem){
        PersonDtoSmall personDtoSmall= null;
        if(todoItem.getAssignee()!=null){
            Person person= todoItem.getAssignee();
            personDtoSmall= new PersonDtoSmall(person.getPersonId(), person.getFirstName(), person.getLastName(),person.getBirthDate());
        }
        return new TodoItemDto(todoItem.getTodoId(),todoItem.getTitle(),todoItem.getDescription(),todoItem.getDeadLine(), todoItem.isDone(),personDtoSmall);
    }

    public PersonDto toPersonDto(Person person){
        List<TodoItemDtoSmall> list= new ArrayList<>();
        for(TodoItem item: person.getTodoItems()){
            list.add(new TodoItemDtoSmall(item.getTodoId(), item.getTitle(),item.getDescription(), item.getDeadLine(), item.isDone()));
        }
        return new PersonDto(person.getPersonId(), person.getFirstName(), person.getLastName(), person.getBirthDate(),list);
    }

}
