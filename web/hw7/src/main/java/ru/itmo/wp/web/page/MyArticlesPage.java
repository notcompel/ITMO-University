package ru.itmo.wp.web.page;

import com.google.common.base.Strings;
import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyArticlesPage {
    private final ArticleService articleService = new ArticleService();
    private void action(HttpServletRequest request, Map<String, Object> view) {
        findByUserId(request, view);
    }

    private void findByUserId(HttpServletRequest request, Map<String, Object> view) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            view.put("userArticles",
                    articleService.findByUserId(user.getId()));
        } else {
            List<Article> articles = new ArrayList<>();
            view.put("userArticles", new ArrayList<>());
        }
    }

    private void editHidden(HttpServletRequest request, Map<String, Object> view) {
        long id = Long.parseLong(request.getParameter("id"));
        boolean val = Boolean.parseBoolean(request.getParameter("value"));
        Article article = articleService.find(id);
        User user = (User) request.getSession().getAttribute("user");
        if (article.getUserId() != user.getId()) {
            return;
        }
        articleService.editHidden(id, val);
    }
}
