package ru.org.springcourse.Project2_Library2_Boot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @Column(name ="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Person owner;

    @Column(name = "name")
    @Size(min=1, max = 100, message = "Пустое или слишком длинное название для книги")
    private String name;

    @Column(name = "name_author")
    @Size(min=1, max = 100, message = "Пустое или слишком длинное имя автора")
    private String nameAuthor;

    @Column(name = "year_publishing")
    @Min(value = 0, message = "Книга не может быть издана в отрицательный год")
    @Max(value = 2024, message = "Нет книг из будущего")
    private int yearPublishing;

    @Column(name = "date_take")
    @Temporal(TemporalType.DATE)
    private Date dateTake;

    @Transient
    private boolean isLate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameAuthor() {
        return nameAuthor;
    }

    public void setNameAuthor(String nameAuthor) {
        this.nameAuthor = nameAuthor;
    }

    public int getYearPublishing() {
        return yearPublishing;
    }

    public void setYearPublishing(int yearOfPublish) {
        this.yearPublishing = yearOfPublish;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Date getDateTake() {
        return dateTake;
    }

    public void setDateTake(Date dateTake) {
        this.dateTake = dateTake;
    }

    public boolean isLate() {
        return isLate;
    }

    public void setLate(boolean late) {
        isLate = late;
    }
}
