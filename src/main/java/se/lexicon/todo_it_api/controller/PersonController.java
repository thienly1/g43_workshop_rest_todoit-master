package se.lexicon.todo_it_api.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.lexicon.todo_it_api.dto.PersonDto;
import se.lexicon.todo_it_api.dto.TodoItemDto;
import se.lexicon.todo_it_api.form.PersonForm;
import se.lexicon.todo_it_api.model.entity.TodoItem;
import se.lexicon.todo_it_api.service.PersonService;
import se.lexicon.todo_it_api.service.TodoItemService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("todo/api/v1/people")
public class PersonController {

    private final PersonService personService;
    private final ModelMapper modelMapper;
    private final TodoItemService todoItemService;

    @Autowired
    public PersonController(PersonService personService, ModelMapper modelMapper, TodoItemService todoItemService) {
        this.personService = personService;
        this.modelMapper = modelMapper;
        this.todoItemService = todoItemService;
    }

    @PostMapping
    public ResponseEntity<PersonDto> createPerson(@RequestBody PersonForm personForm){
        PersonDto personDto = personService.create(personForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(personDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> findByID(@PathVariable("id") Integer personId){
        PersonDto found= personService.findById(personId);
        return ResponseEntity.ok(found);
    }
    @GetMapping("/search")
    public ResponseEntity<Collection<PersonDto>> find(@RequestParam("firstName") String firstName){
        Collection<PersonDto> foundedList= personService.findIdlePeople();
        return ResponseEntity.ok(foundedList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> update(@PathVariable("id") Integer personId,@RequestBody PersonForm personForm){

        PersonDto personDto= personService.update(personId, personForm);
        return ResponseEntity.ok(personDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable("id") Integer personId){
        personService.delete(personId);
        String message= "successfully deleted";
        return ResponseEntity.status(HttpStatus.valueOf(message)).build();
    }

    @GetMapping("/todoItems/{id}")
    public ResponseEntity<List<TodoItemDto>> getTodoItems(@PathVariable("id") Integer personId){
        PersonDto foundPerson= personService.findById(personId);
        List<TodoItem> todoItems= foundPerson.getTodoItems();
        List<TodoItemDto> todoItemDtos= modelMapper.map(todoItems, new TypeToken<List<TodoItemDto>>(){}.getType());
        return ResponseEntity.ok(todoItemDtos);
    }

    @PutMapping("/add/{id}")
    public  ResponseEntity<PersonDto> addTodoItems(@PathVariable("id") Integer personId, @RequestParam("todoItemId") Integer todoItemId){
        PersonDto personDto= personService.findById(personId);
        TodoItem todoItem= modelMapper.map(todoItemService.findById(todoItemId), TodoItem.class);
        personDto.getTodoItems().add(todoItem);
        return ResponseEntity.ok(personDto);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<PersonDto> removeTodoItem(@PathVariable("id") Integer personId,@RequestParam("TodoItemId") Integer todoItemId){
        PersonDto personDto= personService.findById(personId);
        TodoItem todoItem= modelMapper.map(todoItemService.findById(todoItemId), TodoItem.class);
        personDto.getTodoItems().remove(todoItem);
        return ResponseEntity.ok(personDto);
    }



}
