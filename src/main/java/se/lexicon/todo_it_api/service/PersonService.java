package se.lexicon.todo_it_api.service;


import se.lexicon.todo_it_api.dto.PersonDto;
import se.lexicon.todo_it_api.dto.PersonDtoSmall;
import se.lexicon.todo_it_api.exception.AppResourceNotFoundException;
import se.lexicon.todo_it_api.form.PersonForm;

import java.util.List;

public interface PersonService {

    PersonDto create(PersonForm personForm) throws AppResourceNotFoundException;
    PersonDto findById(Integer personId) throws AppResourceNotFoundException;
    List<PersonDto> findAll() throws AppResourceNotFoundException;
    List<PersonDto> findIdlePeople() throws AppResourceNotFoundException;
    PersonDto update(Integer personId, PersonForm personForm) throws AppResourceNotFoundException;
    boolean delete(Integer personId) throws AppResourceNotFoundException;
    PersonDto addTodoItem(Integer personId, Integer todoItemId) throws AppResourceNotFoundException;
    PersonDto removeTodoItem(Integer personId, Integer todoItemId) throws AppResourceNotFoundException;

}
