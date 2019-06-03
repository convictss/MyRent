package com.convict.model;

/**
 * @Author Convict
 * @Date 2019/3/13 20:02
 */
public class WebPath {
    private String path;

    public WebPath() {
    }

    public WebPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "WebPath{" +
                "path='" + path + '\'' +
                '}';
    }
}
