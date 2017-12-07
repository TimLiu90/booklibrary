package edu.towson.booklibrary.controller;


import edu.towson.booklibrary.domain.Role;
import edu.towson.booklibrary.domain.User;
import edu.towson.booklibrary.event.UserRegistrationEvent;
import edu.towson.booklibrary.form.FormRegistration;
import edu.towson.booklibrary.service.RoleService;
import edu.towson.booklibrary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    RoleService roleService;

    /**
     * Show login page
     * @param modelMap
     * @return
     */
    @GetMapping(value = "/login")
    public String showLogin(ModelMap modelMap){

        return "login";
    }

    /**
     * Show registration page
     * @param modelMap
     * @return
     */
    @GetMapping(value = "/registration")
    public String showRegistration(ModelMap modelMap){

        FormRegistration formRegistration = new FormRegistration();
        modelMap.addAttribute("user", formRegistration);
        return "registration";
    }

    /**
     * Register form
     * @param modelMap
     * @param user
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/register_user")
    public String registerUser(ModelMap modelMap,
                               @ModelAttribute("user") @Valid FormRegistration user,
                               BindingResult bindingResult){

        if(!user.getPassword().equals(user.getPasswordAgain())){
            bindingResult.rejectValue("passwordAgain", "passwords.not.match", "Password not match !");
        }
        if(!bindingResult.hasFieldErrors("email") && !user.getEmail().equals(user.getEmailAgain())){
            bindingResult.rejectValue("emailAgain", "email.not.match", "Email not match !");
        }
        if(!user.isAcceptTerms()){
            bindingResult.rejectValue("acceptTerms", "terms.not.accept", "You must accept terms !");
        }

        // Check for user existing.
        if(user.getUsername() != null || user.getUsername() !=""){
            User savedUser = userService.findByUsername(user.getUsername());
            if(savedUser != null){
//                bindingResult.rejectValue("username", "error.user.existed", messageSource.getMessage("error.user.existed", null, Locale.US));
                bindingResult.rejectValue("username", "error.user.existed", "User already existed in system !");
            }
        }
        if(bindingResult.hasErrors()){
            modelMap.addAttribute("user", user);
            return "registration";
        }

        // Save to db.

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        Role role = roleService.findByRole("ROLE_BORROWER");
        // TODO set inactive and waiting for validation...
        newUser.setActive(true);
        newUser.setEmail(user.getEmail());
        newUser.setRole(role);

        userService.saveUser(newUser);

        // Publish event
        UserRegistrationEvent userRegistrationEvent = new UserRegistrationEvent(this, newUser);
        applicationEventPublisher.publishEvent(userRegistrationEvent);

//        eventBus.notify("newUserCreatedEvent", Event.wrap(user));

        return "login";
    }
}
