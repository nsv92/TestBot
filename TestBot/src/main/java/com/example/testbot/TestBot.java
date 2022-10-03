package com.example.testbot;

import com.example.testbot.config.BotConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TestBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBot.class);

    private final BotConfig config;

    public TestBot(BotConfig config) {
        this.config = config;
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
        String username = getUserNameFromUpdate(update);
        LOGGER.info("New update received from: {}, message: {}",username, update.getMessage().getText());
        if (update.getMessage() != null) {
            Long chatId = update.getMessage().getChatId();
            String echo = update.getMessage().getText();
            System.out.println(echo);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(chatId));
            sendMessage.setText(echo);
            try {
                execute(sendMessage);
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
