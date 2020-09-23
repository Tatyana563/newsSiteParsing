package com.example.reporter;


import com.example.reporter.entity.Article;
import com.example.reporter.repository.ArticleRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static final String URL = "https://reporter-ua.com";
    private static final String WORLD_NEWS = URL + "/world-news";
    private static final String PAGE_PARAM_FORMAT = WORLD_NEWS + "?page=%d";
    private static final String DATE_FORMAT = "dd.MM.yyyy' - 'HH:mm";
//variant1 - see application.properties
//    @Value("${reporter.api.number-of-pages}")
//    private Integer numberOfPages;
//    @Value("${reporter.api.chunk-size}")
//    private Integer chunkSize;
//    @Value("${reporter.thread-pool.pool-size}")
//    private Integer threadPoolSize;
    ConfigProperties config = new ConfigProperties();
    @Autowired
    ArticleRepository articleRepository;

    @Scheduled(fixedRate = 300000)
    @Transactional
    public void getElements() throws IOException, ParseException {
        LOG.info("Получаем список новостей...");
        for (int i = 0; i < config.getChunk(); i++) {
            LOG.info("Получаем список новостей - страница {}", i);
            Document newsPage = Jsoup.connect(String.format(PAGE_PARAM_FORMAT, i)).get();
            processPage(newsPage);
        }
        LOG.info("Список новостей получен");
    }

    @Scheduled(initialDelay = 20000, fixedRate = 30000000)
    @Transactional
    public void getAdditionalArticleInfo() throws  InterruptedException {
        LOG.info("Получаем дополнитульную информацию о статьях...");
        ExecutorService executorService = Executors.newFixedThreadPool(config.getPoolSize());
        PageRequest pageRequest = PageRequest.of(0, config.getChunk());
        List<Article> chunk = articleRepository.getChunk(pageRequest);
        LOG.info("Получили из базы {} статей", chunk.size());
        while (!chunk.isEmpty()) {
            CountDownLatch latch = new CountDownLatch(chunk.size());
            for (Article article : chunk) {
                executorService.execute(new ArticleUpdateTask(articleRepository, article, latch));
            }
            LOG.info("Задачи запущены, ожидаем завершения выполнения...");
            latch.await();
            LOG.info("Задачи выполнены, следующая порция...");
            chunk = articleRepository.getChunk(pageRequest);
        }


        executorService.shutdown();
    }

    private void processPage(Document page) throws ParseException{
        Elements articleElements = page.select("div.e-news > div.e-news-item");
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        for (Element articleElement : articleElements) {
            String articleUrl = articleElement.selectFirst(".e-news-item-title a").absUrl("href");
            LOG.info("Рассматриваем новость: {}", articleUrl);
            if(!articleRepository.existsAllByLink(articleUrl)) {
                LOG.info("Новость не найдена, сохраняем");
                Article article = new Article();
                String image = articleElement.selectFirst(".e-news-item-img img").attr("src");
                String heading = articleElement.selectFirst(".e-news-item-title a").text();
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
            else {
                LOG.info("Новость найдена, пропускаем");
            }
        }
    }
}

