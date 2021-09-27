package ru.job4j.grabber.utils;

import java.time.LocalDateTime;

/**
 * @author Администратор
 */
public interface DateTimeParser {

    /**
     * @param parse parse
     * @return return
     */
    LocalDateTime parse(String parse);
}