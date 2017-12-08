package edu.towson.booklibrary.event.listener;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import edu.towson.booklibrary.domain.User;
import edu.towson.booklibrary.event.UserRegistrationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class UserRegistrationEventListener {


    @Async
    @EventListener
    public void handleUserRegistrationEvent(UserRegistrationEvent userRegistrationEvent){

        User user = userRegistrationEvent.getUser();

        System.out.print("Handle event here: " + userRegistrationEvent.getUser().getEmail());

        try {
            HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/moolda.com/messages")
                    .basicAuth("api", "key-a196e913edcb651695199e8c5cb350e6")
                    .queryString("from", "Library <administrator@library.io>")
                    .queryString("to", user.getEmail())
                    .queryString("subject", "Welcome to Library")
                    .queryString("text", "Welcome to Library portal !")
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

    }
}
