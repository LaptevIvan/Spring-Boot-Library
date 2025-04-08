package ru.org.springcourse.Project2_Library2_Boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.org.springcourse.Project2_Library2_Boot.model.Book;
import ru.org.springcourse.Project2_Library2_Boot.model.Person;

import java.util.List;
import java.util.Optional;


public interface BooksRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findByNameAndNameAuthor(String name, String nameAuthor);

    List<Book> findAllByOwner(Person person);

    List<Book> findAllByNameStartingWith(String startinfPart);
}
