package com.example.testbot.utils;

import java.util.Random;

public class Randomizer {

//    возвращает случайный инт от 0 до 10
    public static int getRandomInt() {
        return (int) (Math.random() * 11);
    }

//    возвращает случайный boolean
    public static boolean getRandomBoolean() {
        return getRandomInt() % 2 == 0;
    }

}
