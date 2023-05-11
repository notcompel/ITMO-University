window.notify = function (message) {
    $.notify(message, {
        position: "right bottom",
        className: "success"
    });
}

window.ajax = function (data, success = (response) => {}) {
    $.ajax({
        type: "POST",
        dataType: "json",
        data: data,
        success: function (response) {
            success(response)
        }
    });
}