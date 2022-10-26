package se.lexicon.todo_it_api.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.lexicon.todo_it_api.data.PersonDAO;
import se.lexicon.todo_it_api.data.TodoItemDAO;
import se.lexicon.todo_it_api.dto.PersonDto;
import se.lexicon.todo_it_api.dto.TodoItemDto;
import se.lexicon.todo_it_api.exception.AppResourceNotFoundException;
import se.lexicon.todo_it_api.exception.DuplicateException;
import se.lexicon.todo_it_api.form.TodoItemForm;
import se.lexicon.todo_it_api.model.entity.Person;
import se.lexicon.todo_it_api.model.entity.TodoItem;

import java.time.LocalDate;
import java.util.List;
@Service
public class TodoItemServiceImpl implements TodoItemService{

    private final TodoItemDAO todoItemDAO;
    private final ModelMapper modelMapper;
    private final PersonDAO personDAO;

    @Autowired
    public TodoItemServiceImpl(TodoItemDAO todoItemDAO, ModelMapper modelMapper, PersonDAO personDAO) {
        this.todoItemDAO = todoItemDAO;
        this.modelMapper = modelMapper;
        this.personDAO = personDAO;
    }

    @Override
    public TodoItemDto create(TodoItemForm todoItemForm) throws AppResourceNotFoundException {
        if (todoItemForm == null) throw  new IllegalArgumentException("todoItem form is null");
        TodoItem todoItem = modelMapper.map(todoItemForm, TodoItem.class);
        if (todoItemDAO.findAll().contains(todoItem)) throw new DuplicateException("todoItem is duplicate");
        TodoItem saveTodoItem = todoItemDAO.save(todoItem);
        TodoItemDto dto = modelMapper.map(saveTodoItem,TodoItemDto.class);
        return dto;
    }

    @Override
    public TodoItemDto findById(Integer todoItemId) throws AppResourceNotFoundException {
        if (todoItemId == null) throw  new IllegalArgumentException("todoItem form is null");
        TodoItem found = todoItemDAO.findById(todoItemId).orElseThrow(
                ()-> new AppResourceNotFoundException("Not found")
        );
        TodoItemDto dto = modelMapper.map(found, TodoItemDto.class);
        return dto;
    }

    @Override
    public List<TodoItemDto> findAll() throws AppResourceNotFoundException {
        List<TodoItem> allTodoItem = todoItemDAO.findAll();
        List<TodoItemDto> allDto = modelMapper.map(allTodoItem, new TypeToken<List<TodoItemDto>>(){}.getType());
        return allDto;
    }

    @Override
    public List<TodoItemDto> findByTitle(String title) throws AppResourceNotFoundException {
        if (title == null) throw new IllegalArgumentException("title is null");
        List<TodoItem> allByTitle = todoItemDAO.findByTitleContains(title);
        List<TodoItemDto> allDto = modelMapper.map(allByTitle, new TypeToken<List<TodoItemDto>>(){}.getType());
        return allDto;
    }

    @Override
    public List<TodoItemDto> findAllUnassigned() throws AppResourceNotFoundException {
        List<TodoItem> allUnAssigned = todoItemDAO.findUnassignedTodoItems();
        List<TodoItemDto> allDto = modelMapper.map(allUnAssigned, new TypeToken<List<TodoItemDto>>(){}.getType());
        return allDto;
    }

    @Override
    public List<TodoItemDto> findAllUnFinishAndOverdue() throws AppResourceNotFoundException {
        List<TodoItem> allMaches = todoItemDAO.findAllUnfinishedAndOverdue();
        List<TodoItemDto> allDto = modelMapper.map(allMaches, new TypeToken<List<TodoItemDto>>(){}.getType());
        return allDto;
    }

    @Override
    public List<TodoItemDto> findByPersonId(Integer personId) throws AppResourceNotFoundException {
        if (personId ==null) throw new IllegalArgumentException("personId is null");
        List<TodoItem> findId = todoItemDAO.findByPersonId(personId);
        List<TodoItemDto> allDto = modelMapper.map(findId, new TypeToken<List<TodoItemDto>>(){}.getType());
        return allDto;
    }

    @Override
    public List<TodoItemDto> findByDoneStatus(Boolean status) throws AppResourceNotFoundException {
        List<TodoItem> findByStatus = todoItemDAO.findByDoneStatus(status);
        List<TodoItemDto> allDto = modelMapper.map(findByStatus, new TypeToken<List<TodoItemDto>>(){}.getType());
        return allDto;
    }

    @Override
    public List<TodoItemDto> findByDeadLineBetween(LocalDate start, LocalDate end) throws AppResourceNotFoundException {
        List<TodoItem> findByDate = todoItemDAO.findByDeadlineBetween(start, end);
        List<TodoItemDto> allDto = modelMapper.map(findByDate, new TypeToken<List<TodoItemDto>>(){}.getType());
        return allDto;
    }

    @Override
    public List<TodoItemDto> findByDeadLineBefore(LocalDate end) throws AppResourceNotFoundException {
        List<TodoItem> findByEndDate = todoItemDAO.findByDeadLineBefore(end);
        List<TodoItemDto> allDto = modelMapper.map(findByEndDate, new TypeToken<List<TodoItemDto>>(){}.getType());
        return allDto;
    }

    @Override
    public List<TodoItemDto> findByDeadLineAfter(LocalDate start) throws AppResourceNotFoundException {
        List<TodoItem> findByStartDate = todoItemDAO.findByDeadlineAfter(start);
        List<TodoItemDto> allDto = modelMapper.map(findByStartDate, new TypeToken<List<TodoItemDto>>(){}.getType());
        return allDto;
    }

    @Override
    public TodoItemDto update(Integer todoItemId, TodoItemForm todoItemForm) throws AppResourceNotFoundException {
        if(todoItemId ==null) throw new IllegalArgumentException("TodoItem Id is null");
        if(todoItemForm ==null) throw new IllegalArgumentException("TodoItem Form is null");
        TodoItemDto convert = modelMapper.map(todoItemForm , TodoItemDto.class);
        if(!todoItemId.equals(convert.getTodoItemId())) throw new IllegalArgumentException("Id needs to match");
        TodoItem toEntity = modelMapper.map(convert, TodoItem.class);
        TodoItem updated =todoItemDAO.save(toEntity);
        return modelMapper.map(updated, TodoItemDto.class);
    }

    @Override
    public Boolean delete(Integer todoItemId) throws AppResourceNotFoundException {
        if(todoItemId==null) throw new IllegalArgumentException("todoItemId is null");
        if(!todoItemDAO.existsById(todoItemId)) throw new AppResourceNotFoundException("There is no todoItem with that todoItemId in the database");
        todoItemDAO.deleteById(todoItemId);
        return true;
    }
}
