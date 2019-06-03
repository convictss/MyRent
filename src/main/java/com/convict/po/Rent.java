package com.convict.po;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class Rent {
    private Integer id;// 数据库id

    private String link;// 每条数据特有的链接

    private String title;// 标题

    private String region;// 区域, eg: 天河

    private String community;// 住宅小区, eg: 雷和花园

    private String address;// 具体地址, eg: 同和 同和路21号

    private String area;// 面积大小, eg: 50平米

    private String houseType;// 房子类型, eg: 三房一厅

    private String floor;// 楼层类型, eg: 中楼层

    private Integer price;// 价格, eg: 2500

    private String depositType;// 押金类型. eg: 押一付一

    private List<String> images;

    private String contact; // 联系人

    private String phoneNumber; // 联系电话

    private String trafficRoute; // 交通路线

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publishDate; // 发布日期

    public Rent() {
    }

    public Rent(String link, String title, String region, String community, String address, String area, String houseType, String floor, Integer price) {
        this.link = link;
        this.title = title;
        this.region = region;
        this.community = community;
        this.address = address;
        this.area = area;
        this.houseType = houseType;
        this.floor = floor;
        this.price = price;
    }

    public Rent(String link, String title, String region, String community, String address, String area, String houseType, String floor, Integer price, String depositType, String contact, String phoneNumber) {
        this.link = link;
        this.title = title;
        this.region = region;
        this.community = community;
        this.address = address;
        this.area = area;
        this.houseType = houseType;
        this.floor = floor;
        this.price = price;
        this.depositType = depositType;
        this.contact = contact;
        this.phoneNumber = phoneNumber;
    }

    public Rent(String link, String title, String region, String community, String address, String area, String houseType, String floor, Integer price, String depositType, String contact, String phoneNumber, String trafficRoute) {
        this.link = link;
        this.title = title;
        this.region = region;
        this.community = community;
        this.address = address;
        this.area = area;
        this.houseType = houseType;
        this.floor = floor;
        this.price = price;
        this.depositType = depositType;
        this.contact = contact;
        this.phoneNumber = phoneNumber;
        this.trafficRoute = trafficRoute;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDepositType() {
        return depositType;
    }

    public void setDepositType(String depositType) {
        this.depositType = depositType;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTrafficRoute() {
        return trafficRoute;
    }

    public void setTrafficRoute(String trafficRoute) {
        this.trafficRoute = trafficRoute;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public String toString() {
        return "Rent{" +
                "id=" + id +
                ", link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", region='" + region + '\'' +
                ", community='" + community + '\'' +
                ", address='" + address + '\'' +
                ", area='" + area + '\'' +
                ", houseType='" + houseType + '\'' +
                ", floor='" + floor + '\'' +
                ", price=" + price +
                ", depositType='" + depositType + '\'' +
                ", images=" + images +
                ", contact='" + contact + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", trafficRoute='" + trafficRoute + '\'' +
                ", publishDate=" + publishDate +
                '}';
    }
}
