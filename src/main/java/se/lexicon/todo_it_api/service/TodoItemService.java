package se.lexicon.todo_it_api.service;

import se.lexicon.todo_it_api.dto.TodoItemDto;
import se.lexicon.todo_it_api.exception.AppResourceNotFoundException;
import se.lexicon.todo_it_api.form.TodoItemForm;

import java.time.LocalDate;
import java.util.List;

public interface TodoItemService {

    TodoItemDto create (TodoItemForm todoItemForm) throws AppResourceNotFoundException;
    TodoItemDto findById (Integer todoItemId) throws AppResourceNotFoundException;
    List<TodoItemDto> findAll() throws AppResourceNotFoundException;
    List<TodoItemDto> findByTitle(String title) throws AppResourceNotFoundException;
    List<TodoItemDto> findAllUnassigned() throws AppResourceNotFoundException;
    List<TodoItemDto> findAllUnFinishAndOverdue() throws AppResourceNotFoundException;
    List<TodoItemDto> findByPersonId(Integer personId) throws AppResourceNotFoundException;
    List<TodoItemDto> findByDoneStatus(boolean status) throws AppResourceNotFoundException;
    List<TodoItemDto> findByDeadLineBetween(LocalDate start, LocalDate end) throws AppResourceNotFoundException;
    List<TodoItemDto> findByDeadLineBefore(LocalDate end) throws AppResourceNotFoundException;
    List<TodoItemDto> findByDeadLineAfter(LocalDate start) throws AppResourceNotFoundException;
    TodoItemDto update(Integer todoItemId, TodoItemForm todoItemForm) throws AppResourceNotFoundException;
    Boolean delete(Integer todoItemId) throws AppResourceNotFoundException;


}
