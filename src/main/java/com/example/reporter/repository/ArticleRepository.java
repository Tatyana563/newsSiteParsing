package com.example.reporter.repository;


import com.example.reporter.entity.Article;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    @Query("from Article as a where a.postProcessed = false ORDER BY a.id ASC  ")
    List<Article> getChunk(PageRequest pageable);

    boolean existsAllByLink(String link);
}
