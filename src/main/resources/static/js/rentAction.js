var querySize = 10; // 页显示数据量
// var pageInf = null; // 存储每一页的rents：排序用到
var queryResult = null; // 存储根据关键词查找的结果

/* 页面加载时动作 */
$(function () {

    // 回车查询
    $(document).keydown(function (event) {
        if (event.keyCode === 13) {
            $("#queryBtn").click();
        }
    });

    // 监听[价格]th按钮
    $("#priceBtn").click(function () {
        $("#contentTable tr").siblings().not("tr[id^='fix']").empty();
        // 页数用来判断当前是正常显示页还是查找结果页
        var pageNum = Number($(".current").html());
        // 隐藏域排序标识
        var flag = $(this).siblings("input:eq(0)");
        if (flag.val() == "none" || flag.val() == "sub") {
            $(this).html("月租∧");
            flag.val("add");
            if (isNaN(pageNum)) {
                // 页数不存在，表示在[查找结果]页
                queryResult.rents.sort(sortPriceAsc);
                showRowData(queryResult);
            } else {
                // 页数存在，表示在[正常显示]页
                pageInf.rents.sort(sortPriceAsc);
                showRowData(pageInf);
            }
        } else {
            $(this).html("月租∨");
            flag.val("sub");
            if (isNaN(pageNum)) {
                // 页数不存在，表示在[查找结果]页
                queryResult.rents.sort(sortPriceDesc);
                showRowData(queryResult);
            } else {
                // 页数存在，表示在[正常显示]页
                pageInf.rents.sort(sortPriceDesc);
                showRowData(pageInf);
            }
        }
    });

    // 监听[查询关键词]下拉框
    $("#queryType").click(function () {
        var queryType = $(this).find("option:selected").val();
        var inputKeyModel = "<input type=\"text\" placeholder=\"搜索关键词\" id=\"queryKey\" style=\"width:150px; height:30px;\" />";
        var selectKeyModel = "<select id=\"queryKey\" style=\"height: 30px; border-color:#0ae;\">\n" +
            "<option value=\"越秀\">越秀</option>\n" +
            "<option value=\"荔湾\">荔湾</option>\n" +
            "<option value=\"海珠\">海珠</option>\n" +
            "<option value=\"天河\">天河</option>\n" +
            "<option value=\"白云\">白云</option>\n" +
            "<option value=\"黄埔\">黄埔</option>\n" +
            "<option value=\"番禺\">番禺</option>\n" +
            "<option value=\"花都\">花都</option>\n" +
            "<option value=\"萝岗\">萝岗</option>\n" +
            "<option value=\"从化\">从化</option>\n" +
            "<option value=\"增城\">增城</option>\n" +
            "<option value=\"南沙\">南沙</option>\n" +
            "</select>";
        var $queryKey = $("#queryKey");
        var qk_tgn = $queryKey.prop("tagName").toLowerCase();
        if (queryType == "region") {
            if (qk_tgn == "input") {
                $queryKey.remove();
                $(this).after(selectKeyModel);
            }

        } else if (queryType == "publishDate") {
            if (qk_tgn == "select") {
                $queryKey.remove();
                $(this).after(inputKeyModel);
                $(this).siblings("input").attr("type", "date");
            } else {
                $queryKey.attr("type", "date");
            }

        } else {
            if (qk_tgn == "select") {
                $queryKey.remove();
                $(this).after(inputKeyModel);
            } else {
                $queryKey.attr("type", "text");
            }
        }

    });

    // 全选
    $("#selectAll").click(function () {
        if (this.checked) {
            $("input[name='indexName']").each(function () {
                this.checked = true;
            });
        } else {
            $("input[name='indexName']").each(function () {
                this.checked = false;
            });
        }
    });


});

/* 获取页数据 */
function getRents(pageNum, pageSize) {
    var url = ctxPath + "manage/queryPageRents";
    var params = {
        "dataSource": dataSource,
        "pageNum": pageNum,
        "pageSize": pageSize
    };
    $.getJSON(url, params, function (data) {
        $("#contentTable tr").siblings().not("tr[id^='fix']").empty();
        pageInf = data;
        showRowData(data);
    })
}

//定义价格升降序函数
function sortPriceAsc(a, b) {
    return a.price - b.price;
}

