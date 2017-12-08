package edu.towson.booklibrary.service.impl;

import edu.towson.booklibrary.domain.Book;
import edu.towson.booklibrary.domain.BorrowBook;
import edu.towson.booklibrary.domain.User;
import edu.towson.booklibrary.repository.BorrowBookRepository;
import edu.towson.booklibrary.service.BorrowBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BorrowBookServiceImpl implements BorrowBookService {

    @Autowired
    BorrowBookRepository borrowBookRepository;

    @Override
    public BorrowBook borrowABook(BorrowBook borrowBook) {
        return borrowBookRepository.save(borrowBook);
    }

    @Override
    public BorrowBook findById(String id) {
        return borrowBookRepository.findOne(id);
    }

    @Override
    public BorrowBook saveHistory(BorrowBook borrowBook) {
        return borrowBookRepository.save(borrowBook);
    }

    @Override
    public BorrowBook findBorrowedBook(String username, String isbn, boolean returned) {
        return borrowBookRepository.findByBorrowerUsernameAndBorrowedBookIsbnAndReturned(username, isbn, returned);
    }
}
