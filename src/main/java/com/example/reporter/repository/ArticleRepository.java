package com.example.reporter.repository;


import com.example.reporter.entity.Article;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    @Query("from Article as a where a.text IS NULL OR a.text = ''")
    List<Article> findEmpty();

    // @Query("from Article as a ORDER BY a.id LIMIT :length OFFSET :index")
    // List<Article>getChunk(@Param("length") int lenght, @Param("offset") int index);
    @Query("from Article as a where a.text IS NULL OR a.text = '' ORDER BY a.id ASC  ")
    List<Article> getChunk(PageRequest pageable);

    Article findById(int id);

    @Modifying
    @Query(" update Article a set a.text=:text where a.id=:id ")
    @Transactional
    void updateText(@Param("text") String myText, @Param("id") Integer myId);
}
