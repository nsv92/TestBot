package com.example.testbot;

import com.example.testbot.config.BotConfig;
import com.example.testbot.handlers.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TestBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBot.class);

    private final BotConfig config;

    private final CommandHandler commandHandler;

    @Autowired
    public TestBot(BotConfig config, CommandHandler commandHandler) {
        this.config = config;
        this.commandHandler = commandHandler;
    }

    @Override
    public String getBotUsername() {
        return config.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }


    @Override
    public void onUpdateReceived(Update update) {

        LOGGER.info("New update received from: {}, message: {}", getUserNameFromUpdate(update), update.getMessage().getText());
        if (update.getMessage() != null) {
            try {
                execute(commandHandler.handle(update));
            } catch (TelegramApiException e) {
                LOGGER.error("Exception during sending message: {}", e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public String getUserNameFromUpdate(Update update) {
        User user = update.getMessage().getFrom();
        return (user.getUserName() == null ? user.getFirstName() : user.getUserName());
    }


}
