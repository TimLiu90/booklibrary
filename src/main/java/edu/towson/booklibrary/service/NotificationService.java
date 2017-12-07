package edu.towson.booklibrary.service;

import edu.towson.booklibrary.domain.User;

public interface NotificationService {

    void initiateNotification(User user)
            throws InterruptedException;
}
