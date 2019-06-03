package com.convict.service;

import com.alibaba.fastjson.JSONObject;
import com.convict.dto.PageInfDto;
import com.convict.dto.RentDto;
import com.convict.po.Rent;

import java.util.List;

/**
 * @Author Convict
 * @Date 2018/10/14 16:52
 */
public interface RentService {

    void remove(RentDto dto);

    void removeBatch(List<Integer> ids, String dataSource);

    void removeAll(String dataSource);

    boolean modify(Rent rent, String dataSource);

    Rent queryRentById(RentDto dto);

    JSONObject queryRentByKey(Rent rent, String dataSource);

    JSONObject queryPageRents(PageInfDto dto);

    Integer queryAllCount(String dataSource);
}
