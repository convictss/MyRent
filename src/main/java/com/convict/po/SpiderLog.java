package com.convict.po;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author Convict
 * @Date 2018/10/23 10:52
 */
public class SpiderLog {

    private String uuid; // 数据库唯一uuid

    private String spiderName; // 爬虫名字

    private String dataSource; // 数据来源

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime; // 爬虫启动时间

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime; // 爬虫结束时间

    private Integer crawlSecond; // 抓取耗时（秒）

    private Integer pageCount; // 抓取页数

    private Integer spiderStatus; // 爬虫状态，0：异常  1：正常结束  2：手动结束

    private String spiderStatusMessage; // 爬虫状态具体描述信息

    private String remark; // 备注信息

    public SpiderLog() {
    }

    public SpiderLog(String uuid, String dataSource) {
        this.uuid = uuid;
        this.dataSource = dataSource;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSpiderName() {
        return spiderName;
    }

    public void setSpiderName(String spiderName) {
        this.spiderName = spiderName;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getCrawlSecond() {
        return crawlSecond;
    }

    public void setCrawlSecond(Integer crawlSecond) {
        this.crawlSecond = crawlSecond;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getSpiderStatus() {
        return spiderStatus;
    }

    public void setSpiderStatus(Integer spiderStatus) {
        this.spiderStatus = spiderStatus;
    }

    public String getSpiderStatusMessage() {
        return spiderStatusMessage;
    }

    public void setSpiderStatusMessage(String spiderStatusMessage) {
        this.spiderStatusMessage = spiderStatusMessage;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "SpiderLog{" +
                "uuid='" + uuid + '\'' +
                ", spiderName='" + spiderName + '\'' +
                ", dataSource='" + dataSource + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", crawlSecond=" + crawlSecond +
                ", pageCount=" + pageCount +
                ", spiderStatus=" + spiderStatus +
                ", spiderStatusMessage='" + spiderStatusMessage + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
