package com.example.testbot.utils;

import com.example.testbot.TestBot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class Parser {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBot.class);

    private final String url = "https://europaplus.ru/";

    public ZodiacSign[] parse() {
        LOGGER.info("Starting parsing zodiac signs");
        ZodiacSign[] zodiacSigns = ZodiacSign.values();

        try {
            Document doc = Jsoup.connect(url).get();
            LOGGER.info("Parser connected successfully to url: {}", url);
            Elements e = doc.select("p.horoscope-card__forecast");
            if (e.isEmpty()) {
                LOGGER.info("Parser couldn't find required elements!");
                Arrays.stream(zodiacSigns).forEach(s -> s.setPrediction("Сервис временно недоступен!"));
            } else {
                for (int i = 0; i < zodiacSigns.length; i++) {
                    int finalI = i;
                    Optional<Element> prediction = e.stream().filter(element -> element.text().split(",")[0]
                            .equals(zodiacSigns[finalI].getParseTitle())).findFirst();
                    if (prediction.isPresent()) {
                        zodiacSigns[i].setPrediction(prediction.get().text());
                    } else {
                        zodiacSigns[i].setPrediction("Ошибка сервиса!");
                        LOGGER.info("Parser got NULL trying to parse prediction to {} sign!", zodiacSigns[i].getTitle());
                    }
                }
            }

        } catch (IOException e) {
            LOGGER.error("Exception during parser connection attempt to url {} : {}", url, e.getMessage());
            throw new RuntimeException(e);
        }
        LOGGER.info("Parsed successfully!");
        return zodiacSigns;
    }
}
