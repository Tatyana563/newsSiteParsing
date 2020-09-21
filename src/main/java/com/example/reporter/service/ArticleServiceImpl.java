package com.example.reporter.service;

import com.example.reporter.dto.ArticleDto;
import com.example.reporter.repository.ArticleDtoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService{
    @Autowired
    private ArticleDtoRepository articleDtoRepository;
    public void save(ArticleDto articleDto){
        articleDtoRepository.save(articleDto);
    }
}
