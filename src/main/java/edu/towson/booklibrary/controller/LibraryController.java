package edu.towson.booklibrary.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LibraryController {

    @GetMapping(value = "/admin/bookmgnt")
    public String showBookMgnt(){
        return "bookMgnt";
    }

    @GetMapping(value = "/admin/borrow/history")
    public String showBorrowHistory(ModelMap modelMap){
        return "borrowHistory";
    }


}
