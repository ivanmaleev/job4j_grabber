package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {

    private static final Map<String, String> MONTHS = Map.ofEntries(Map.entry("янв", "Jan"), Map.entry("фев", "Feb"), Map.entry("мар", "Mar"),
            Map.entry("апр", "Apr"), Map.entry("май", "May"), Map.entry("июн", "Jun"), Map.entry("июл", "Jul"), Map.entry("авг", "Aug"),
            Map.entry("сен", "Sep"), Map.entry("окт", "Okt"), Map.entry("ноя", "Nov"), Map.entry("дек", "Dec"));

    @Override
    public LocalDateTime parse(String parse) {
        if (parse.startsWith("сегодня") || parse.startsWith("вчера")) {
            DateTimeFormatter dateTimeFormatterToday = DateTimeFormatter.ofPattern("d MMM yy", Locale.ENGLISH);
            LocalDateTime now = LocalDateTime.now();
            if (parse.startsWith("сегодня")) {
                String today = now.format(dateTimeFormatterToday);
                parse = parse.replace("сегодня", today);
            } else {
                now = now.minusDays(1);
                String today = now.format(dateTimeFormatterToday);
                parse = parse.replace("вчера", today);
            }
        } else {
            for (Map.Entry<String, String> entry : MONTHS.entrySet()) {
                String key = entry.getKey();
                if (parse.contains(key)) {
                    parse = parse.replace(key, entry.getValue());
                    break;
                }
            }
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d MMM yy, HH:mm", Locale.ENGLISH);
        return LocalDateTime.parse(parse, dateTimeFormatter);
    }

    public static void main(String[] args) {
        SqlRuDateTimeParser sqlRuDateTimeParser = new SqlRuDateTimeParser();
        System.out.println(sqlRuDateTimeParser.parse("02 апр 19, 22:33"));
        System.out.println(sqlRuDateTimeParser.parse("сегодня, 22:33"));
        System.out.println(sqlRuDateTimeParser.parse("вчера, 10:35"));
    }
}