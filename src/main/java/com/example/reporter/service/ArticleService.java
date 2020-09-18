package com.example.reporter.service;

import com.example.reporter.dto.ArticleDto;
import com.example.reporter.entity.Article;
import org.springframework.stereotype.Service;

@Service
public interface ArticleService {
    void save(ArticleDto articleDto);
}
