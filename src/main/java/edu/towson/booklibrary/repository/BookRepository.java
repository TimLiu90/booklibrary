package edu.towson.booklibrary.repository;

import edu.towson.booklibrary.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    public Book findByIsbn(String isbn);
}
