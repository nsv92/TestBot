package com.example.testbot.services;


import com.example.testbot.TestBot;
import com.example.testbot.utils.Parser;
import com.example.testbot.utils.ZodiacSign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Service
public class HoroscopeServiceImpl implements HoroscopeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBot.class);

    private final Parser parser = new Parser();

    private ZodiacSign[] zodiacSigns = ZodiacSign.values();

    private LocalDateTime lastUpdateTime;

    @Override
    public ZodiacSign[] getUpdate() {
            isUpToDate();
            return zodiacSigns;
        }

    private void isUpToDate() {

        if (zodiacSigns[0].getPrediction() == null || lastUpdateTime == null) {
            update();
        }

        if (lastUpdateTime.getDayOfYear() < LocalDateTime.now().getDayOfYear()) {
            update();
        }

        if (lastUpdateTime.getDayOfYear() == LocalDateTime.now().getDayOfYear() &&
            lastUpdateTime.getHour() < 2) {
            update();
        }
    }

    private void update() {
        zodiacSigns = parser.parse();
        lastUpdateTime = LocalDateTime.now();
    }

    @Override
    public Optional<ZodiacSign> findByName(String name) {
        return Arrays.stream(zodiacSigns).filter(s -> s.name().equals(name)).findFirst();
    }
}
