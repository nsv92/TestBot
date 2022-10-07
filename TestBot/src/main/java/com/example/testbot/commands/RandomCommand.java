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

public class RandomCommand extends BotCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBot.class);

    public RandomCommand() {
        super("random", "Команда для получения случайного числа от 0 до 10");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Случайное число: ");
        int random = (int) (Math.random() * 10);
        messageBuilder.append(random);

        SendMessage answer = new SendMessage(chat.getId().toString(), messageBuilder.toString());
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            LOGGER.error("Exception during executing RandomCommand: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
