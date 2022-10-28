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
import se.lexicon.todo_it_api.dto.TodoItemDtoSmall;
import se.lexicon.todo_it_api.exception.AppResourceNotFoundException;
import se.lexicon.todo_it_api.exception.DuplicateException;
import se.lexicon.todo_it_api.form.PersonForm;
import se.lexicon.todo_it_api.model.entity.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {
    private final PersonDAO personDAO;
    private final TodoItemService todoItemService;
    private final ConversionService conversion;
    private final TodoItemDAO todoItemDAO;

    @Autowired
    public PersonServiceImpl(PersonDAO personDAO, TodoItemService todoItemService, ConversionService conversion, TodoItemDAO todoItemDAO) {
        this.personDAO = personDAO;
        this.todoItemService = todoItemService;
        this.conversion = conversion;
        this.todoItemDAO = todoItemDAO;
    }

    @Override
    public PersonDto create(PersonForm personForm) {
        if(personForm==null) throw new IllegalArgumentException("personForm is null");
        Person person= conversion.toPerson(personForm);
        if(personDAO.findAll().contains(person)) throw new DuplicateException("personForm has been existed in database");
        Person savedPerson= personDAO.save(person);
        return conversion.toPersonDto(savedPerson);
    }

    @Override
    @Transactional
    public PersonDto findById(Integer personId){
        if(personId==null) throw new IllegalArgumentException("person Id is null");
        Person found = personDAO.findById(personId).orElseThrow(()-> new AppResourceNotFoundException("not found"));
       return conversion.toPersonDto(found);
    }
    @Override
    public List<PersonDto> findAll() {
        List<Person> all = personDAO.findAll();
        return all.stream().map(conversion::toPersonDto).collect(Collectors.toList());
    }

    @Override
    public List<PersonDto> findIdlePeople() {
        List<Person> idlePeople = personDAO.findIdlePeople();
        return idlePeople.stream().map(conversion::toPersonDto).collect(Collectors.toList());
    }

    @Override
    public PersonDto update(Integer personId, PersonForm personForm) {
        if(personId==null) throw new IllegalArgumentException("person Id is null");
        if(personForm==null) throw new IllegalArgumentException("personForm is null");
        Person found = personDAO.findById(personId).orElseThrow(()->new AppResourceNotFoundException("not found the person with that id"));
        found.setFirstName(personForm.getFirstName());
        found.setLastName(personForm.getLastName());
        found.setBirthDate(personForm.getBirthDate());
        return conversion.toPersonDto(found);
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
        return conversion.toPersonDto(found);
    }

    @Override
    public PersonDto removeTodoItem(Integer personId, Integer todoItemId) {
        if(personId==null) throw new IllegalArgumentException("personId was null");
        if(todoItemId==null) throw new IllegalArgumentException("todoItemId is null");
        if(!personDAO.existsById(personId)) throw new AppResourceNotFoundException("There is no person with that personId in the database");
        if(!todoItemDAO.existsById(todoItemId)) throw new AppResourceNotFoundException("There is no todoItem with that TodoItemId in the database");
        Person found=personDAO.findById(personId).get();
        found.removeTodoItem(todoItemDAO.findById(todoItemId).get());
        return conversion.toPersonDto(found);
    }
}
