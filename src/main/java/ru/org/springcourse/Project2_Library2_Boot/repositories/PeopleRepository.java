package ru.org.springcourse.Project2_Library2_Boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.org.springcourse.Project2_Library2_Boot.model.Person;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface PeopleRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByEmail(String email);
}
