package com.example.testbot.services;

import com.example.testbot.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getUser(long chatId);

    void addUser(User user);

    void deleteUser(long chatId);

    List<User> findALlUsers();

    void unsubscribe(long chatId);

    List<User> getSubscribedUsers();

}
