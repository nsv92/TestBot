package com.example.testbot.utils;

import com.example.testbot.services.HoroscopeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class ParserTest {

    @Test
    void parse() {
        Parser parser = new Parser();
        ZodiacSign[] signs = parser.parse();
        Arrays.stream(signs).forEach(s -> {
            Assertions.assertNotNull(s.getPrediction());
            Assertions.assertNotEquals(s.getPrediction(), s.getParseTitle());
            System.out.println(s.getPrediction());
        });
    }



    @Test
    void service() {
        HoroscopeServiceImpl zodiacService = new HoroscopeServiceImpl();
        ZodiacSign[] signs = zodiacService.getUpdate();
        Arrays.stream(signs).forEach(s -> System.out.println(s.getPrediction()));
    }
}