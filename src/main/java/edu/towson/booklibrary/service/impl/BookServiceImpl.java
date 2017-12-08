package edu.towson.booklibrary.service.impl;

import edu.towson.booklibrary.domain.Book;
import edu.towson.booklibrary.repository.BookRepository;
import edu.towson.booklibrary.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;


    @Override
    public List<Book> allBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book findByISBN(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    public Book findById(String id) {
        return bookRepository.findOne(id);
    }
}
