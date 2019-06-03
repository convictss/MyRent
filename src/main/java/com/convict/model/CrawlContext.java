package com.convict.model;

import com.convict.po.Rent;
import com.convict.po.SpiderLog;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Convict
 * @Date 2018/10/24 11:21
 */

/**
 * 抓取上下文：
 * 日志、当前所爬页、已爬页数、是否下载图片、爬虫线程数、爬虫中间件map
 */
public class CrawlContext {

    private SpiderLog spiderLog;

    private Integer pageNum = 1;

    private Integer pageCount = 0;

    private Boolean imgDown;

    private Integer threadCount;

    private Map<String, Rent> detailsMap = new HashMap<>();


    public CrawlContext() {
    }

    public CrawlContext(SpiderLog spiderLog, Boolean imgDown, Integer threadCount) {
        this.spiderLog = spiderLog;
        this.imgDown = imgDown;
        this.threadCount = threadCount;
    }

    public SpiderLog getSpiderLog() {
        return spiderLog;
    }

    public void setSpiderLog(SpiderLog spiderLog) {
        this.spiderLog = spiderLog;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Map<String, Rent> getDetailsMap() {
        return detailsMap;
    }

    public void setDetailsMap(Map<String, Rent> detailsMap) {
        this.detailsMap = detailsMap;
    }

    public Boolean getImgDown() {
        return imgDown;
    }

    public void setImgDown(Boolean imgDown) {
        this.imgDown = imgDown;
    }

    public Integer getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
    }
}
