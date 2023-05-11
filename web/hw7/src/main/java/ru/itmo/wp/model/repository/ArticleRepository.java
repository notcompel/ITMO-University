package ru.itmo.wp.model.repository;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;

import java.util.List;

public interface ArticleRepository {
    void save(Article article);
    List<Article> findAll();
    Article find(long id);

    List<Article> findByUserId(long userId);

    void editHidden(long id, boolean val);
}
