package com.example.reporter;


import com.example.reporter.entity.Article;
import com.example.reporter.repository.ArticleRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Main {
    private static final String URL = "https://reporter-ua.com";
    private static final String WORLD_NEWS = URL + "/world-news";
    private static final String PAGE_PARAM_FORMAT = WORLD_NEWS + "?page=%d";
    private static final String DATE_FORMAT = "dd.MM.yyyy' - 'HH:mm";
    @Value("${reporter.api.number-of-pages}")
    private Integer NumberOfPages;
    @Autowired
    ArticleRepository articleRepository;

    @Scheduled(fixedRate = 30000000)
    @Transactional
    public void getElements2() throws IOException, ParseException {
        for (int i = 0; i < NumberOfPages; i++) {
            Document newsPage = Jsoup.connect(String.format(PAGE_PARAM_FORMAT, i)).get();
            processPage(newsPage);
        }
    }

    private void processPage(Document page) throws ParseException, IOException {
        Elements articleElements = page.select("div.e-news > div.e-news-item");
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        for (Element articleElement : articleElements) {
            String description = null;
            String text = null;
            String source = null;
            String image = articleElement.selectFirst(".e-news-item-img img").attr("src");
            String heading = articleElement.selectFirst(".e-news-item-title a").text();
            String articleUrl = articleElement.selectFirst(".e-news-item-title a").absUrl("href");
            String dateString = articleElement.selectFirst(".post-date").text();
            Date date = dateFormat.parse(dateString);
            String shorDescription = articleElement.selectFirst("div.e-news-item-content > div.e-news-item-text").text();

            //TODO: Get and parse article page
            Document articlePage = Jsoup.connect(articleUrl).get();
            Elements singleArticle = articlePage.select(".field-items");
            description = singleArticle.select(".field-item  strong").text();
            text = singleArticle.select(".field-item p:not(:first-of-type)").text();
            source = singleArticle.select(".field-item > p:nth-child(1) > strong:nth-child(1) > a:nth-child(1)").text();

            Elements category = articlePage.select(".menu li");
          //  String categ = category.select("li.active-trail:nth-child(1) > a:nth-child(1)").text();
            String categ = category.select(".menu li> a").text();

            Article article = new Article();

            article.setDate(date);
            article.setHeading(heading);
            article.setLink(articleUrl);
            article.setShortDescription(shorDescription);
            article.setImageUrl(image);
            article.setSource(source);
            article.setDescription(description);
            article.setText(text);
            article.setCategory(categ);

            articleRepository.save(article);
            System.out.println(article);
        }
    }
}

