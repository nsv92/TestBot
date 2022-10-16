package com.example.testbot;

import com.example.testbot.commands.RandomBooleanCommand;
import com.example.testbot.commands.RandomIntCommand;
import com.example.testbot.commands.StartCommand;
import com.example.testbot.config.BotConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommandsBot extends TelegramLongPollingCommandBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBot.class);

    private final BotConfig config;

    public CommandsBot(BotConfig config) {
        this.config = config;


//  Для инициализации меню команд т.к. в библиотеке meta.extensions не реализовано меню команд

//        List<BotCommand> commandList = new ArrayList<>();
//        commandList.add(new BotCommand("/help", "Список всех команд"));
//        commandList.add(new BotCommand("/start", "Команда для начала работы с ботом"));
//        commandList.add(new BotCommand("/random_number", "Команда для получения случайного числа от 0 до 10"));
//        commandList.add(new BotCommand("/random_boolean", "Команда для получения случайного ДА/НЕТ"));
//
//        try {
//            this.execute(new SetMyCommands(commandList, new BotCommandScopeDefault(), null));
//        } catch (TelegramApiException e) {
//            throw new RuntimeException(e);
//        }


        register(new StartCommand());
        register(new RandomIntCommand());
        register(new RandomBooleanCommand());
        register(new HelpCommand("/help", "Список всех команд", "Помощь"));

        registerDefaultAction(((absSender, message) -> {
            SendMessage commandUnknownMessage = new SendMessage();
            commandUnknownMessage.setChatId(message.getChatId());
            commandUnknownMessage.setText("The command '" + message.getText()
                    + "' is not known by this bot.");
            try {
                absSender.execute(commandUnknownMessage);
            } catch (TelegramApiException e) {
                LOGGER.error("Exception during executing unknown command: {}", e.getMessage());
                throw new RuntimeException(e);
            }

        }));

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
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasText()) {
                SendMessage echoMessage = new SendMessage();
                echoMessage.setChatId(message.getChatId());
                echoMessage.setText("Hey here`s your message:\n" + message.getText());

                try {
                    execute(echoMessage);
                } catch (TelegramApiException e) {
                    LOGGER.error("Exception during ec" +
                            "ho: {}", e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
