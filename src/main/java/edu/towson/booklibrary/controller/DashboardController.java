package edu.towson.booklibrary.controller;


import edu.towson.booklibrary.domain.Book;
import edu.towson.booklibrary.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    BookService bookService;


    /**
     * Display dashboard with all books in library
     * @param modelMap
     * @return
     */
    @GetMapping(value = {"/dashboard", "/"})
    public String showDashboard(ModelMap modelMap){
        // Get all books
        List<Book> bookList = bookService.allBooks();
        modelMap.addAttribute("books", bookList);
        return "dashboard";
    }
}
