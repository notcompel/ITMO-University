<#import "./common.ftlh" as c>

<@c.page>
    <div class="datatable">
        <div class="caption">Submissions</div>
        <table>
            <thead>
            <tr>
                <th>№</th>
                <th>Id</th>
                <th>Handle</th>
                <th>Name</th>
            </tr>
            </thead>
            <tbody>
             <#list users as u>
                <tr>
                    <td>${u_index + 1}</td>
                    <td>${u.id}</td>
                    <td><#if user?? && user.id == u.id>*</#if>
                        <@c.userlink u/></td>
                    <td>${u.name}</td>
                </tr>
             </#list>
            </tbody>
        </table>
    </div>
</@c.page>

