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

public class RandomBooleanCommand  extends BotCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBot.class);

    public RandomBooleanCommand() {
        super("random_boolean", "Команда для получения случайного Да/Нет");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        String str = Randomizer.getRandomBoolean() ? "Ответ: ДА!" : "Ответ: НЕТ!";
        SendMessage answer = new SendMessage(chat.getId().toString(), str);
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            LOGGER.error("Exception during executing RandomBooleanCommand: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
