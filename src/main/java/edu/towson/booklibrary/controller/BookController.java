package edu.towson.booklibrary.controller;


import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;
import edu.towson.booklibrary.dao.AjaxResponseBody;
import edu.towson.booklibrary.dao.BookDAO;
import edu.towson.booklibrary.dao.HistoryDAO;
import edu.towson.booklibrary.domain.Book;
import edu.towson.booklibrary.domain.BookMeta;
import edu.towson.booklibrary.domain.BorrowBook;
import edu.towson.booklibrary.domain.User;
import edu.towson.booklibrary.form.FormBook;
import edu.towson.booklibrary.form.FormBookTotalAdjust;
import edu.towson.booklibrary.service.BookService;
import edu.towson.booklibrary.service.BorrowBookService;
import edu.towson.booklibrary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.glassfish.jersey.server.ServerProperties.APPLICATION_NAME;

@Controller
public class BookController {

    @Autowired
    UserService userService;
    @Autowired
    BookService bookService;
    @Autowired
    BorrowBookService borrowBookService;

    @Value("${google.api.book}")
    private String googleBookApiKey;

    @PostMapping(value = "/api/book/all", produces = "application/json")
    @ResponseBody
    public List<BookDAO> getAllBooks(ModelMap modelMap, Principal principal){

        modelMap.addAttribute("book", new Book());

        // Check is admin
        String userName = principal.getName();
        User user = userService.findByUsername(userName);
        boolean isAdmin = user.getRole().getRole().equalsIgnoreCase("ROLE_ADMIN");
        List<BookDAO> bookDAOS = new ArrayList<>();

        List<Book> books = bookService.allBooks();
        books.forEach(book -> {
            BookDAO bookDAO = new BookDAO();
            bookDAO.setAdmin(isAdmin);
            bookDAO.setId(book.getId());
            bookDAO.setName(book.getName());
            bookDAO.setISBN(book.getIsbn());
            bookDAO.setTotal(book.getTotal());
            bookDAO.setAvailable(book.getBookMeta().getAvailable());

            bookDAOS.add(bookDAO);
        });
        return bookDAOS;
    }

    @PostMapping(value = "/api/book/borrow/history", produces = "application/json")
    @ResponseBody
    public List<HistoryDAO> getAllBorrowHistory(Principal principal){

        List<HistoryDAO> historyDAOS = new ArrayList<>();

        // Get my borrow history.
        // Get user
        User user = userService.findByUsername(principal.getName());
        // Get history
        List<BorrowBook> borrowBooks = user.getBorrowBookList();
        borrowBooks.forEach(history -> {
            HistoryDAO historyDAO = new HistoryDAO();
            historyDAO.setHistoryId(history.getId());
            historyDAO.setName(history.getBorrowedBook().getName());
            historyDAO.setIsbn(history.getBorrowedBook().getIsbn());
            historyDAO.setReturned(history.isReturned());
            historyDAO.setBorrowDate(history.getBorrowDate());
            historyDAOS.add(historyDAO);
        });

        return historyDAOS;

    }

