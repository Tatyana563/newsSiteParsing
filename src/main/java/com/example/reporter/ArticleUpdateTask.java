package com.example.reporter;

import com.example.reporter.entity.Article;
import com.example.reporter.repository.ArticleRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ArticleUpdateTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleUpdateTask.class);

    private final ArticleRepository articleRepository;
    private final Article article;
    private final CountDownLatch latch;

    public ArticleUpdateTask(ArticleRepository articleRepository, Article article, CountDownLatch latch) {
        this.articleRepository = articleRepository;
        this.article = article;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            String link = article.getLink();
            LOG.info("Запрашиваем страницу {}", link);
            Document articlePage = Jsoup.connect(link).get();

            Elements singleArticle = articlePage.select(".field-items");

            String description = singleArticle.select(".field-item  strong").text();
            String text = singleArticle.select(".field-item p:not(:first-of-type)").text();
            String source = singleArticle.select(".field-item > p:nth-child(1) > strong:nth-child(1) > a:nth-child(1)").text();

            Element category = articlePage.select(".topmenu2 .menu a.active-trail").first();
            String categoryText = category.text();
            LOG.info("Категория '{}'", categoryText);

            article.setDescription(description);
            article.setText(text);
            article.setSource(source);
            article.setCategory(categoryText);
            article.setPostProcessed(true);

            articleRepository.save(article);

            LOG.info("Статья обновлена");
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            latch.countDown();
        }
    }
}
