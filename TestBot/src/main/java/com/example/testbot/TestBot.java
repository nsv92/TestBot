package com.example.testbot;

import com.example.testbot.config.BotConfig;
import com.example.testbot.handlers.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TestBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBot.class);

    private final BotConfig config;

    private final CommandHandler commandHandler;

//    private final ZodiacService zodiacService;

    @Autowired
    public TestBot(BotConfig config, CommandHandler commandHandler) {
        this.config = config;
        this.commandHandler = commandHandler;


        List<BotCommand> commandList = new ArrayList<>();
        commandList.add(new BotCommand("/help", "Список всех команд"));
        commandList.add(new BotCommand("/start", "Команда для начала работы с ботом"));
        commandList.add(new BotCommand("/random10", "Команда для получения случайного числа от 0 до 10"));
        commandList.add(new BotCommand("/randomb", "Команда для получения случайного ДА/НЕТ"));
        commandList.add(new BotCommand("/zodiac", "Команда для получения гороскопа"));

        try {
            this.execute(new SetMyCommands(commandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
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

        if (update.getMessage() != null) {
            LOGGER.info("New update received from: {}, message: {}", getUserNameFromUpdate(update), update.getMessage().getText());
            if (update.getMessage().hasText()) {
                try {
                    execute(commandHandler.handleTextMessage(update));
                } catch (TelegramApiException e) {
                    LOGGER.error("Exception during sending message: {}", e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }

        if (update.hasCallbackQuery()) {
            LOGGER.info("New CallbackQuery received from: {}", getUserNameFromUpdate(update));
            try {
                execute(commandHandler.handleCallbackQuery(update));
            } catch (Exception e) {
                LOGGER.error("Exception during handling CallbackQuery: {}", e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public String getUserNameFromUpdate(Update update) {
        User user;
        if (update.getMessage() != null) {
            user = update.getMessage().getFrom();
        } else {
            user = update.getCallbackQuery().getFrom();
        }
        return (user.getUserName() == null ? user.getFirstName() : user.getUserName());
    }


}