    /**
     * Adjust books' total
     * @param formBookTotalAdjust
     * @return
     */
    @PostMapping(value = "/book/total/adjust")
    public ResponseEntity<?> adjustBookTotal(@RequestBody FormBookTotalAdjust formBookTotalAdjust){

        AjaxResponseBody ajaxResponseBody = new AjaxResponseBody();
        // Get book
        Book book = bookService.findById(formBookTotalAdjust.getBookid());
        // Adjust
        book.setTotal(book.getTotal()+formBookTotalAdjust.getIncrement());

        // Adjust the book available amount as well
        book.getBookMeta().setAvailable(book.getBookMeta().getAvailable()+formBookTotalAdjust.getIncrement());

        if(book.getTotal() < 0 || book.getBookMeta().getAvailable() < 0){
            ajaxResponseBody.getErrorList().add(new ObjectError("error.count", "Book total or available total can't less than zero"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ajaxResponseBody);
        }
        // Save back to database
        bookService.saveBook(book);

        return ResponseEntity.ok(ajaxResponseBody);
    }


    /**
     * Add new book to library
     * @param formBook
     * @param errors
     * @return
     */
    @PostMapping(value = "/book/new")
    public ResponseEntity<?> newBook(@Valid @RequestBody FormBook formBook,
                                     Errors errors){


        Book book = new Book();
        AjaxResponseBody ajaxResponseBody = new AjaxResponseBody();

        if(errors.hasErrors()){
            ajaxResponseBody.setErrorList(errors.getAllErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ajaxResponseBody);
        }

        // Retrieve data from online database
        // Set up Books client.
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        try {

            final Books books = new Books.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, null)
                    .setApplicationName(APPLICATION_NAME)
                    .setGoogleClientRequestInitializer(new BooksRequestInitializer(googleBookApiKey))
                    .build();

            com.google.api.services.books.Books.Volumes.List volumesList = books.volumes().list("isbn:"+formBook.getIsbn());
            Volumes volumes = volumesList.execute();

            // No book found
            if (volumes.getTotalItems() == 0 || volumes.getItems() == null) {
                System.out.println("No matches found.");
                // Error.
                errors.rejectValue("emptybook", "error.book.notexisting", "Can't find book by this ISBN");
                ajaxResponseBody.setErrorList(errors.getAllErrors());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ajaxResponseBody);
            }

            for(Volume volume : volumes.getItems()){
                Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();

                // Set ISBN
                book.setIsbn(formBook.getIsbn());
                // Description (if any).
                if (volumeInfo.getDescription() != null && volumeInfo.getDescription().length() > 0) {
                    System.out.println("Description: " + volumeInfo.getDescription());
                    book.setDescription(volumeInfo.getDescription());
                }
                if(volumeInfo.getTitle() !=null && volumeInfo.getTitle().length() > 0 ){
                    book.setName(volumeInfo.getTitle());
                }

                book.setTotal(formBook.getBookTotal());
                book.setThumbnail(volumeInfo.getImageLinks().getThumbnail());
                BookMeta bookMeta = new BookMeta();
                bookMeta.setAvailable(formBook.getBookTotal());
                book.setBookMeta(bookMeta);
                bookService.saveBook(book);
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            errors.rejectValue("duplicated", "error.book.duplicated", "This book already in library");
            ajaxResponseBody.setErrorList(errors.getAllErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ajaxResponseBody);
        }
        return ResponseEntity.ok(book);
    }


    /**
     * Borrow book
     * @param bookId
     * @param principal
     * @return
     */
    @GetMapping(value = "/book/borrow/{bookId}", produces = "application/json")
    public ResponseEntity<?> borrowBook(@PathVariable("bookId") String bookId, Principal principal){

        AjaxResponseBody ajaxResponseBody = new AjaxResponseBody();

        // Find the book
        Book book = bookService.findById(bookId);
        // Check for the availability
        if(book.getBookMeta().getAvailable() == 0){
            ObjectError objectError = new ObjectError("notavailable", "Book not available right now!");
            ajaxResponseBody.getErrorList().add(objectError);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ajaxResponseBody);
        }

        // Check for duplicated borrowed book
        BorrowBook borrowBook = borrowBookService.findBorrowedBook(principal.getName(), book.getIsbn(), false);
        if(borrowBook != null){
            ObjectError objectError = new ObjectError("duplicated", "You already borrow this book!");
            ajaxResponseBody.getErrorList().add(objectError);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ajaxResponseBody);
        }

        // Save to borrow history table.
        try {
            // Get user
            User user = userService.findByUsername(principal.getName());
            borrowBook = new BorrowBook();
            borrowBook.setBorrower(user);
            borrowBook.setBorrowedBook(book);
            borrowBook.setBorrowDate(new Date());
            borrowBookService.borrowABook(borrowBook);

            // Update database.
            book.getBookMeta().setAvailable(book.getBookMeta().getAvailable() - 1);
            bookService.saveBook(book);
        }catch(Exception e){

        }

        return ResponseEntity.ok(ajaxResponseBody);
    }


    /**
     * Return book to library
     * @param historyId
     * @param principal
     * @return
     */
    @GetMapping(value = "/book/return/{historyid}", produces = "application/json")
    public ResponseEntity<?> returnBook(@PathVariable("historyid") String historyId, Principal principal){

        AjaxResponseBody ajaxResponseBody = new AjaxResponseBody();

        // Find borrow history
        BorrowBook borrowBook = borrowBookService.findById(historyId);
        // Set return = true
        borrowBook.setReturned(true);
        borrowBookService.saveHistory(borrowBook);

        // Update the book available
        Book book = borrowBook.getBorrowedBook();
        book.getBookMeta().setAvailable(book.getBookMeta().getAvailable()+1);
        bookService.saveBook(book);

        return ResponseEntity.ok(ajaxResponseBody);
    }
}
