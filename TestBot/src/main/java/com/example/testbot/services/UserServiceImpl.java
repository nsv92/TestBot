package com.example.testbot.services;

import com.example.testbot.entities.User;
import com.example.testbot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getUser(long chatId) {
        return userRepository.findById(chatId);
    }

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(long chatId) {
        userRepository.deleteById(chatId);
    }

    @Override
    public List<User> findALlUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public void unsubscribe(long chatId) {
        Optional<User> optionalUser = getUser(chatId);
        if (optionalUser.isPresent() && optionalUser.get().isSubscription()) {
            User user = optionalUser.get();
            user.setSubscription(false);
            addUser(user);
        }
    }

    @Override
    public List<User> getSubscribedUsers() {
        return userRepository.findAllBySubscriptionTrue();
    }
}
