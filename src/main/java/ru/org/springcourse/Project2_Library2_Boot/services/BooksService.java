package ru.org.springcourse.Project2_Library2_Boot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.org.springcourse.Project2_Library2_Boot.model.Book;
import ru.org.springcourse.Project2_Library2_Boot.model.Person;
import ru.org.springcourse.Project2_Library2_Boot.repositories.BooksRepository;
import ru.org.springcourse.Project2_Library2_Boot.repositories.PeopleRepository;

import java.util.List;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    private final PeopleRepository peopleRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    private void calcIsLate(List<Book> books) {
        long currentDate = new Date().getTime();
        for (Book book : books) {
            if (book.getOwner() != null) {
                long diff = Math.abs(currentDate - book.getDateTake().getTime());
                long diffDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                if (diffDays > 10)
                    book.setLate(true);
                else
                    book.setLate(false);
            } else
                book.setLate(false);
        }
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear)
            return booksRepository.findAll(Sort.by("yearPublishing"));
        return booksRepository.findAll();
    }
    public List<Book> findAll(int page, int booksPerPage, boolean sortByYear) {
        if (sortByYear)
            return booksRepository.findAll(PageRequest.of(page, booksPerPage,Sort.by("yearPublishing"))).getContent();
        return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public Optional<Book> findOne(int id) {
        return booksRepository.findById(id);
    }

    public Optional<Book> findByNameAndAuthor(String name, String nameAuthor) {
        return booksRepository.findByNameAndNameAuthor(name, nameAuthor);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        Book book = booksRepository.findById(id).get();
        updatedBook.setId(id);
        updatedBook.setOwner(book.getOwner());
        updatedBook.setDateTake(book.getDateTake());

        save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }


    @Transactional(readOnly = true)
    public List<Book> checkBooksDelay(Person person) {
        List<Book> books = booksRepository.findAllByOwner(person);
        calcIsLate(books);
        return books;
    }

    @Transactional
    public void updateBookByOwner(int id, Person newOwner) {
        booksRepository.findById(id).ifPresent(book -> {
            book.setOwner(newOwner);

            Random random = new Random();
            int r = random.nextInt(2);
            System.out.println(r);
            Date date = r % 2 == 0 ? new Date() : new Date(2020, 6, 17);

            book.setDateTake(date);
        });
    }

    @Transactional
    public void toFree(int id) {
        Book book = booksRepository.findById(id).get();
        book.setOwner(null);
        book.setDateTake(null);
    }


    public List<Book> findAllByNameStartingWith(String startingParam) {
        return booksRepository.findAllByNameStartingWith(startingParam);
    }

}
