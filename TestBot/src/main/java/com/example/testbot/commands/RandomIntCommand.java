package com.example.testbot.commands;

import com.example.testbot.TestBot;
import com.example.testbot.utils.Randomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class RandomIntCommand extends BotCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBot.class);

    public RandomIntCommand() {
        super("random_number", "Команда для получения случайного числа от 0 до 10");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        String str = "Случайное число: " +
                Randomizer.getRandomInt();
        SendMessage answer = new SendMessage(chat.getId().toString(), str);
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            LOGGER.error("Exception during executing RandomIntCommand: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
