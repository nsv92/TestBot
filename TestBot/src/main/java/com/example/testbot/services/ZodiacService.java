package com.example.testbot.services;

import com.example.testbot.utils.Parser;
import com.example.testbot.utils.ZodiacSign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface ZodiacService {

    ZodiacSign[] getUpdate();

    Optional<ZodiacSign> findByName(String name);
}
