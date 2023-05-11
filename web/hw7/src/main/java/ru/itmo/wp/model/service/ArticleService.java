package ru.itmo.wp.model.service;

import org.checkerframework.checker.units.qual.A;
import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.repository.ArticleRepository;
import ru.itmo.wp.model.repository.UserRepository;
import ru.itmo.wp.model.repository.impl.ArticleRepositoryImpl;
import ru.itmo.wp.model.repository.impl.UserRepositoryImpl;

import java.util.List;

public class ArticleService {
    private final ArticleRepository articleRepository = new ArticleRepositoryImpl();
    public void send(Article article) {
        articleRepository.save(article);
    }
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article find(long id) {
        return articleRepository.find(id);
    }

    public List<Article> findByUserId(long userId) {
        return articleRepository.findByUserId(userId);
    }

    public void editHidden(long id, boolean val) {
        articleRepository.editHidden(id, val);
    }
}
