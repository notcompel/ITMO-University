package ru.itmo.web.hw4.model;

public class Post {
    private final long id;
    private final long userId;
    private final String title;
    private final String text;

    public Post(long id, long userId, String title, String text) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public String getMiniText() {
        if (text.length() > 350)
            return text.substring(0, 350) + "...";
        return text;
    }

    public String getTitle() {
        return title;
    }
}
