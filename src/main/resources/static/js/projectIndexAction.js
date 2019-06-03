$(function() {
    $(".leftnav h2").click(function() {
        $(this).next().slideToggle(200);
        $(this).toggleClass("on");
    })
    $(".leftnav ul li a").click(function() {
        $("#a_leader_txt").text($(this).text());
        $(".leftnav ul li a").removeClass("on");
        $(this).addClass("on");
    })
});

/* 清空指定session */
function removeSessionByDataSource() {
    var url = ctxPath + "manage/removeSessionByDataSource";
    var params = {
        "dataSource": null,
        "time": new Date()
    };
    $.getJSON(url, params, function (data) {
        if (data.status == 0) {
            alert(data.message);
        }
    });
}

/* 登出 */
function logout() {
    if (confirm("确定退出登录?")) {
        var url = ctxPath + "manage/logout";
        window.location.href = url;
    }
}