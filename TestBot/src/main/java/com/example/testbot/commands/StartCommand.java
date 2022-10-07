package com.example.testbot.commands;

import com.example.testbot.TestBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class StartCommand extends BotCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBot.class);

    public StartCommand() {
        super("start", "Команда для начала работы с ботом");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        StringBuilder messageBuilder = new StringBuilder();
        String userName = user.getUserName() == null ? user.getFirstName() : user.getUserName();

        messageBuilder.append("Привет ").append(userName).append("!").append("\n");
        messageBuilder.append("Данный бот создан для обучения!").append("\n");

        SendMessage answer = new SendMessage(chat.getId().toString(), messageBuilder.toString());

        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            LOGGER.error("Exception during executing StartCommand: {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
