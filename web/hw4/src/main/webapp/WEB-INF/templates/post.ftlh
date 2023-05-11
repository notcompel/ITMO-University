<#-- @ftlvariable name="handle" type="java.lang.String" -->
<#-- @ftlvariable name="user_id" type="java.lang.Long" -->
<#-- @ftlvariable name="post_id" type="java.lang.Long" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#if post_id??>
        <#assign p = c.findBy(posts, "id", post_id)!/>
    </#if>
    <#if p?has_content>
        <@c.post p false/>
        <div class="arrows">
            <#assign next_p = getNext(p)!/>
            <#assign prev_p = getPrev(p)!/>
            <#if prev_p?has_content>
                <a href="/post?post_id=${c.getPrev(posts, p).id}" >←</a>
            <#else> ←
            </#if>
            <#if next_p?has_content>
                <a href="/post?post_id=${c.getNext(posts, p).id}" >→</a>
            <#else> →
            </#if>
        </div>
    <#else>
        No such post
    </#if>
</@c.page>

<#function getNext curr>
    <#list posts as p>
        <#if p_has_next && p.id == curr.id>
            <#return posts[p_index + 1]/>
        </#if>
    </#list>
</#function>

<#function getPrev curr>
    <#list posts as p>
        <#if (p_index > 0) && p.id == curr.id>
            <#return posts[p_index - 1]/>
        </#if>
    </#list>
</#function>

