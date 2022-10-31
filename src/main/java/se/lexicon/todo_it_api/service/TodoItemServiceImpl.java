package se.lexicon.todo_it_api.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.todo_it_api.data.PersonDAO;
import se.lexicon.todo_it_api.data.TodoItemDAO;
import se.lexicon.todo_it_api.dto.PersonDto;
import se.lexicon.todo_it_api.dto.PersonDtoSmall;
import se.lexicon.todo_it_api.dto.TodoItemDto;
import se.lexicon.todo_it_api.exception.AppResourceNotFoundException;
import se.lexicon.todo_it_api.exception.DuplicateException;
import se.lexicon.todo_it_api.form.TodoItemForm;
import se.lexicon.todo_it_api.model.entity.Person;
import se.lexicon.todo_it_api.model.entity.TodoItem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoItemServiceImpl implements TodoItemService{

    private final TodoItemDAO todoItemDAO;
    private final ConversionService conversion;

    @Autowired
    public TodoItemServiceImpl(TodoItemDAO todoItemDAO, ConversionService conversion) {
        this.todoItemDAO = todoItemDAO;
        this.conversion = conversion;
    }

    @Override
    @Transactional
    public TodoItemDto create(TodoItemForm todoItemForm) {
        if (todoItemForm == null) throw  new IllegalArgumentException("todoItem form is null");
        TodoItem todoItem = conversion.toTodoItem(todoItemForm);
        if (todoItemDAO.findAll().contains(todoItem)) throw new DuplicateException("todoItem is duplicate");
        TodoItem saveTodoItem = todoItemDAO.save(todoItem);
        return conversion.toTodoItemDto(saveTodoItem);
    }

    @Override
    @Transactional(readOnly = true)
    public TodoItemDto findById(Integer todoItemId) {
        if (todoItemId == null) throw  new IllegalArgumentException("todoItem form is null");
        TodoItem found = todoItemDAO.findById(todoItemId).orElseThrow(
                ()-> new AppResourceNotFoundException("Not found")
        );
        return conversion.toTodoItemDto(found);
    }

    @Override
    public List<TodoItemDto> findAll(){
        List<TodoItem> allTodoItem = todoItemDAO.findAll();
        return allTodoItem.stream().map(conversion::toTodoItemDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TodoItemDto> findByTitle(String title){
        if (title == null) throw new IllegalArgumentException("title is null");
        List<TodoItem> allByTitle = todoItemDAO.findByTitleContains(title);
        return allByTitle.stream().map(conversion::toTodoItemDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TodoItemDto> findAllUnassigned() {
        List<TodoItem> allUnAssigned = todoItemDAO.findUnassignedTodoItems();
        return allUnAssigned.stream().map(conversion::toTodoItemDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TodoItemDto> findAllUnFinishAndOverdue() {
        List<TodoItem> allMatches = todoItemDAO.findAllUnfinishedAndOverdue();
        return allMatches.stream().map(conversion::toTodoItemDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TodoItemDto> findByPersonId(Integer personId) {
        if (personId ==null) throw new IllegalArgumentException("personId is null");
        List<TodoItem> findId = todoItemDAO.findByPersonId(personId);
        return findId.stream().map(conversion::toTodoItemDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TodoItemDto> findByDoneStatus(boolean status) {
        List<TodoItem> findByStatus = todoItemDAO.findByDoneStatus(status);
        return findByStatus.stream().map(conversion::toTodoItemDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TodoItemDto> findByDeadLineBetween(LocalDate start, LocalDate end) {
        List<TodoItem> findByDate = todoItemDAO.findByDeadlineBetween(start, end);
        return findByDate.stream().map(conversion::toTodoItemDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TodoItemDto> findByDeadLineBefore(LocalDate end) {
        List<TodoItem> findByEndDate = todoItemDAO.findByDeadLineBefore(end);
        return findByEndDate.stream().map(conversion::toTodoItemDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TodoItemDto> findByDeadLineAfter(LocalDate start) {
        List<TodoItem> findByStartDate = todoItemDAO.findByDeadlineAfter(start);
        return findByStartDate.stream().map(todoItem -> conversion.toTodoItemDto(todoItem)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TodoItemDto update(Integer todoItemId, TodoItemForm todoItemForm) {
        if(todoItemId ==null) throw new IllegalArgumentException("TodoItem Id is null");
        if(todoItemForm ==null) throw new IllegalArgumentException("TodoItem Form is null");
        TodoItem needToUpdate = todoItemDAO.findById(todoItemId).orElseThrow(()->new IllegalArgumentException("Id needs to match"));
        needToUpdate.setTitle(todoItemForm.getTitle());
        needToUpdate.setDescription(todoItemForm.getDescription());
        needToUpdate.setDeadLine(todoItemForm.getDeadLine());
        needToUpdate.setDone(todoItemForm.isDone());
        return conversion.toTodoItemDto(needToUpdate);
    }

    @Override
    @Transactional
    public Boolean delete(Integer todoItemId) {
        if(todoItemId==null) throw new IllegalArgumentException("todoItemId is null");
        if(!todoItemDAO.existsById(todoItemId)) throw new AppResourceNotFoundException("There is no todoItem with that todoItemId in the database");
        todoItemDAO.deleteById(todoItemId);
        return true;
    }
}
