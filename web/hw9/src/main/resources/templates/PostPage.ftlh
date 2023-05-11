<#-- @ftlvariable name="post" type="ru.itmo.wp.domain.Post" -->
<#-- @ftlvariable name="comments" type="ru.itmo.wp.domain.Comment[]" -->
<#-- @ftlvariable name="comment" type="ru.itmo.wp.domain.Comment" -->

<#import "commons.ftlh" as c>

<@c.page>
    <#if post??>
        <@c.post post=post/>
        <#if user??>
            <div class="comment">
                <div class="body">
                    <form method="post" action="">
                        <input type="hidden" name="action" value="writeComment"/>
                        <div class="field">
                            <div class="value">
                                <textarea id="text" name="text" rows="2" cols="20">${comment.text!}</textarea>
                            </div>
                            <@c.error "comment.text"/>
                        </div>
                        <div class="button-field">
                            <input type="submit" value="Write a comment">
                        </div>
                    </form>
                </div>
            </div>
        </#if>
        <div class="comments">
            <div class="title">Comments:</div>
            <ul>
                <#list post.comments as comment>
                    <li>
                        <p style="color: blueviolet"><b>${comment.user.login}</p>
                        <p>${comment.text}</p>
                        <p>${comment.creationTime}</p>
                    </li>
                </#list>
            </ul>
        </div>
    <#else>
        No such post
    </#if>

</@c.page>
