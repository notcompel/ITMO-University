<#-- @ftlvariable name="user_id" type="java.lang.Long" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#if user_id??>
        <#list posts?reverse as p>
            <#if p?? && p.userId == user_id>
                <@c.post p true/>
            </#if>
        </#list>
    <#else> No such user
    </#if>
</@c.page>

