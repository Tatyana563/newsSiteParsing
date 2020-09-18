package com.example.reporter.repository;

import com.example.reporter.dto.ArticleDto;
import com.example.reporter.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleDto, Integer> {
}
