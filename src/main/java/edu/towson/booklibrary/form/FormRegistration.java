package edu.towson.booklibrary.form;

import edu.towson.booklibrary.domain.Role;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

//@FieldsValueMatches.List({
//        @FieldsValueMatches(
//                field = "password",
//                fieldMatch = "passwordAgain",
//                message = "Password not match"
//        ),
//        @FieldsValueMatches(
//                field = "email",
//                fieldMatch = "emailAgain",
//                message = "Email not match"
//        )
//})
public class FormRegistration {

    @NotEmpty(message = "Username can't be empty")
    @NotNull (message = "Username can't be null")
    private String username;
    @NotEmpty(message = "Password can't be empty")
    @NotNull
    private String password;
    private String passwordAgain;
    @NotEmpty(message = "Email is required")
    @NotNull (message = "Email is required")
    @Email(message = "Please provide valid email address")
    private String email;

    private String emailAgain;

    private boolean acceptTerms;

    private Role role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailAgain() {
        return emailAgain;
    }

    public void setEmailAgain(String emailAgain) {
        this.emailAgain = emailAgain;
    }

    public String getPasswordAgain() {
        return passwordAgain;
    }

    public void setPasswordAgain(String passwordAgain) {
        this.passwordAgain = passwordAgain;
    }

    public boolean isAcceptTerms() {
        return acceptTerms;
    }

    public void setAcceptTerms(boolean acceptTerms) {
        this.acceptTerms = acceptTerms;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
