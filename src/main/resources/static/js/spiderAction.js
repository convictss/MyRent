$(function () {

    /* 获取正在运行的数据源爬虫 */
    var activeDataSourceUrl = ctxPath + "manage/getActiveDataSource";
    $.getJSON(activeDataSourceUrl, function (data) {
        if (data.activeDataSource != undefined) {
            var $trs = $("table[class='table table-hover text-center']").find("tr:gt(0)");
            $.each(data.activeDataSource, function (index, item) {
                $trs.each(function (ind, tr) {
                    var dataSource = $(tr).find("td:eq(0)").html();
                    if (dataSource == item) {
                        $(tr).find("input[type='button']:eq(-2)").attr("disabled", "disabled");
                        $(tr).find("input[type='button']:eq(-1)").removeAttr("disabled");
                    }
                });
            });
        }
    });

    /* 线程数增加、减少单击事件 */
    $("td[name='threadTD']").each(function () {
        var $tcInp = $(this).find("input:eq(1)");
        $(this).find("input[name='subBtn']").click(function () {
            if (Number($tcInp.val()) > 1) {
                $tcInp.val(Number($tcInp.val()) - 1);
            }
        });
        $(this).find("input[name='addBtn']").click(function () {
            if (Number($tcInp.val()) < 5) {
                $tcInp.val(Number($tcInp.val()) + 1);
            }
        });

    });

    /* 爬虫状态校验定时器参数 */
    var intervalRef;

    /* 遍历[启动][停止]按钮增加事件 */
    $("tr:gt(0)").each(function () {
        var $tr = $(this);
        var dataSource = $tr.find("td:eq(0)").html();
        var params = {
            "dataSource": dataSource,
            "time": new Date()
        };

        // [启动]单击事件
        $tr.find("input[type='button']:eq(-2)").click(function () {
            var $tcInp = $tr.find("td[name='threadTD'] input:eq(1)");
            var $tcInpTip = $tr.find("td[name='threadTD'] strong");
            if ($tcInp.val() == null || $tcInp.val() == "" || isNaN($tcInp.val())) {
                $tcInpTip.html("请输入整数");
                $tcInp.focus();

            } else if ($tcInp.val() > 10 || $tcInp.val() < 1) {
                $tcInpTip.html("请检查整数范围");
                $tcInp.focus();

            } else {
                $tcInpTip.empty();
                params["threadCount"] = $tcInp.val();
                var imgDown = $tr.find("input[name='imgDown']");
                if (imgDown.get(0).checked) {
                    params["imgDown"] = true;
                }else {
                    params["imgDown"] = false;
                }
                var url = ctxPath + "manage/launchSpider";
                $.getJSON(url, params, function (data) {
                    // 修改[启动][停止]按钮的状态
                    $tr.find("input[type='button']:eq(-2)").attr("disabled", "disabled");
                    $tr.find("input[type='button']:eq(-1)").removeAttr("disabled");
                    // 插入返回信息
                    $tr.find("td:eq(1)").html(data.message);
                    $tr.find("td:eq(2)").html("<span>" + data.uuid + "</span>");
                    // 启动爬虫校验器
                    if (data.status == 3) {
                        intervalRef = setInterval(checkSpider, 3000, data.uuid, $tr);
                    }
                });
            }
        });

        // [停止]单击事件
        $tr.find("input[type='button']:eq(-1)").click(function () {
            var url = ctxPath + "manage/suspendSpider";
            $.getJSON(url, params, function (data) {
                clearInterval(intervalRef);
                removeSessionByDataSource(dataSource);
                // 修改[启动][停止]按钮的状态
                $tr.find("input[type='button']:eq(-2)").removeAttr("disabled");
                $tr.find("input[type='button']:eq(-1)").attr("disabled", "disabled");
                // 插入返回信息
                $tr.find("td:eq(1)").html(data.message);
                // 插入跳转日志详情的按钮
                $tr.find("td:eq(2) span").after("&nbsp;<input type=\"button\" class=\"button border-yellow\" value=\"详情\"/>");
                // 为[详情]按钮增加单击事件
                $tr.find("td:eq(2) input[type='button']").click(function () {
                    var uuid = $tr.find("td:eq(2) span").html();
                    var modifyPageUrl = ctxPath + "show/log/modify";
                    var queryUrl = ctxPath + "manage/queryLogByUuid";
                    var params = {
                        "uuid": uuid
                    };
                    $.get(queryUrl, params);
                    window.location.href = modifyPageUrl;
                });
            });
        });

    });

    /*检查爬虫是否停止*/
    function checkSpider(uuid, $tr) {
        var url = ctxPath + "manage/checkSpider";
        var dataSource = $tr.find("td:eq(0)").html();
        var params = {
            "uuid": uuid,
            "time": new Date()
        };
        $.getJSON(url, params, function (data) {
            if (data.status == 0 || data.status == 1) {//爬虫异常或自然停止
                clearInterval(intervalRef);
                // 清空相关的缓存重新渲染数据
                removeSessionByDataSource(dataSource);
                // 修改[启动][停止]按钮的状态
                $tr.find("input[type='button']:eq(-2)").removeAttr("disabled");
                $tr.find("input[type='button']:eq(-1)").attr("disabled", "disabled");
                // 插入返回信息
                if (data.status == 0) {
                    $tr.find("td:eq(1)").html("<span style=\"color:red;\">" + data.message + "</span>");
                } else {
                    $tr.find("td:eq(1)").html(data.message);
                }

                // 插入跳转日志详情的按钮
                $tr.find("td:eq(2) span").after("&nbsp;<input type=\"button\" class=\"button border-yellow\" value=\"详情\"/>");
                // 为[详情]按钮增加单击事件
                $tr.find("td:eq(2) input[type='button']").click(function () {
                    var uuid = $tr.find("td:eq(2) span").html();
                    var modifyPageUrl = ctxPath + "show/log/modify";
                    var queryUrl = ctxPath + "manage/queryLogByUuid";
                    var params = {
                        "uuid": uuid
                    };
                    $.get(queryUrl, params);
                    window.location.href = modifyPageUrl;
                });
            }
        });
    };

    /* 清空指定session */
    function removeSessionByDataSource(dataSource) {
        var url = ctxPath + "manage/removeSessionByDataSource";
        var params = {
            "dataSource": dataSource,
            "time": new Date()
        };
        $.get(url, params);
    }

});