package se.lexicon.todo_it_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.lexicon.todo_it_api.dto.TodoItemDto;
import se.lexicon.todo_it_api.form.TodoItemForm;
import se.lexicon.todo_it_api.service.TodoItemService;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequestMapping("/todo/api/v1/todos")
public class TodoItemController {

    private final TodoItemService todoItemService;

    @Autowired
    public TodoItemController(TodoItemService todoItemService) {
        this.todoItemService = todoItemService;
    }

    @PostMapping()
    public ResponseEntity<TodoItemDto> create(@RequestBody TodoItemForm form){
        TodoItemDto todoItemDTO = todoItemService.create(form);
        return ResponseEntity.status(HttpStatus.CREATED).body(todoItemDTO);
    }

    @GetMapping("/title")
    public ResponseEntity<Collection<TodoItemDto>> findByTitle(@RequestParam("title") String title){
        return ResponseEntity.ok(todoItemService.findByTitle(title));
    }

    @GetMapping("/unassigned")
    public ResponseEntity<Collection<TodoItemDto>> findAllUnassigned(){
        return ResponseEntity.ok(todoItemService.findAllUnassigned());
    }

    @GetMapping("/UnFinishAndOverdue")
    public ResponseEntity<Collection<TodoItemDto>> findAllUnFinishAndOverdue(){
        return ResponseEntity.ok(todoItemService.findAllUnFinishAndOverdue());
    }

    @GetMapping("/personId/{personId}")
    public ResponseEntity<Collection<TodoItemDto>> findByPersonId(@PathVariable("personId") Integer personId){
        return ResponseEntity.ok(todoItemService.findByPersonId(personId));
    }

    @GetMapping("/status")
    public ResponseEntity<Collection<TodoItemDto>> findByDoneStatus(@RequestParam("status") String status){
        Boolean statusForSearch= Boolean.parseBoolean(status);
        return ResponseEntity.ok(todoItemService.findByDoneStatus(statusForSearch));
    }
    @GetMapping("/between")
    public ResponseEntity<Collection<TodoItemDto>> findByDeadlineBetween(@RequestParam("start") String start,@RequestParam("end") String end){
        return ResponseEntity.ok(todoItemService.findByDeadLineBetween(LocalDate.parse(start), LocalDate.parse(end)));
    }
    @GetMapping("/endDate")
    public ResponseEntity<Collection<TodoItemDto>> findByDeadlineBefore(@RequestParam("endDate") String endDate){
        return ResponseEntity.ok(todoItemService.findByDeadLineBefore(LocalDate.parse(endDate)));
    }
    @GetMapping("/startDate")
    public ResponseEntity<Collection<TodoItemDto>> findByDeadlineAfter(@RequestParam("startDate") String startDate){
        return ResponseEntity.ok(todoItemService.findByDeadLineAfter(LocalDate.parse(startDate)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoItemDto> findById(@PathVariable("id") Integer id){
        TodoItemDto todoItemDto = todoItemService.findById(id);
        return ResponseEntity.ok(todoItemDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoItemDto> update(@PathVariable("id") Integer id, @RequestBody TodoItemForm form){
        TodoItemDto todoItemDto = todoItemService.update(id, form);
        return ResponseEntity.ok(todoItemDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        boolean deleted = todoItemService.delete(id);
        return ResponseEntity.ok(deleted ? "TodoItem with id " + id + " was deleted" : "TodoItem with id " + id + " was not deleted");
    }
}
