package com.convict.model;

/**
 * @Author Convict
 * @Date 2019/1/26 15:03
 */
public class DataSourceExtra {

    private String spiderName; // 爬虫名字

    private String listUrl; // 列表url

    private String logKey; // log在session域的key

    private String rentsKey; // rents在session域的key

    private String rentTableName; // rent所在的表名

    private String viewName; // rent、log对应的视图名

    public DataSourceExtra() {
    }

    public DataSourceExtra(String spiderName, String listUrl, String logKey, String rentsKey, String rentTableName, String viewName) {
        this.spiderName = spiderName;
        this.listUrl = listUrl;
        this.logKey = logKey;
        this.rentsKey = rentsKey;
        this.rentTableName = rentTableName;
        this.viewName = viewName;
    }

    public String getSpiderName() {
        return spiderName;
    }

    public void setSpiderName(String spiderName) {
        this.spiderName = spiderName;
    }

    public String getListUrl() {
        return listUrl;
    }

    public void setListUrl(String listUrl) {
        this.listUrl = listUrl;
    }

    public String getLogKey() {
        return logKey;
    }

    public void setLogKey(String logKey) {
        this.logKey = logKey;
    }

    public String getRentsKey() {
        return rentsKey;
    }

    public void setRentsKey(String rentsKey) {
        this.rentsKey = rentsKey;
    }

    public String getRentTableName() {
        return rentTableName;
    }

    public void setRentTableName(String rentTableName) {
        this.rentTableName = rentTableName;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
}
