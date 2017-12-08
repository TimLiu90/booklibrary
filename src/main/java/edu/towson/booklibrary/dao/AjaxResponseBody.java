package edu.towson.booklibrary.dao;

import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

public class AjaxResponseBody {

    private String status;
    private String message;
    private List<ObjectError> errorList = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ObjectError> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<ObjectError> errorList) {
        this.errorList = errorList;
    }
}
