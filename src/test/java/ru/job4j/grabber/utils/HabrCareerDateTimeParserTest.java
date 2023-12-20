package ru.job4j.grabber.utils;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;


class HabrCareerDateTimeParserTest {

    @Test
    void whenCorrectParsingManually() {
        HabrCareerDateTimeParser parser = new HabrCareerDateTimeParser();
        LocalDateTime result = parser.parse("2023-12-20T10:15:30+03:00");
        LocalDateTime expected = LocalDateTime.parse("2023-12-20T10:15:30");
        assertEquals(expected, result);
    }
}