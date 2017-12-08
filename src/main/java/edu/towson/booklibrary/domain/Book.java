package edu.towson.booklibrary.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Book {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String name;
    private String title;
    private String author;
    @Column(unique = true)
    private String isbn;
    private int total;
    private Date publication;
    @Lob
    private String description;
    private String thumbnail;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bookmeta_id")
    private BookMeta bookMeta;

    @OneToMany(mappedBy = "borrowedBook")
    private List<BorrowBook> borrowBookList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getPublication() {
        return publication;
    }

    public void setPublication(Date publication) {
        this.publication = publication;
    }

    public BookMeta getBookMeta() {
        return bookMeta;
    }

    public void setBookMeta(BookMeta bookMeta) {
        this.bookMeta = bookMeta;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BorrowBook> getBorrowBookList() {
        return borrowBookList;
    }

    public void setBorrowBookList(List<BorrowBook> borrowBookList) {
        this.borrowBookList = borrowBookList;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}