package ru.org.springcourse.Project2_Library2_Boot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.org.springcourse.Project2_Library2_Boot.model.Person;
import ru.org.springcourse.Project2_Library2_Boot.services.PeopleService;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {
    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        Optional<Person> sameValuePerson = peopleService.findByEmail(person.getEmail());
        if (sameValuePerson.isPresent() && sameValuePerson.get().getId() != person.getId())
            errors.rejectValue("email", "", "Эта почта уже занята");
    }
}
