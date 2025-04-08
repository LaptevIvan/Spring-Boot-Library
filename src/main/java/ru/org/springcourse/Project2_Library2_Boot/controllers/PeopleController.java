package ru.org.springcourse.Project2_Library2_Boot.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.org.springcourse.Project2_Library2_Boot.model.Book;
import ru.org.springcourse.Project2_Library2_Boot.model.Person;
import ru.org.springcourse.Project2_Library2_Boot.services.BooksService;
import ru.org.springcourse.Project2_Library2_Boot.services.PeopleService;
import ru.org.springcourse.Project2_Library2_Boot.util.PersonValidator;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;
    private final BooksService booksService;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PeopleService peopleService, BooksService booksService, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.booksService = booksService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String showPeople(Model model) {
        model.addAttribute("people", peopleService.findAll());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String showPerson(@PathVariable("id") int id, Model model) {
        Optional<Person> optPerson = peopleService.findById(id);
        if (optPerson.isEmpty())
            throw new AssertionError();

        Person person = optPerson.get();
        model.addAttribute("person", person);
        List<Book> lst = booksService.checkBooksDelay(person);
        model.addAttribute("books", lst);

        return "people/show";
    }

    @GetMapping("/new")
    public String showCreatingPerson(@ModelAttribute("person") Person person) {
        return "people/create";
    }

    @PostMapping()
    public String createPerson(@ModelAttribute("person") @Valid Person person, BindingResult check) {
        personValidator.validate(person, check);

        if (check.hasErrors())
            return "people/create";

        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String showUpdatingPerson(@PathVariable("id") int id, Model model) {
        Optional<Person> optPerson = peopleService.findById(id);
        if (optPerson.isEmpty())
            throw new AssertionError();

        model.addAttribute("person", optPerson.get());
        return "people/update";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") @Valid Person updatePerson, BindingResult check,
                               @PathVariable("id") int id) {
        updatePerson.setId(id);
        personValidator.validate(updatePerson, check);
        if (check.hasErrors())
            return "people/update";

        peopleService.update(id, updatePerson);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }
}
