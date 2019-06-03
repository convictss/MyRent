package com.convict.dto;

import java.util.List;

/**
 * Created by Convict
 * 2018/10/12 13:25
 */
public class RentDto {
    private Integer id;
    private List<String> images;
    private String tableName;
    private String dataSource;

    public RentDto() {
    }

    public RentDto(Integer id, String tableName) {
        this.id = id;
        this.tableName = tableName;
    }

    public RentDto(Integer id, String tableName, List<String> images) {
        this.id = id;
        this.tableName = tableName;
        this.images = images;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
}
