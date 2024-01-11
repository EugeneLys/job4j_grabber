package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {

    private static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PREFIX = "/vacancies?page=";

    private static final String SUFFIX = "&q=Java%20developer&type=all";

    private final DateTimeParser dateTimeParser;

    private int id;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    private static String retrieveDescription(String link) throws IOException {
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Elements result = document.select(".faded-content__container");
        return result.text();
    }

    @Override
    public List<Post> list(String link) throws IOException {
        List<Post> list = new ArrayList<>();
        int pageNumber = 1;
        while (pageNumber < 6) {
            String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, pageNumber, SUFFIX);
            Connection connection = Jsoup.connect(fullLink);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                String date = row.select("time.basic-date").attr("datetime");
                String vacancyName = titleElement.text();
                String vacancyLink = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                String description = null;
                try {
                    description = retrieveDescription(vacancyLink);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                id++;
                Post post = new Post(id, vacancyName, vacancyLink,
                        description, new HabrCareerDateTimeParser().parse(date));
                list.add(post);
            });
            pageNumber++;
        }
        return list;
    }
}