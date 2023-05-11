<#import "commons.ftlh" as c>

<@c.page>
    <#if user??>
    id: ${user.id}
    creation time: ${user.creationTime}
    login: ${user.login}
    <#else>
        No such user
    </#if>
</@c.page>
