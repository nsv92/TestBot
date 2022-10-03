package com.example.testbot.config;

import com.example.testbot.TestBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotInit {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBot.class);

    @Autowired
    private TestBot bot;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(bot);
            LOGGER.info("Bot registration -> success!");
        }
        catch (TelegramApiException e) {
            LOGGER.error("Exception during bot registration: {}", e.getMessage());
            throw  new RuntimeException(e);
        }
    }
}
