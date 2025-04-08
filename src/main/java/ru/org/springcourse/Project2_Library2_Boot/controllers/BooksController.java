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
import ru.org.springcourse.Project2_Library2_Boot.util.BookValidator;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;

    private final PeopleService peopleService;
    private final BookValidator bookValidator;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService, BookValidator bookValidator) {
        this.booksService = booksService;
        this.peopleService = peopleService;
        this.bookValidator = bookValidator;
    }


    @GetMapping()
    public String showBooks(@RequestParam(value = "page", required = false) Integer page,
                            @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                            @RequestParam(value = "sort", required = false) boolean sort,
                            Model model) {
        List<Book> books = null;
        if (page != null && booksPerPage != null) {
                books = booksService.findAll(page, booksPerPage, sort);
        } else
            books = booksService.findAll(sort);

        model.addAttribute("books", books);
        return "books/index";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, Model model) {
        Optional<Book> optBook = booksService.findOne(id);
        if (optBook.isEmpty())
            throw new AssertionError();

        Book book = optBook.get();
        model.addAttribute("book", book);
        Person owner = book.getOwner();
        if (owner != null) {
            model.addAttribute("owner", owner);
        } else {
            model.addAttribute("people", peopleService.findAll());
            model.addAttribute("newOwner", new Person());
        }

        return "books/show";
    }

    @GetMapping("/new")
    public String showCreatingBook(@ModelAttribute("book") Book book) {
        return "books/create";
    }

    @PostMapping()
    public String createBook(@ModelAttribute("book") @Valid Book book, BindingResult check) {
        bookValidator.validate(book, check);
        if (check.hasErrors())
            return "books/create";

        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String showUpdatingBook(@PathVariable("id") int id, Model model) {
        Optional<Book> optBook = booksService.findOne(id);
        if (optBook.isEmpty())
            throw new AssertionError();
        model.addAttribute("book", optBook.get());
        return "books/update";
    }

    @PatchMapping("/{id}")
    public String updateBook(@ModelAttribute("book") @Valid Book updateBook, BindingResult check,
                             @PathVariable("id") int id) {
        bookValidator.validate(updateBook, check);
        if (check.hasErrors())
            return "books/update";

        booksService.update(id, updateBook);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/setOwner")
    public String setOwnerBook(@ModelAttribute("newOwner") Person newOwner, @PathVariable("id") int id) {
        booksService.updateBookByOwner(id, newOwner);
        return "redirect:/books/{id}";
    }

    @PatchMapping("/{id}/toFree")
    public String toFreeBook(@PathVariable("id") int id) {
        booksService.toFree(id);
        return "redirect:/books/{id}";
    }


    @GetMapping("/search")
    public String searchBooks() {
        return "books/search";
    }

    @PatchMapping("/search")
    public String searchFindBooks(@RequestParam("searchName") String searchName, Model model) {
        List<Book> books = booksService.findAllByNameStartingWith(searchName);
        System.out.println(books.size());
        model.addAttribute("findBooks", books);
        return "books/search";
    }
}
