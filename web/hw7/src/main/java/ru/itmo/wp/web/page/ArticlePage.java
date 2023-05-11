package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.model.service.UserService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ArticlePage {
    private final ArticleService articleService = new ArticleService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }

    private void send(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        if (request.getSession().getAttribute("user") == null) {
            throw new ValidationException("You have to login");
        }
        User user = (User) request.getSession().getAttribute("user");
        Article article = new Article();
        article.setTitle(request.getParameter("title"));
        article.setText(request.getParameter("text"));
        article.setUserId(user.getId());
        articleService.send(article);

        request.getSession().setAttribute("message", "Post has been sent successfully");
        throw new RedirectException("/index");
    }
}
