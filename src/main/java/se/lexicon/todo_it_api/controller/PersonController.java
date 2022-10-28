package se.lexicon.todo_it_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.lexicon.todo_it_api.dto.PersonDto;
import se.lexicon.todo_it_api.dto.TodoItemDto;
import se.lexicon.todo_it_api.form.PersonForm;


import se.lexicon.todo_it_api.service.PersonService;
import se.lexicon.todo_it_api.service.TodoItemService;
import java.util.List;

@RestController
@RequestMapping("/todo/api/v1/people")
public class PersonController {

    private final PersonService personService;
    private final TodoItemService todoItemService;

    @Autowired
    public PersonController(PersonService personService, TodoItemService todoItemService) {
        this.personService = personService;
        this.todoItemService = todoItemService;
    }
    @PostMapping
    public ResponseEntity<PersonDto> createPerson(@RequestBody PersonForm personForm){
        PersonDto personDto = personService.create(personForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(personDto);
    }

    @GetMapping("/{personId}")
    public ResponseEntity<PersonDto> findById(@PathVariable("personId") Integer personId){
        return ResponseEntity.ok(personService.findById(personId));
    }
    @GetMapping("/idle")
    public ResponseEntity<List<PersonDto>> findIdlePeople(){
        List<PersonDto> foundedList = personService.findIdlePeople();
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
        return ResponseEntity.ok(todoItemService.findByPersonId(personId));
    }

    @PutMapping("/add/{id}")
    public  ResponseEntity<PersonDto> addTodoItems(@PathVariable("id") Integer personId, @RequestParam("todoItemId") Integer todoItemId){
        return ResponseEntity.ok(personService.addTodoItem(personId, todoItemId));
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<PersonDto> removeTodoItem(@PathVariable("id") Integer personId,@RequestParam("TodoItemId") Integer todoItemId){
        return ResponseEntity.ok(personService.removeTodoItem(personId,todoItemId));
    }

}
