package com.convict.dto;


/**
 * @Author Convict
 * @Date 2018/12/10 11:34
 */
public class LogDto {

    private String uuid;
    private String dataSource;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String toString() {
        return "LogDto{" +
                "uuid=" + uuid +
                ", dataSource='" + dataSource + '\'' +
                '}';
    }
}

