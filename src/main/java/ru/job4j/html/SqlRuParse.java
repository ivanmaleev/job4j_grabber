package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.Post;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Администратор
 */
public class SqlRuParse {
    /**
     * @param args args
     */
    public static void main(String[] args) throws Exception {
        datesOfFirstFivePages();
        List<Post> vacancies = findVacancies();
        vacancies.forEach(System.out::println);
    }

    private static void datesOfFirstFivePages() throws IOException {
        String address = "https:/" + "/www.sql.ru/forum/job-offers/";
        for (int i = 1; i < 6; i++) {
            String fullAddress = address + i;
            Document doc = Jsoup.connect(fullAddress).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element parent = td.parent();
                System.out.println(parent.children().get(5).text());
            }
        }
    }

    private static List<Post> findVacancies() throws IOException {
        List<Post> vacancies = new ArrayList<>();
        String address = "https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t";
        Document doc = Jsoup.connect(address).get();
        Elements row = doc.select(".msgTable");
        DateTimeParser dateTimeParser = new SqlRuDateTimeParser();
        for (Element td : row) {
            String title = td.children().get(0)
                    .children().get(0).text();
            String dateText = td.children().get(0)
                    .children().get(2).text().trim();
            String[] split = dateText.split(" ");
            if (split[0].equals("сегодня") || split[0].equals("вчера")) {
                dateText = split[0].concat(" ")
                        .concat(split[1]).concat(" ")
                        .concat(split[2]);
            } else {
                dateText = split[0].concat(" ")
                        .concat(split[1]).concat(" ")
                        .concat(split[2]).concat(" ")
                        .concat(split[3]);
            }
            String description = td.children().get(0)
                    .children().get(1)
                    .children().get(1).text();

            vacancies.add(new Post(title, address, description, dateTimeParser.parse(dateText)));
        }
        return vacancies;
    }
}