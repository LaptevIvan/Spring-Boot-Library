package ru.org.springcourse.Project2_Library2_Boot.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.org.springcourse.Project2_Library2_Boot.model.Book;
import ru.org.springcourse.Project2_Library2_Boot.services.BooksService;

import java.util.Optional;


@Component
public class BookValidator implements Validator {

    private final BooksService booksService;

    @Autowired
    public BookValidator(BooksService booksService) {
        this.booksService = booksService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Book.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;
        Optional<Book> sameBook = booksService.findByNameAndAuthor(book.getName(), book.getNameAuthor());
        if (sameBook.isPresent() && sameBook.get().getId() != book.getId())
            errors.rejectValue("name", "", "Эта книга уже издана этим автором");
    }
}
