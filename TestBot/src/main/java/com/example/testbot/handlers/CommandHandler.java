package com.example.testbot.handlers;

import com.example.testbot.TestBot;
import com.example.testbot.utils.Randomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
public class CommandHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBot.class);

    static final String HELP_TEXT = "This bot is created to demonstrate Spring capabilities.\n\n" +
            "Available commands list:\n\n" +
            "Type /start to see a welcome message\n\n" +
            "Type /random10 to get random number from 0 to 10\n\n" +
            "Type /randomb to get random YES or NO\n\n" +
            "Type /help to see this message again";

    public SendMessage handle(Update update) {

        String userName = getUserNameFromUpdate(update);
        String text = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        switch (text) {
            case "/start":
                LOGGER.info("START command from user: {}", userName);
                String str = "Привет " + userName + "!" + "\n" +
                        "Данный бот создан для обучения!" + "\n";
                return new SendMessage(String.valueOf(chatId), str);

            case "/help":
                LOGGER.info("HELP command from user: {}", userName);
                return new SendMessage(String.valueOf(chatId), HELP_TEXT);

            case "/random10":
                LOGGER.info("RANDOM10 command from user: {}", userName);
                String randomTen = "Случайное число: " + Randomizer.getRandomInt();
                return new SendMessage(String.valueOf(chatId), randomTen);

            case "/randomb":
                LOGGER.info("RANDOMB command from user: {}", userName);
                String randomBoolean = Randomizer.getRandomBoolean() ? "Ответ: ДА!" : "Ответ: НЕТ!";
                return new SendMessage(String.valueOf(chatId), randomBoolean);

            default:
                LOGGER.info("UNKNOWN command from user: {}", userName);
                return new SendMessage(String.valueOf(chatId), "Неизвестная команда!");
        }

    }

    public String getUserNameFromUpdate(Update update) {
        User user = update.getMessage().getFrom();
        return (user.getUserName() == null ? user.getFirstName() : user.getUserName());
    }
}
