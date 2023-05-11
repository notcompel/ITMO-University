<#macro page>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>Codeforces</title>
        <link rel="stylesheet" href="/css/normalize.css">
        <link rel="stylesheet" href="/css/style.css">
        <link rel="icon" href="/favicon.ico">
    </head>
    <body>
    <@header/>
    <div class="middle">
        <@sidebar/>
        <main>
            <#nested/>
        </main>
    </div>
    <@footer/>
    </body>
    </html>
</#macro>

<#macro header>
<#-- @ftlvariable name="href" type="java.lang.String" -->
    <header>
        <a href="#" class="logo"><img alt="Codeforces" title="Codeforces"
                                      src="/img/codeforces-logo-with-telegram.png"></a>
        <div class="languages">
            <a href="#"><img alt="In English" title="In English"
                             src="/img/gb.png"></a>
            <a href="#"><img alt="In Russian" title="In Russian"
                             src="/img/ru.png"></a>
        </div>
        <div class="enter-or-register-box">
            <#if user??>
                <@userlink user/>
                |
                <a href="/logout">Logout</a>
            <#else>
                <a href="/enter">Enter</a>
                |
                <a href="/register">Register</a>
            </#if>
        </div>
        <nav>
            <#assign items=[
            {"href": "/index", "name": "index"},
            {"href": "/misc/help", "name": "help"},
            {"href": "/contests", "name": "contests"},
            {"href": "/users", "name": "users"}
            ]/>
            <ul>
                <#list items as item>
                    <li>
                        <#if item.href == href>
                            <a href="${item.href}" class="underlined-link">${item.name}</a>
                        <#else> <a href="${item.href}">${item.name}</a>
                        </#if>
                    </li>
                </#list>
            </ul>
        </nav>
    </header>
</#macro>

<#macro sidebar>
    <aside>
        <#list posts?reverse as p>
            <#if p??>
                <section>
                    <div class="header">Post #${p.id}</div>
                    <div class="body">${p.miniText}
                    </div>
                    <div class="footer">
                        <@c.postlink p "View all"/>
                    </div>
                </section>
            </#if>
        </#list>

    </aside>
</#macro>

<#macro footer>
    <footer>
        2010-2021 &copy; by Mike Mirzayanov
    </footer>
</#macro>

<#macro post p isMini>
<#-- @ftlvariable name="p" type="ru.itmo.web.hw4.model.Post" -->
<#-- @ftlvariable name="isMini" type="java.lang.Boolean" -->
    <article>
        <div class="title">${p.title}</div>
        <div class="information">By ${p.userId} user, 2 days ago</div>
        <div class="body">
            <#if isMini>
                <#if p.text?length &gt; 350>
                    ${p.text?substring(0, 350)}...
                <#else>
                    ${p.text}
                </#if>
            <#else> ${p.text}
            </#if>

        </div>
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
                2 days ago
                <img src="img/comments_16x16.png" title="Comments" alt="Comments"/>
                <a href="#">68</a>
            </div>
        </div>
    </article>
</#macro>

<#macro userlink user>
<#-- @ftlvariable name="user" type="ru.itmo.web.hw4.model.User" -->
    <a href="/user?handle=${user.handle}">${user.name}</a>
</#macro>

<#macro postlink post text>
<#-- @ftlvariable name="post" type="ru.itmo.web.hw4.model.Post" -->
<#-- @ftlvariable name="text" type="java.lang.String" -->
    <a href="/post?post_id=${post.id}">${text}</a>
</#macro>

<#function findBy items key value>
    <#list items as item>
        <#if item[key] == value>
            <#return item/>
        </#if>
    </#list>
</#function>

<#function getNext array curr>
    <#list array as p>
        <#if p_has_next && p.id == curr.id>
            <#return array[p_index + 1]/>
        </#if>
    </#list>
</#function>

<#function getPrev array curr>
    <#list array as p>
        <#if (p_index > 0) && p.id == curr.id>
            <#return array[p_index - 1]/>
        </#if>
    </#list>
</#function>

<#function countPosts user_id>
    <#assign cnt = 0>
    <#list posts as p>
        <#if p?? && p.userId == user_id>
            <#assign cnt = cnt + 1>
        </#if>
    </#list>
    <#return cnt/>
</#function>