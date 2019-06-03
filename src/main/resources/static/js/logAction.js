/*String 判断结尾*/
String.prototype.endWith = function (s) {
    var d = this.length - s.length;
    return (d >= 0 && this.lastIndexOf(s) == d)
};

/*Date 格式化*/
Date.prototype.format = function (format) {
    // eg
    // var et1 = "${session.modifyLog.endTime}";
    // var et2 = new Date(et1);
    // var etVal = et2.format("yyyy-MM-dd hh:mm:ss");
    // $("#endTime").val(etVal);
    var o =
        {
            "M+" : this.getMonth() + 1, // month
            "d+" : this.getDate(), // day
            "h+" : this.getHours(), // hour
            "m+" : this.getMinutes(), // minute
            "s+" : this.getSeconds(), // second
            "q+" : Math.floor((this.getMonth() + 3)  / 3), // quarter
            "S" : this.getMilliseconds() // millisecond
        }
    if (/(y+)/.test(format))
    {
        format = format.replace(RegExp.$1, (this.getFullYear() + "") .substr(4 - RegExp.$1.length));
    }
    for ( var k in o)
    {
        if (new RegExp("(" + k + ")").test(format))
        {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
};

var querySize = 10; // 页显示数据量
var queryResult = null; // 存储根据关键词查找的结果

/* 页面加载时动作 */
$(function () {

    // 回车查询
    $(document).keydown(function (event) {
        if (event.keyCode === 13) {
            $("#queryBtn").click();
        }
    });

    // 监听[查询关键词]下拉框
    $("#queryType").click(function () {
        var queryType = $(this).find("option:selected").val();
        var inputKeyModel = "<input type=\"text\" placeholder=\"请输入UUID（部分|全部）\" id=\"queryKey\" style=\"width:150px; height:30px;\" />";
        var selectKeyModel = "<select id=\"queryKey\" style=\"height: 30px; border-color:#0ae;\">\n" +
                                "<option value=\"0\">异常</option>\n" +
                                "<option value=\"1\">正常结束</option>\n" +
                                "<option value=\"2\">手动结束</option>\n" +
                             "</select>";
        var $queryKey = $("#queryKey");
        var qk_tgn = $queryKey.prop("tagName").toLowerCase();
        if (queryType == "uuid") {
            if (qk_tgn == "select") {
                $queryKey.remove();
                $(this).after(inputKeyModel);
            } else {
                $queryKey.attr("type", "text");
            }

        } else if (queryType.endWith("Time")) {
            if (qk_tgn == "select") {
                $queryKey.remove();
                $(this).after(inputKeyModel);
                $(this).siblings("input").attr("type", "date");
            } else {
                $queryKey.attr("type", "date");
            }

        } else {
            if (qk_tgn == "input") {
                $queryKey.remove();
                $(this).after(selectKeyModel);
            }
        }

    });

    // 全选
    $("#selectAll").click(function () {
        if (this.checked) {
            $("input[name='indexName']").each(function() {
                this.checked = true;
            });
        }else {
            $("input[name='indexName']").each(function() {
                this.checked = false;
            });
        }
    });

});

/* 获取数据 */
function getLogs(pageNum, pageSize) {
    var url = ctxPath + "manage/queryPageLogs";
    var params = {
        "dataSource": dataSource,
        "pageNum": pageNum,
        "pageSize": pageSize
    };
    $.getJSON(url, params, function (data) {
        $("#contentTable tr").siblings().not("tr[id^='fix']").empty();
        showRowData(data);
    })
}

/* 显示行数据、页码等 */
function showRowData(data) {
    $.each(data.logs, function (index, item) {
        var rowData = "<tr>" +
                        "<td><input type=\"checkbox\" name=\"indexName\" />" + (index + 1) + "</td>" +
                        "<td>" + item.uuid + "</td>" +
                        "<td><input type=\"button\" class=\"button border-black copyBtn\" value=\"复制\" data-clipboard-text=\"" + item.uuid + "\"/></td>" +
                        "<td>" + getDateTime(item.startTime) + "</td>" +
                        "<td>" + getDateTime(item.endTime) + "</td>" +
                        "<td>" + item.crawlSecond + "</td>" +
                        "<td>" +
                            "<div class=\"button-group\">" +
                                "<a class=\"button border-main icon-edit\" onclick=\"queryLogByUuid(this)\"> 详情/修改 </a>" +
                                "<a class=\"button border-red icon-trash-o\" href=\"javascript:void(0)\" onclick=\"remove(this)\"> 删除 </a>" +
                            "</div>" +
                        "</td>" +
                      "</tr>";
        $("#contentTable tr:eq(-2)").after(rowData);
    });

    // 显示页码等其他信息
    if (data.hasOwnProperty("pageNum")) {
        var pagePrefix = "<a href=\"javascript:void(0);\" onclick=\"changeFixPage(this)\">";
        var pageSuffix = "</a>";
        var $cur = $(".current");
        $cur.siblings("a").not("[id]").remove();
        $cur.html(data.pageNum);
        if (data.pageCount < 3) {// 页数等于3
            if (data.pageNum == 1) {
                if (data.pageCount == 2) {//当前页=首页
                    $cur.after(pagePrefix + (Number(data.pageNum) + 1) + pageSuffix);
                } else if (data.pageCount > 2) {
                    $cur.after(pagePrefix + (Number(data.pageNum) + 2) + pageSuffix);
                    $cur.after(pagePrefix + (Number(data.pageNum) + 1) + pageSuffix);
                }
            } else if (data.pageNum == data.pageCount) {//当前页=尾页
                if (data.pageCount == 2) {
                    $cur.before(pagePrefix + (Number(data.pageNum) - 1) + pageSuffix);
                } else {
                    $cur.before(pagePrefix + (Number(data.pageNum) - 2) + pageSuffix);
                    $cur.before(pagePrefix + (Number(data.pageNum) - 1) + pageSuffix);
                }
            } else {
                if (data.pageCount == 2) {//当前页=中间页
                    $cur.before(pagePrefix + (Number(data.pageNum) - 1) + pageSuffix);
                } else {
                    $cur.after(pagePrefix + (Number(data.pageNum) + 1) + pageSuffix);
                }
            }

        } else {// 页数大于3
            if (data.pageNum == 1) {// 当前页=首页
                $cur.after(pagePrefix + (Number(data.pageNum) + 2) + pageSuffix);
                $cur.after(pagePrefix + (Number(data.pageNum) + 1) + pageSuffix);
            } else if (data.pageNum == data.pageCount) {//当前页=尾页
                $cur.before(pagePrefix + (Number(data.pageNum) - 2) + pageSuffix);
                $cur.before(pagePrefix + (Number(data.pageNum) - 1) + pageSuffix);
            } else {//当前页=中间页
                $cur.before(pagePrefix + (Number(data.pageNum) - 1) + pageSuffix);
                $cur.after(pagePrefix + (Number(data.pageNum) + 1) + pageSuffix);
            }
        }
        $("#rentsCountVal").val(data.rentsCount);
        $("#pageCountVal").val(data.pageCount);
        $("#pageNumVal").val(data.pageNum);
    }

    // 批量增加复制按钮功能
    var clipboard = new Clipboard(".copyBtn");
    clipboard.on('success', function (e) {
        // console.log(e);
    });
    clipboard.on('error', function (e) {
        // console.log(e);
    });
}

/* 时间戳转换js日期 */
function getDateTime(timeStamp) {
    var date = new Date(timeStamp);
    var yyyy = date.getFullYear() + '-';
    var MM = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    var dd = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' ';
    var h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ":";
    var m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ":";
    var s = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
    return yyyy + MM + dd + h + m + s;
}

/* 跳转前后、首尾、显示页码的页 */
function changeFixPage(ele) {
    var $trs = $("#contentTable tr").siblings().not("tr[id^='fix']");
    var id = ele.id;
    var pageNum = Number($("#pageNumVal").val());
    var pageCount = Number($("#pageCountVal").val());

    // 跳转显示页码的页
    if (id == "") {
        pageNum = Number($(ele).html());
        $trs.remove();
        getLogs(pageNum, querySize);
    }

    // 跳转前后页、首尾页
    else {
        if (id.indexOf("home") === 0) {
            if (pageNum != 1) {
                $trs.remove();
                getLogs(1, querySize);
            }

        } else if (id.indexOf("previous") === 0) {
            if (pageNum > 1) {
                $trs.remove();
                getLogs(pageNum - 1, querySize);
            }

        } else if (id.indexOf("next") === 0) {
            if (pageNum < pageCount) {
                $trs.remove();
                getLogs(pageNum + 1, querySize);
            }

        } else if (id.indexOf("last") === 0) {
            if (pageNum != pageCount) {
                $trs.remove();
                getLogs(pageCount, querySize);
            }
        }
    }

}

/* 跳转指定页 */
function changeSpecPage() {

    $("#pageTip").empty();
    var myPage = Number($("#myPage").val());
    var pageNum = Number($("#pageNumVal").val());
    var pageCount = Number($("#pageCountVal").val());
    $("#myPage").val("");

    if (myPage == pageNum) {
        $("#pageTip").html("当前页已经是目标页");
        $("#myPage").focus();

    } else if (myPage == null || myPage == "") {
        $("#pageTip").html("请输入要跳转的页");
        $("#myPage").focus();

    } else if (isNaN(myPage)) {
        $("#pageTip").html("请输入数字");
        $("#myPage").focus();

    } else if (myPage < 1) {
        $("#pageTip").html("请输入有效页数");
        $("#myPage").focus();

    } else if (myPage > pageCount) {
        $("#pageTip").html("已超过最大页数");
        $("#myPage").focus();

    } else {
        getLogs(myPage, querySize);
    }

}

/* [删除] */
function remove(ele) {
    if (confirm("您确定要删除吗?")) {
        var uuid = $(ele).closest("td").siblings("td:eq(1)").html();
        var url = ctxPath + "manage/removeOneLog";
        var params = {
            "uuid": uuid,
            "dataSource": dataSource
        };
        $.get(url, params, function () {
            var pageNum = Number($("#pageNumVal").val());
            if (isNaN(pageNum)) {
                // 页数不存在，表示在[查找结果]页面删除数据，重新显示数据
                $.each(queryResult.logs, function (index, item) {
                    if (item.uuid == uuid) {
                        queryResult.logs.splice(index, 1);
                        $("#contentTable tr").siblings().not("tr[id^='fix']").remove();
                        $(".pagelist").empty().html("共找到<strong style='color: red;'>" + queryResult.logs.length + "</strong>条数据");
                        showRowData(queryResult);
                        return false;
                    }
                });
            } else {
                // 页数存在，表示在[正常显示]页删除数据，重新查询、显示数据
                getLogs(pageNum, querySize);
            }
        });
    }
}

/* [批量删除] */
function removeBatch() {
    if ($("input[name='indexName']:checked").length == 0) {
        alert("请选择需要删除的数据！");

    }else {
        if (confirm("您确定要删除选中数据吗?")) {
            var $inputs = $("input[name='indexName']:checked");
            var uuids = [];
            $inputs.each(function () {
                var uuid = $(this).parents("tr").find("td:eq(1)").html();
                uuids.push(uuid);
            });
            var url = ctxPath + "manage/removeBatchLogs";
            var params = {
                "dataSource": dataSource,
                "uuids": uuids
            };
            $.get(url, params, function () {
                $("#selectAll").attr("checked", false);
                var pageNum = Number($("#pageNumVal").val());
                if (isNaN(pageNum)) {
                    // 页数不存在，表示在[查找结果]页面删除数据，重新显示数据
                    $.each(uuids, function (uuidsIndex, uuidsItem) {
                        $.each(queryResult.logs, function (index, item) {
                            if (uuidsItem == item.uuid) {
                                queryResult.logs.splice(index, 1);
                                $("#contentTable tr").siblings().not("tr[id^='fix']").remove();
                                $(".pagelist").empty().html("共找到<strong style='color: red;'>" + queryResult.logs.length + "</strong>条数据");
                                showRowData(queryResult);
                                return false;
                            }
                        });
                    });
                } else {
                    // 页数存在，表示在[正常显示]页删除数据，重新查询、显示数据
                    getLogs(pageNum, querySize);
                }
            });
        }
    }
}

/* [查看/修改] */
function queryLogByUuid(ele) {
    var uuid = $(ele).closest("td").siblings("td").eq(1).html();
    var url = ctxPath + "manage/queryLogByUuid";
    var params = {
        "uuid": uuid
    };
    $.getJSON(url, params, function (data) {
        if (data.result) {
            window.location = ctxPath + "show/log/modify";
        }
    });
}

/* 根据关键词查找 */
function queryLogByKey() {
    var queryType = $("#queryType option:selected").val();
    var $queryKey = $("#queryKey");// eg:2018-02-19
    var qk_tgn = $queryKey.prop("tagName").toLowerCase();
    var qk_val = null;
    if (qk_tgn == "select") {
        qk_val = $queryKey.find("option:selected").val();

    } else if (qk_tgn == "input") {
        if ($queryKey.val() == "" || $queryKey.val() == null) {
            $queryKey.focus();
        }else {
            qk_val = $queryKey.val();
        }
    }

    if (qk_val != null) {
        var url = ctxPath + "manage/queryLogByKey";
        var params = {
            "dataSource": dataSource
        };
        params[queryType] = qk_val;
        $.getJSON(url, params, function (data) {
            $("#contentTable tr").siblings().not("tr[id^='fix']").remove();
            $(".pagelist").empty().html("共找到<strong style='color: red;'>" + data.logs.length + "</strong>条数据");
            $("#pageHeadTip").html("[查找结果]");
            queryResult = data;
            showRowData(data);
        });
    }
}
