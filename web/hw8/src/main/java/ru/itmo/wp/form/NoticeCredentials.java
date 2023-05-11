package ru.itmo.wp.form;

import javax.validation.constraints.*;

@SuppressWarnings("unused")
public class NoticeCredentials {
    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 2, max = 100)
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
