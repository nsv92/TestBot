package com.example.testbot.utils;

public enum ZodiacSign {

    ARIES ("Овен", "Овны"),
    TAURUS ("Телец", "Тельцы"),
    GEMINI ("Близнецы", "Близнецы"),
    CANCER ("Рак", "Раки"),
    LEO ("Лев", "Львы"),
    VIRGO ("Дева", "Девы"),
    LIBRA ("Весы", "Весы"),
    SCORPIO("Скорпион", "Скорпионы"),
    SAGITTARIUS("Стрелец", "Стрельцы"),
    CAPRICORN("Козерог", "Козероги"),
    AQUARIUS("Водолей", "Водолеи"),
    PISCES("Рыбы", "Рыбы")
    ;

    ZodiacSign(String title, String parseTitle) {
        this.title = title;
        this.parseTitle = parseTitle;
    }

    private final String title;

    private final String parseTitle;

    private String prediction;

    public String getTitle() {
        return title;
    }

    public String getParseTitle() {
        return parseTitle;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }
}