function sortPriceDesc(a, b) {
    return b.price - a.price;
}

/* 显示行数据、页码等 */
function showRowData(data) {
    $.each(data.rents, function (index, item) {
        var rowData = "<tr>" +
            "<td><input type=\"checkbox\" name=\"indexName\" />" + (index + 1) + "</td>" +
            "<td>" + item.id + "</td>" +
            "<td>" + item.title + "</td>" +
            "<td>" + item.houseType + "</td>" +
            "<td>" + item.region + "</td>" +
            "<td>" + item.price + "</td>" +
            "<td>" + item.contact + "</td>" +
            "<td>" + item.floor + "</td>" +
            /*"<td><a type=\"button\" class=\"button border-yellow\" href=\"" + item.link + "\" target=\"_blank\"> 原链接 </a></td>" +*/
            "<td>" + timeStamp2Date(item.publishDate) + "</td>" +
            "<td>" +
            "<div class=\"button-group\">" +
            "<a class=\"button border-main icon-edit\" onclick=\"queryRentById(this)\"> 详情/修改 </a>" +
            "<a class=\"button border-red icon-trash-o\" href=\"javascript:void(0)\" onclick=\"remove(this)\"> 删除 </a>" +
            "</div>" +
            "</td>" +
            "</tr>";
        $("#contentTable tr:eq(-2)").after(rowData);
    });

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
        $("#rentsCountSpan").html(data.rentsCount);
        $("#pageCountSpan").html(data.pageCount);
        // $("#pageNumVal").val(data.pageNum);
    }
}

/* 跳转前后、首尾、显示页码的页 */
function changeFixPage(ele) {
    $("#pageTip").empty();
    var $trs = $("#contentTable tr").siblings().not("tr[id^='fix']");
    var id = ele.id;
    var pageNum = Number($(".current").html());
    var pageCount = Number($("#pageCountSpan").html());

    // 跳转显示页码的页
    if (id == "") {
        pageNum = Number($(ele).html());
        $trs.remove();
        getRents(pageNum, querySize);
    }

    // 跳转前后页、首尾页
    else {
        if (id.indexOf("home") === 0) {
            if (pageNum != 1) {
                $trs.remove();
                getRents(1, querySize);
            }

        } else if (id.indexOf("previous") === 0) {
            if (pageNum > 1) {
                $trs.remove();
                getRents(pageNum - 1, querySize);
            }

        } else if (id.indexOf("next") === 0) {
            if (pageNum < pageCount) {
                $trs.remove();
                getRents(pageNum + 1, querySize);
            }

        } else if (id.indexOf("last") === 0) {
            if (pageNum != pageCount) {
                $trs.remove();
                getRents(pageCount, querySize);
            }
        }
    }

}

/* 跳转指定页 */
function changeSpecPage() {
    $("#pageTip").empty();
    var myPage = Number($("#myPage").val());
    var pageNum = Number($(".current").html());
    var pageCount = Number($("#pageCountSpan").html());
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
        getRents(myPage, querySize);
    }

}

/* [删除] */
function remove(ele) {
    if (confirm("您确定要删除吗?")) {
        var id = $(ele).closest("td").siblings("td:eq(1)").html();
        var url = ctxPath + "manage/removeOneRent";
        var params = {
            "id": id,
            "dataSource": dataSource
        };
        $.get(url, params, function () {
            var pageNum = Number($(".current").html());
            if (isNaN(pageNum)) {
                // 页数不存在，表示在[查找结果]页面删除数据，需重新查找结果的数据，并移除session的缓存
                $.each(queryResult.rents, function (index, item) {
                    if (item.id == id) {
                        queryResult.rents.splice(index, 1);
                        $("#contentTable tr").siblings().not("tr[id^='fix']").remove();
                        $(".pagelist").empty().html("共找到<strong style='color: red;'>" + queryResult.rents.length + "</strong>条数据");
                        showRowData(queryResult);
                        return false;
                    }
                });
                var delPgInfUrl = ctxPath + "manage/removeSessionByDataSource";
                var delPageInfParam = {
                    "dataSource": dataSource
                };
                $.get(delPgInfUrl, delPageInfParam);

            } else {
                // 页数存在，表示在[正常显示]页删除数据，重新查询、显示数据
                getRents(pageNum, querySize);
            }
        });
    }
}

