package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.Post;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.PsqlStore;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;
import ru.job4j.quartz.AlertRabbit;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDateTime;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Администратор
 */
public class SqlRuParse implements Parse {

    private final DateTimeParser dateTimeParser;

    public SqlRuParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    public static void main(String[] args) throws Exception {
/*        SqlRuParse sqlRuParse = new SqlRuParse(new SqlRuDateTimeParser());
        List<Post> posts = sqlRuParse.list("https:/" + "/www.sql.ru/forum/job-offers/");
        posts.forEach(System.out::println);
        Post post = sqlRuParse.detail("https://www.sql.ru/forum/1339076/razrabotka-veb-kraulera");
        System.out.println(post);*/
        final Properties cfg = AlertRabbit.readProperties();
        PsqlStore psqlStore = new PsqlStore(cfg);
        Post post = new Post("name3", "link3", "text3", LocalDateTime.now());
        psqlStore.save(post);
        System.out.println(psqlStore.findById(2));
        System.out.println(psqlStore.getAll());
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

    @Override
    public List<Post> list(String link) {
        List<Post> posts = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
            return posts;
        }
        Elements row = doc.select(".sort_options");
        String textPages = row.get(1).children().get(0).children().get(0).children().get(0).text();
        String[] s = textPages.split(" ");
        int numberOfPages = Integer.parseInt(s[s.length - 1]);

        for (int page = 1; page < numberOfPages; page++) {
            String fullAddress = link + page;
            try {
                doc = Jsoup.connect(fullAddress).get();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            row = doc.select(".forumTable");
            Elements postChildrens = row.get(0).children().get(0).children();
            for (int i = (page == 1 ? 4 : 1); i < postChildrens.size(); i++) {
                Element element = postChildrens.get(i);
                String postLink = element.children().get(1).children().get(0).attr("href");
                posts.add(detail(postLink));
            }
        }
        return posts;
    }

    @Override
    public Post detail(String link) {
        Document doc = null;
        try {
            doc = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Elements row = doc.select(".msgTable");
        Element td = row.get(0);
        String title = td.children().get(0)
                .children().get(0).text();
        String dateText = td.children().get(0)
                .children().get(2).text().trim();
        String[] split = dateText.split(" ");
        if (split[0].equals("сегодня,") || split[0].equals("вчера,")) {
            dateText = split[0].concat(" ")
                    .concat(split[1]);
        } else {
            dateText = split[0].concat(" ")
                    .concat(split[1]).concat(" ")
                    .concat(split[2]).concat(" ")
                    .concat(split[3]);
        }
        String description = td.children().get(0)
                .children().get(1)
                .children().get(1).text();
        return new Post(title, link, description, dateTimeParser.parse(dateText));
    }
}