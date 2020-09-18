package com.example.reporter;

import com.example.reporter.dto.ArticleDto;
import com.example.reporter.service.ArticleService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class Main {
    private static final String URL = "https://reporter-ua.com";
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ArticleService service;


    @Scheduled(fixedRate = 30000000)
    public void getElements() throws IOException {
        Document doc = Jsoup.connect("https://reporter-ua.com/world-news").get();

        Elements elements = doc.select(".pager-item>a");
        for (Element link : elements) {
            String href = link.attr("href");
            Document page = Jsoup.connect(URL + href).get();
            Elements items = page.select(".e-news-item-title");
            Elements dates = page.select(".post-date");
            Elements images = page.select(".e-news-item-img > a > img");
            Elements shortDescription = page.select(".e-news-item-text");
            // Elements ref = page.select(".e-news-item-title");

            for (int j = 0; j < items.size(); j++) {

                String itemHref = items.get(j).attr("href");
                System.out.println("link: " + items.get(j).attr("href"));
                String heading = items.get(j).text();
                System.out.println("Text: " + heading);
                String date = dates.get(j).text();
                System.out.println("date: " + date);
                System.out.println("image" + images.get(j));
                String description = shortDescription.get(j).text();
                System.out.println("description:" + description);


                Elements itemLink = page.select(".e-news-item-title>a");
                System.out.println(itemLink.get(j).attr("LINK: "+ "href"));
                ArticleDto articleDto = new ArticleDto(heading,description,date);
service.save(articleDto);
                //  System.out.println("ref:"+ ref.get(j).attr("href"));
                //     System.out.println("LINK:"+links.get(j).attr("href"));

//                   Document articles = Jsoup.connect(URL + itemHref).get();
//                    Elements source = articles.select(".field-item > p:nth-child(1) > strong:nth-child(1) > a:nth-child(1)");
//                    System.out.println("source"+source.get(j).text());
            }
        }
    }
}

