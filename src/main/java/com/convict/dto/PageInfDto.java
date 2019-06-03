package com.convict.dto;

/**
 * @Author Convict
 * @Date 2018/11/2 16:13
 */
public class PageInfDto {

    private String tableName;
    private String dataSource;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "PageInfDto{" +
                "tableName='" + tableName + '\'' +
                ", dataSource='" + dataSource + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", offset=" + offset +
                '}';
    }
}
