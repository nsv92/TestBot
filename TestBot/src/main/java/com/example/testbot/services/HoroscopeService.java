package com.example.testbot.services;

import com.example.testbot.utils.ZodiacSign;

import java.util.Optional;


public interface HoroscopeService {

    ZodiacSign[] getUpdate();

    Optional<ZodiacSign> findByName(String name);
}
