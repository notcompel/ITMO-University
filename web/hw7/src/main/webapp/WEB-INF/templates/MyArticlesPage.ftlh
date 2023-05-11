<#-- @ftlvariable name="userArticles" type="ru.itmo.wp.model.domain.Article[]" -->

<#import "commons.ftlh" as c>

<@c.page>
    <div class="articles datatable">
        <div class="caption">Article</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Title</th>
                <th>Hidden</th>
            </tr>
            </thead>
            <tbody>
            <#list userArticles as article>
                <tr>
                    <td>${article.id}</td>
                    <td>${article.title}</td>
                    <td class="article_hidden">
                        <#if article.hidden>
                            <button data-articleId=${article.id} type="submit">Show</button>
                        <#else>
                            <button data-articleId=${article.id} type="submit">Hidden</button>
                        </#if>

                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
    <script>
        $(function () {
            $(".articles button").click(function() {
                const id = $(this).attr("data-articleId");
                if ($(this).text().trim() === "Show") {
                    ajax(
                        {
                            action: "editHidden",
                            id: id,
                            value: false
                        },
                        (response) => {
                            $(this).text("Hidden")
                        }
                    )
                } else {
                    ajax(
                        {
                            action: "editHidden",
                            id: id,
                            value: true
                        },
                        (response) => {$(this).text("Show")}
                    )
                }
            });
        })
    </script>
</@c.page>
