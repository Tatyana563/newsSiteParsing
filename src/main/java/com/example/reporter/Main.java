package com.example.reporter;


import com.example.reporter.entity.Article;
import com.example.reporter.repository.ArticleRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class Main {
    private static final String URL = "https://reporter-ua.com";
    private static final String WORLD_NEWS = URL + "/world-news";
    private static final String PAGE_PARAM_FORMAT = WORLD_NEWS + "?page=%d";
    private static final String DATE_FORMAT = "dd.MM.yyyy' - 'HH:mm";
    String articleUrl;
    String description;
    String text;
    String source;
    @Value("${reporter.api.number-of-pages}")
    private Integer NumberOfPages;
    @Value("${reporter.api.chunk-size}")
    private Integer chunkSize;
    @Autowired
    ArticleRepository articleRepository;

    @Scheduled(fixedRate = 30000000)
    @Transactional
    public void getElements2() throws IOException, ParseException {
        for (int i = 0; i < NumberOfPages; i++) {
            Document newsPage = Jsoup.connect(String.format(PAGE_PARAM_FORMAT, i)).get();
            processPage(newsPage);
            thread.start();
        }
    }

    private void processPage(Document page) throws ParseException, IOException {
        Elements articleElements = page.select("div.e-news > div.e-news-item");
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        for (Element articleElement : articleElements) {
            Article article = new Article();
            String image = articleElement.selectFirst(".e-news-item-img img").attr("src");
            String heading = articleElement.selectFirst(".e-news-item-title a").text();
            articleUrl = articleElement.selectFirst(".e-news-item-title a").absUrl("href");
            String dateString = articleElement.selectFirst(".post-date").text();
            Date date = dateFormat.parse(dateString);
            String shorDescription = articleElement.selectFirst("div.e-news-item-content > div.e-news-item-text").text();
            article.setImageUrl(image);
            article.setDate(date);
            article.setHeading(heading);
            article.setLink(articleUrl);
            article.setShortDescription(shorDescription);
            articleRepository.save(article);
        }
    }
    //TODO: Get and parse article page

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                processArticle();
                Thread.sleep(30000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    public void processArticle() throws IOException {
        List<Article> uncompleted = articleRepository.findEmpty();
        if (!uncompleted.isEmpty()) {
            List<Article> chunk = articleRepository.getChunk(PageRequest.of(0, 5));
            for (int i = 0; i < chunk.size(); i++) {
                Document articlePage = Jsoup.connect(chunk.get(i).getLink()).get();
                Elements singleArticle = articlePage.select(".field-items");

                description = singleArticle.select(".field-item  strong").text();
                text = singleArticle.select(".field-item p:not(:first-of-type)").text();
                source = singleArticle.select(".field-item > p:nth-child(1) > strong:nth-child(1) > a:nth-child(1)").text();
                Optional<Article> item = articleRepository.findById(chunk.get(i).getId());
                articleRepository.updateText(text, item.get().getId());
                Elements category = articlePage.select(".menu li > a");
                //  String categ = category.select("li.active-trail:nth-child(1) > a:nth-child(1)").text();
                String categ = category.select(".menu li> a").text();
            }
        }
    }
}

