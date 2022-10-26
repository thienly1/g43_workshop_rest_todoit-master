package se.lexicon.todo_it_api.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.lexicon.todo_it_api.data.PersonDAO;
import se.lexicon.todo_it_api.data.TodoItemDAO;
import se.lexicon.todo_it_api.dto.PersonDto;
import se.lexicon.todo_it_api.exception.AppResourceNotFoundException;
import se.lexicon.todo_it_api.exception.DuplicateException;
import se.lexicon.todo_it_api.form.PersonForm;
import se.lexicon.todo_it_api.model.entity.Person;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    private final ModelMapper modelMapper;
    private final PersonDAO personDAO;
    private final TodoItemDAO todoItemDAO;

    @Autowired
    public PersonServiceImpl(ModelMapper modelMapper, PersonDAO personDAO, TodoItemDAO todoItemDAO) {
        this.modelMapper = modelMapper;
        this.personDAO = personDAO;
        this.todoItemDAO = todoItemDAO;
    }

    @Override
    public PersonDto create(PersonForm personForm) {
        if(personForm==null) throw new IllegalArgumentException("personForm is null");
        Person person= modelMapper.map(personForm, Person.class);
        if(personDAO.findAll().contains(person)) throw new DuplicateException("personForm has been existed in database");
        Person savedPerson= personDAO.save(person);
        return modelMapper.map(savedPerson,PersonDto.class);
    }

    @Override
    public PersonDto findById(Integer personId) {
        if(personId==null) throw new IllegalArgumentException("person Id is null");
        Person found = personDAO.findById(personId).orElseThrow(()-> new AppResourceNotFoundException("not found"));
        return modelMapper.map(found,PersonDto.class);
    }

    @Override
    public List<PersonDto> findAll() {
        return modelMapper.map(personDAO.findAll(), new TypeToken<List<PersonDto>>(){}.getType());
    }

    @Override
    public List<PersonDto> findIdlePeople() {
        return modelMapper.map(personDAO.findIdlePeople(), new TypeToken<List<PersonDto>>(){}.getType());
    }

    @Override
    public PersonDto update(Integer personId, PersonForm personForm) {
        if(personId==null) throw new IllegalArgumentException("person Id is null");
        if(personForm==null) throw new IllegalArgumentException("personForm is null");
        PersonDto convert = modelMapper.map(personForm, PersonDto.class);
        if(!personId.equals(convert.getPersonId())) throw new IllegalArgumentException("Id needs to match");
        Person toEntity = modelMapper.map(convert, Person.class);
        Person updated = personDAO.save(toEntity);
        return modelMapper.map(updated, PersonDto.class);
    }

    @Override
    public boolean delete(Integer personId) {
        if(personId==null) throw new IllegalArgumentException("personId is null");
        if(!personDAO.existsById(personId)) throw new AppResourceNotFoundException("There is no person with that personId in the database");
        personDAO.deleteById(personId);
        return true;
    }

    @Override
    public PersonDto addTodoItem(Integer personId, Integer todoItemId) {
        if(personId==null) throw new IllegalArgumentException("personId is null");
        if(todoItemId==null) throw new IllegalArgumentException("todoItemId is null");
        if(!personDAO.existsById(personId)) throw new AppResourceNotFoundException("There is no person with that personId in the database");
        if(!todoItemDAO.existsById(todoItemId)) throw new AppResourceNotFoundException("There is no todoItem with that TodoItemId in the database");
        Person found= personDAO.findById(personId).get();
        found.addTodoItem(todoItemDAO.findById(todoItemId).get());
        return modelMapper.map(found, PersonDto.class);
    }

    @Override
    public PersonDto removeTodoItem(Integer personId, Integer todoItemId) {
        if(personId==null) throw new IllegalArgumentException("personId was null");
        if(todoItemId==null) throw new IllegalArgumentException("todoItemId is null");
        if(!personDAO.existsById(personId)) throw new AppResourceNotFoundException("There is no person with that personId in the database");
        if(!todoItemDAO.existsById(todoItemId)) throw new AppResourceNotFoundException("There is no todoItem with that TodoItemId in the database");
        Person found=personDAO.findById(personId).get();
        found.removeTodoItem(todoItemDAO.findById(todoItemId).get());
        return modelMapper.map(found, PersonDto.class);
    }
}
