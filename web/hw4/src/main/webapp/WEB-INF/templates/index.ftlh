<#import "./common.ftlh" as c>

<@c.page>
    <#list posts?reverse as p>
        <#if p??>
            <@c.post p true/>
        </#if>
    </#list>

</@c.page>

