package edu.towson.booklibrary.service.impl;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import edu.towson.booklibrary.domain.User;
import edu.towson.booklibrary.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Override
    public void initiateNotification(User user) throws InterruptedException {

        System.out.print("Handle event here: " + user.getEmail());

        try {
            HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/moolda.com/messages")
                    .basicAuth("api", "key-a196e913edcb651695199e8c5cb350e6")
                    .queryString("from", "Library <zhijiang@chen.me>")
                    .queryString("to", user.getEmail())
                    .queryString("subject", "Welcome to Library")
                    .queryString("text", "Welcome to Library portal !")
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
