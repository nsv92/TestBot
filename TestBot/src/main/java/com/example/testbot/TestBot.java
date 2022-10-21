package com.example.testbot;

import com.example.testbot.config.BotConfig;
import com.example.testbot.handlers.CommandHandler;
import com.example.testbot.services.HoroscopeServiceImpl;
import com.example.testbot.services.UserService;
import com.example.testbot.services.UserServiceImpl;
import com.example.testbot.services.HoroscopeService;
import com.example.testbot.utils.ZodiacSign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class TestBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBot.class);

    private final BotConfig config;

    private final CommandHandler commandHandler;

    private final UserService userService;

    private final HoroscopeService horoscopeService;

    @Autowired
    public TestBot(BotConfig config, CommandHandler commandHandler, UserServiceImpl userService,
                   HoroscopeServiceImpl horoscopeService) {
        this.config = config;
        this.commandHandler = commandHandler;
        this.userService = userService;
        this.horoscopeService = horoscopeService;


        List<BotCommand> commandList = new ArrayList<>();
        commandList.add(new BotCommand("/help", "Список всех команд"));
        commandList.add(new BotCommand("/start", "Команда для начала работы с ботом"));
        commandList.add(new BotCommand("/random10", "Команда для получения случайного числа от 0 до 10"));
        commandList.add(new BotCommand("/yesorno", "Команда для получения случайного ДА/НЕТ"));
        commandList.add(new BotCommand("/horoscope", "Команда для получения гороскопа"));
        commandList.add(new BotCommand("/unsubscribe", "Команда для отписки от получения гороскопа"));

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
            } else LOGGER.error("Null message from user: {}", getUserNameFromUpdate(update));
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

    @Scheduled(cron = "${interval-in-cron}")
    public void executeSubscriptions() {
        ArrayList<com.example.testbot.entities.User> subscribed =
                (ArrayList<com.example.testbot.entities.User>) userService.getSubscribedUsers();

        for (com.example.testbot.entities.User user : subscribed) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(user.getChatId());

            Optional<ZodiacSign> sign = Arrays.stream(ZodiacSign.values())
                    .filter(s -> s.name().equals(user.getZodiac())).findFirst();
            if (sign.isPresent()) {
                sendMessage.setText(sign.get().getPrediction());
            } else {
                sendMessage.setText("Ошибка сервиса");
            }
            try {
                execute(sendMessage);
                LOGGER.info("Horoscope has been send successfully to subscribed user: {}", sendMessage.getChatId());
            } catch (TelegramApiException e) {
                LOGGER.error("Exception during sending message: {}", e.getMessage());
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
