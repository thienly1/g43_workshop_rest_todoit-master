package se.lexicon.todo_it_api.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.lexicon.todo_it_api.dto.TodoItemDto;
import se.lexicon.todo_it_api.form.TodoItemForm;
import se.lexicon.todo_it_api.model.entity.TodoItem;
import se.lexicon.todo_it_api.service.TodoItemService;

import java.util.Collection;

@RestController
@RequestMapping("/todo/api/v1/todoItems")
public class TodoItemController {

    private final TodoItemService todoItemService;
    private final ModelMapper modelMapper;

    @Autowired
    public TodoItemController(TodoItemService todoItemService, ModelMapper modelMapper) {
        this.todoItemService = todoItemService;
        this.modelMapper = modelMapper;
    }


    @PostMapping
    public ResponseEntity<TodoItemDto> createTodoItem(@RequestBody TodoItemForm todoItemForm){
        return ResponseEntity.status(HttpStatus.CREATED).body(todoItemService.create(todoItemForm));

    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoItemDto> findById(@PathVariable("id") Integer todoItemId){
        TodoItemDto found = todoItemService.findById(todoItemId);
        return ResponseEntity.ok(found);
    }

//    @GetMapping("/list")
//    public ResponseEntity<Collection<TodoItemDto>> find(String title, String[] assignee){
//
//    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoItemDto> update(@PathVariable("id") Integer todoItemId, @RequestBody TodoItemForm todoItemForm){
        TodoItemDto update = todoItemService.update(todoItemId,todoItemForm);
        return ResponseEntity.ok(update);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteTodoItem(@PathVariable("id") Integer todoItemId){
        todoItemService.delete(todoItemId);
        return ResponseEntity.status(HttpStatus.valueOf("TodoItem deleted")).build();

    }
}
