<#import "commons.ftlh" as c>

<@c.page>
    <div class="articles">
        <template>
            <article>
                <div class="title"></div>
                <div class="text"></div>
                <ul class="attachment">
                    <li>Announcement of <a href="#">Codeforces Round #510 (Div. 1)</a></li>
                </ul>
                <div class="footer">
                    <div class="left">
                        <img src="img/voteup.png" title="Vote Up" alt="Vote Up"/>
                        <span class="positive-score">+173</span>
                        <img src="img/votedown.png" title="Vote Down" alt="Vote Down"/>
                    </div>
                    <div class="right">
                        <img src="img/date_16x16.png" title="Publish Time" alt="Publish Time"/>
                        <div class="creationTime"></div>
                        <img src="img/comments_16x16.png" title="Comments" alt="Comments"/>
                        <a href="#">68</a>
                    </div>
                </div>
            </article>
        </template>
        <div class="noData">
                No data
        </div>
    </div>
    <script>
        $(function () {
            const $noData = $(".articles .noData");
            $noData.text("Loading...");

            ajax(
                {
                    action: "findAll"
                },
                (response) => {
                    response["articles"].forEach(function(article) {
                        if (article["hidden"] === false) {
                            $noData.hide();
                            const $tr = $($(".articles template").prop("content")).clone();
                            $tr.find(".title").text(article["title"]);
                            $tr.find(".text").text(article["text"]);
                            $tr.find(".creationTime").text(article["creationTime"]);
                            $tr.find("tr").attr("data-articleId", article["id"]);
                            $(".articles").append($tr);
                        }
                    });
                    $(".articles").click(function() {
                        ajax({
                                action: "findUser",
                                articleId: $(this).attr("data-articleId")
                            },
                            (response) => {
                                notify(response["article"].creationTime);
                            }
                        )
                    });
                });
        })
    </script>
</@c.page>
