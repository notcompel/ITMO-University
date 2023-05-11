<#-- @ftlvariable name="handle" type="java.lang.String" -->
<#-- @ftlvariable name="user_id" type="java.lang.Long" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#if handle??>
        <#assign u = c.findBy(users, "handle", handle)!/>
    <#elseif user_id??>
        <#assign u = c.findBy(users, "id", user_id)!/>
    </#if>
    <#if u?has_content>
        <div class="userinfo">
            <a href="#" class="user-pic"><img alt="Person picture" src="/img/person.png" ></a>
            <a href="#" class="header">${u.name}</a>
            <ul>
                <li>
                    id: ${u.id}
                </li>
                <li>
                    handle: ${u.handle}
                </li>
                <li>
                    name: ${u.name}
                </li>
                <li>
                    posts: <a href="/posts?user_id=${u.id}">${c.countPosts(u.id)}</a>
                </li>
            </ul>
        </div>
        <div class="arrows">
            <#assign next_u = getNext(u)!/>
            <#assign prev_u = getPrev(u)!/>
            <#if prev_u?has_content>
                <a href="/user?handle=${c.getPrev(users, u).handle}" >←</a>
            <#else> ←
            </#if>
            <#if next_u?has_content>
                <a href="/user?handle=${c.getNext(users, u).handle}" >→</a>
            <#else> →
            </#if>
        </div>
    <#else>
        No such user
    </#if>
</@c.page>

<#function getNext curr>
    <#list users as u>
        <#if u_has_next && u.id == curr.id>
            <#return users[u_index + 1]/>
        </#if>
    </#list>
</#function>

<#function getPrev curr>
    <#list users as u>
        <#if (u_index > 0) && u.id == curr.id>
            <#return users[u_index - 1]/>
        </#if>
    </#list>
</#function>

