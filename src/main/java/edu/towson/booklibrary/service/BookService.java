package edu.towson.booklibrary.service;

import edu.towson.booklibrary.domain.Book;

import java.util.List;

public interface BookService {

    public List<Book> allBooks();
    public Book saveBook(Book book);
    public Book findByISBN(String isbn);
    public Book findById(String id);
}
