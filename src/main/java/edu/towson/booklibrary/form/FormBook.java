package edu.towson.booklibrary.form;

import org.hibernate.validator.constraints.NotBlank;

public class FormBook {

    @NotBlank(message = "ISBN can't blank !")
    private String isbn;
    private int bookTotal;
    private String duplicated;

    private String emptybook;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getBookTotal() {
        return bookTotal;
    }

    public void setBookTotal(int bookTotal) {
        this.bookTotal = bookTotal;
    }

    public String getEmptybook() {
        return emptybook;
    }

    public void setEmptybook(String emptybook) {
        this.emptybook = emptybook;
    }

    public String getDuplicated() {
        return duplicated;
    }

    public void setDuplicated(String duplicated) {
        this.duplicated = duplicated;
    }
}
