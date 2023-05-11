package ru.itmo.wp.web.page;

import com.google.common.base.Strings;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class Page {
    protected final UserService userService = new UserService();
    protected HttpServletRequest request;

    private void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }

    void before(HttpServletRequest request, Map<String, Object> view) {
        this.request = request;
        putMessage(view);
        putUser(view);

        view.put("userCount", userService.findCount());
    }

    void after(HttpServletRequest request, Map<String, Object> view) {
    }

    private void putUser(Map<String, Object> view) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            view.put("user", user);
        }
    }

    private void putMessage(Map<String, Object> view) {
        String message = (String) request.getSession().getAttribute("message");
        if (!Strings.isNullOrEmpty(message)) {
            view.put("message", message);
            request.getSession().removeAttribute("message");
        }
    }

    private void setMessage(String message) {
        request.getSession().setAttribute("message", message);
    }

    private void setUser(User user) {
        request.getSession().setAttribute("user", user);
    }

    private User getUser(User user) {
        return (User) request.getSession().getAttribute("user");
    }
}