/* [批量删除] */
function removeBatch() {
    if ($("input[name='indexName']:checked").length == 0) {
        alert("请选择需要删除的数据！");

    } else {
        if (confirm("您确定要删除选中数据吗?")) {
            var $input = $("input[name='indexName']:checked");
            var ids = [];
            $input.each(function () {
                var id = $(this).parent().siblings("td").eq(0).html();
                ids.push(id);
            });
            var url = ctxPath + "manage/removeBatchRents";
            var params = {
                "dataSource": dataSource,
                "ids": ids
            };
            $.get(url, params, function () {
                $("#selectAll").attr("checked", false);
                var pageNum = Number($(".current").html());
                if (isNaN(pageNum)) {
                    // 页数不存在，表示在[查找结果]页面删除数据，需重新查找结果的数据，并移除session的缓存
                    $.each(ids, function (idsIndex, idsItem) {
                        $.each(queryResult.rents, function (index, item) {
                            if (idsItem == item.id) {
                                queryResult.rents.splice(index, 1);
                                $("#contentTable tr").siblings().not("tr[id^='fix']").remove();
                                $(".pagelist").empty().html("共找到<strong style='color: red;'>" + queryResult.rents.length + "</strong>条数据");
                                showRowData(queryResult);
                                return false;
                            }
                        });
                    });
                    var delPgInfUrl = ctxPath + "manage/removeSessionByDataSource";
                    var delPageInfParam = {
                        "dataSource": dataSource
                    };
                    $.get(delPgInfUrl, delPageInfParam);

                } else {
                    // 页数存在，表示在[正常显示]页删除数据，重新查询、显示数据
                    getRents(pageNum, querySize);
                }
            });
        }
    }
}

/* [删除全部] */
function removeAll() {
    if (confirm("您确定要清空整个数据表吗?")) {
        if (confirm("再次确认！您确定要清空整个数据表吗?！")) {
            var url = ctxPath + "manage/removeAllRents";
            var params = {
                "dataSource": dataSource
            };
            $.get(url, params, function () {
                // var pageNum = Number($(".current").html());
                getRents(1, querySize);
            });
        }
    }
}

/* [查看/修改] */
function queryRentById(ele) {
    var id = $(ele).closest("td").siblings("td").eq(1).html();
    var url = ctxPath + "manage/queryRentById";
    var params = {
        "id": id,
        "dataSource": dataSource
    };
    $.getJSON(url, params, function (data) {
        if (data.result) {
            window.location = ctxPath + "show/rent/modify";
        }
    });
}

/* 查找 */
function queryRentsByKey() {
    var queryType = $("#queryType option:selected").val();
    var $queryKey = $("#queryKey");
    if ($queryKey.val() == "" || $queryKey.val() == null) {
        $queryKey.focus();
    } else {
        if (queryType == "id" || queryType == "price" || queryType == "floor") {
            if (isNaN($queryKey.val())) {
                $queryKey.val("");
                $queryKey.attr("placeholder", "请输入数字");
            }
        }
        if ($queryKey.val() != "" && $queryKey.val() != null) {
            var url = ctxPath + "manage/queryRentsByKey";
            var params = {
                "dataSource": dataSource
            }
            params[queryType] = $queryKey.val();
            $.getJSON(url, params, function (data) {
                $("#contentTable tr").siblings().not("tr[id^='fix']").remove();
                $(".pagelist").empty().html("共找到<strong style='color: red;'>" + data.rents.length + "</strong>条数据");
                $("#pageHeadTip").html("[查找结果]");
                queryResult = data;
                showRowData(data);
            });
        }
    }
}

/* 时间戳转换js日期 */
function timeStamp2Date(timeStamp) {
    if (timeStamp == "" || timeStamp == null) {
        return "";
    } else {
        var date = new Date(timeStamp);
        var yyyy = date.getFullYear() + '-';
        var MM = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
        var dd = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate());
        // var dd = (date.timeStamp2Date() < 10 ? '0' + date.timeStamp2Date() : date.timeStamp2Date()) + ' ';
        // var h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ":";
        // var m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ":";
        // var s = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
        return yyyy + MM + dd;
    }

}