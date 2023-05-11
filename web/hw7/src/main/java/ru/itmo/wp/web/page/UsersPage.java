package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/** @noinspection unused*/
public class UsersPage {
    private final UserService userService = new UserService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }

    private void findAll(HttpServletRequest request, Map<String, Object> view) {
        view.put("users", userService.findAll());
    }

    private void findUser(HttpServletRequest request, Map<String, Object> view) {
        view.put("user",
                userService.find(Long.parseLong(request.getParameter("userId"))));
    }

    private void editAdmin(HttpServletRequest request, Map<String, Object> view) {
        long id = Long.parseLong(request.getParameter("id"));
        boolean val = Boolean.parseBoolean(request.getParameter("value"));
        User user = (User) request.getSession().getAttribute("user");
        if (!user.isAdmin()) {
            return;
        }
        if (user.getId() == id) {
            user.setAdmin(!user.isAdmin());
            request.getSession().setAttribute("user", user);
        }
        userService.editAdmin(id, val);
    }
}
