package com.example.reporter.service;

import com.example.reporter.dto.ArticleDto;
import com.example.reporter.entity.Article;
import com.example.reporter.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService{
    @Autowired
    private ArticleRepository articleRepository;
    public void save(ArticleDto articleDto){
        articleRepository.save(articleDto);
    }
}
