package edu.towson.booklibrary.service;


import edu.towson.booklibrary.domain.Book;
import edu.towson.booklibrary.domain.BorrowBook;
import edu.towson.booklibrary.domain.User;

public interface BorrowBookService {

    public BorrowBook borrowABook(BorrowBook borrowBook);
    public BorrowBook findById(String id);
    public BorrowBook saveHistory(BorrowBook borrowBook);
    public BorrowBook findBorrowedBook(String username, String isbn, boolean returned);
}
