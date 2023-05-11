<#-- @ftlvariable name="users" type="ru.itmo.wp.model.domain.User[]" -->
<#-- @ftlvariable name="user" type="ru.itmo.wp.model.domain.User" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="users datatable">
        <div class="caption">User</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Login</th>
                <#if user?? && user.admin>
                    <th>Admin</th>
                </#if>
            </tr>
            </thead>
            <tbody>
            <template>
                <tr>
                    <td class="user-id"></td>
                    <td class="user_login"></td>
                    <#if user?? && user.admin>
                        <td class="user_admin">
                            <div class="admin-value"></div>
                            <button type="submit"></button>
                        </td>
                    </#if>
                </tr>
            </template>
            <tr class="noData">
                <td colspan="3">
                    No data
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <script>
        $(function () {
            const $noData = $(".users .noData");
            $noData.find("td").text("Loading...");

            ajax(
                {
                    action: "findAll"
                },
                (response) => {
                    response["users"].forEach(function(user) {
                        $noData.hide();
                        const $tr = $($(".users template").prop("content")).clone();
                        $tr.find(".user-id").text(user["id"]);
                        $tr.find(".user_login").text(user["login"]);
                        $tr.find(".user_admin_value").text(user["admin"]);
                        if (user["admin"] === false) {
                            $tr.find("button").text("Enable");
                            $tr.find(".admin-value").text("false");
                        } else {
                            $tr.find("button").text("Disable");
                            $tr.find(".admin-value").text("true");
                        }
                        $tr.find("tr button").attr("data-userId", user["id"]);
                        $(".users tbody").append($tr);
                    });

                    $(".users button").click(function() {
                        const id = $(this).attr("data-userId");
                        if ($(this).text().trim() === "Enable") {
                            ajax(
                                {
                                    action: "editAdmin",
                                    id,
                                    value: true
                                },
                                (response) => {
                                    $(this).text("Disable");
                                    $(this.parentNode).find(".admin-value").text("true");
                                }
                            )
                        } else {
                            ajax(
                                {
                                    action: "editAdmin",
                                    id,
                                    value: false
                                },
                                (response) => {
                                    $(this).text("Enable");
                                    $(this.parentNode).find(".admin-value").text("false");
                                }
                            )
                        }

                    });
                }
            );
        })
    </script>
</@c.page>
