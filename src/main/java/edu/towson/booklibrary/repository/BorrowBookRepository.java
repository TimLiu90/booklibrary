package edu.towson.booklibrary.repository;


import edu.towson.booklibrary.domain.BorrowBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowBookRepository extends JpaRepository<BorrowBook, String> {

    public BorrowBook findByBorrowerUsernameAndBorrowedBookIsbnAndReturned(String username, String isbn, boolean returned);
}
