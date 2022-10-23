package com.example.testbot.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bot_users")
public class User {
    @Id
    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "username", nullable = false, unique = true)
    private String userName;

    @Column(name = "zodiac")
    private String zodiac;

    @Column(name = "subscription")
    private boolean subscription;

    public User() {
    }

    public User(Long chatId, String userName) {
        this.chatId = chatId;
        this.userName = userName;
    }

    public User(Long chatId, String userName, String zodiac, boolean subscription) {
        this.chatId = chatId;
        this.userName = userName;
        this.zodiac = zodiac;
        this.subscription = subscription;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getZodiac() {
        return zodiac;
    }

    public void setZodiac(String zodiac) {
        this.zodiac = zodiac;
    }

    public boolean isSubscription() {
        return subscription;
    }

    public void setSubscription(boolean subscription) {
        this.subscription = subscription;
    }
}
