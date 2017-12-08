package edu.towson.booklibrary.event;

import edu.towson.booklibrary.domain.User;
import org.springframework.context.ApplicationEvent;

public class UserRegistrationEvent extends ApplicationEvent {

    private User user;

    public UserRegistrationEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

    public User getUser(){
        return user;
    }
}
