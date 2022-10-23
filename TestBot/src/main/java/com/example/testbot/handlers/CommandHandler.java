package com.example.testbot.handlers;

import com.example.testbot.TestBot;
import com.example.testbot.services.UserService;
import com.example.testbot.services.UserServiceImpl;
import com.example.testbot.services.HoroscopeService;
import com.example.testbot.services.HoroscopeServiceImpl;
import com.example.testbot.utils.Randomizer;
import com.example.testbot.utils.ZodiacSign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class CommandHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBot.class);

    private final HoroscopeService horoscopeService;

    private final UserService userService;


    static final String HELP_TEXT = "This bot is created to demonstrate Spring capabilities.\n\n" +
            "Available commands list:\n\n" +
            "Type /start to see a welcome message\n\n" +
            "Type /random10 to get random number from 0 to 10\n\n" +
            "Type /yesorno to get random YES or NO\n\n" +
            "Type /horoscope to get today's horoscope\n\n" +
            "Type /unsubscribe to unsubscribe from horoscope\n\n" +
            "Type /help to see this message again";

    @Autowired
    public CommandHandler(HoroscopeServiceImpl zodiacService, UserServiceImpl userService) {
        this.horoscopeService = zodiacService;
        this.userService = userService;
    }


    public SendMessage handleTextMessage(Update update) {

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

            case "/yesorno":
                LOGGER.info("RANDOMB command from user: {}", userName);
                String randomBoolean = Randomizer.getRandomBoolean() ? "Ответ: ДА!" : "Ответ: НЕТ!";
                return new SendMessage(String.valueOf(chatId), randomBoolean);

            case "/horoscope":
                LOGGER.info("ZODIAC command from user: {}", userName);
                return getHoroscope(chatId);

            case "/unsubscribe":
                LOGGER.info("UNSUBSCRIBE command from user: {}", userName);
                return unsubscribe(chatId);

            default:
                LOGGER.info("UNKNOWN command from user: {}", userName);
                return new SendMessage(String.valueOf(chatId), "Неизвестная команда!");
        }

    }

//    public EditMessageText handleCallbackQuery(Update update) {
//
//        LOGGER.info("Started handling CallbackQuery: {} from user: {}", update.getCallbackQuery().getData(),
//                update.getCallbackQuery().getFrom());
//        String callbackData = update.getCallbackQuery().getData();
//        long messageId = update.getCallbackQuery().getMessage().getMessageId();
//        long chatId = update.getCallbackQuery().getMessage().getChatId();
//        EditMessageText editMessageText = new EditMessageText();
//        editMessageText.setChatId(chatId);
//        editMessageText.setMessageId((int) messageId);
//
//
//        Optional<ZodiacSign> sign = zodiacService.findByName(callbackData);
//        if (sign.isPresent()) {
//            editMessageText.setText(sign.get().getPrediction());
//        } else {
//            editMessageText.setText("Ошибка сервиса!");
//        }
//        return editMessageText;
//    }

    public BotApiMethod<? extends Serializable> handleCallbackQuery(Update update) {

        String callbackData = update.getCallbackQuery().getData();
        String userName = getUserNameFromUpdate(update);
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        LOGGER.info("Started handling CallbackQuery: {} from user: {}", callbackData,
                userName);

        ZodiacSign[] signs = ZodiacSign.values();
        if (Arrays.stream(signs).anyMatch(s -> s.name().equals(callbackData))) {
            userService.addUser(new com.example.testbot.entities.User(chatId, userName, callbackData, false));
            return subscriptionAsk(chatId);
        }

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId((int) messageId);

        if (callbackData.equals("YES_SUBSCRIPTION")) {
            Optional<com.example.testbot.entities.User> updateUser = userService.getUser(chatId);
            if (updateUser.isPresent()) {
                updateUser.get().setSubscription(true);
                userService.addUser(updateUser.get());
                Optional<ZodiacSign> sign = horoscopeService.findByName(updateUser.get().getZodiac());
                if (sign.isPresent()) {
                    editMessageText.setText(sign.get().getPrediction());
                } else {
                    editMessageText.setText("Ошибка сервиса!");
                }
            } else {
                editMessageText.setText("Ошибка сервиса!");
            }
            LOGGER.info("New horoscope subscription for username: {}", userName);
            return editMessageText;
        }

        if (callbackData.equals("NO_SUBSCRIPTION")) {
            Optional<com.example.testbot.entities.User> updateUser = userService.getUser(chatId);
            if (updateUser.isPresent()) {
                updateUser.get().setSubscription(false);
                userService.addUser(updateUser.get());
                Optional<ZodiacSign> sign = horoscopeService.findByName(updateUser.get().getZodiac());
                if (sign.isPresent()) {
                    editMessageText.setText(sign.get().getPrediction());
                } else {
                    editMessageText.setText("Ошибка сервиса!");
                }
            } else {
                editMessageText.setText("Ошибка сервиса!");
            }
            return editMessageText;
        } else {
            editMessageText.setText("Ошибка сервиса!");
            return editMessageText;
        }
    }

    private SendMessage getHoroscope(long chatId) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String text = "Выберите Ваш знак зодика:";
        sendMessage.setText(text);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine3 = new ArrayList<>();

        ZodiacSign[] zodiacSigns = horoscopeService.getUpdate();

        for (int i = 0; i < zodiacSigns.length; i++) {
            ZodiacSign sign = zodiacSigns[i];
            List<InlineKeyboardButton> row = null;
            if (i < 4) {
                row = rowInLine1;
            }
            if (i >= 4 && i < 8) {
                row = rowInLine2;
            }
            if (i >= 8) {
                row = rowInLine3;
            }
            row.add(new InlineKeyboardButton(sign.getTitle(), null, sign.name(),
                    null, null, null,
                    null, null, null));
        }

        rowsInLine.add(rowInLine1);
        rowsInLine.add(rowInLine2);
        rowsInLine.add(rowInLine3);

        markupInLine.setKeyboard(rowsInLine);
        sendMessage.setReplyMarkup(markupInLine);
        return sendMessage;
    }

    private SendMessage subscriptionAsk(long chatId) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String text = "Хотите подписаться на рассылку (ежедневно 7:15)?";
        sendMessage.setText(text);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine1 = new ArrayList<>();
        rowInLine1.add(new InlineKeyboardButton("Да", null, "YES_SUBSCRIPTION",
                null, null, null,
                null, null, null));
        rowInLine1.add(new InlineKeyboardButton("Нет", null, "NO_SUBSCRIPTION",
                null, null, null,
                null, null, null));
        rowsInLine.add(rowInLine1);
        markupInLine.setKeyboard(rowsInLine);
        sendMessage.setReplyMarkup(markupInLine);
        return sendMessage;
    }

    private SendMessage unsubscribe(long chatId) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String text = "Вы отписались от рассылки гороскопа!";
        sendMessage.setText(text);
        userService.unsubscribe(chatId);
        return sendMessage;
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
