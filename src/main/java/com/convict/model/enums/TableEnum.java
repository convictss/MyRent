package com.convict.model.enums;

public enum TableEnum {
    Anjuke("anjuke"),
    AnjukeImage("anjuke_image"),
    FiveEightTongCheng("58tongcheng"),
    FiveEightTongChengImage("58tongcheng_image"),
    FangTianXia("fangtianxia"),
    FangTianXiaImage("fangtianxia_image"),
    LeYouJia("leyoujia"),
    LeYouJiaImage("leyoujia_image"),
    GanJiWang("ganjiwang"),
    GanJiWangImage("ganjiwang_image"),
    BaiXingWang("baixingwang"),
    BaiXingWangImage("baixingwang_image"),
    LianJia("lianjia"),
    LianJiaImage("lianjia_image"),
    QFangWang("qfangwang"),
    QFangWangImage("qfangwang_image"),
    ;

    private String value;

    TableEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
