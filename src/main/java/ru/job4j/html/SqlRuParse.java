package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        String address = "https://www.sql.ru/forum/job-offers/";
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
}