package com.convict.controller;

import com.alibaba.fastjson.JSONObject;
import com.convict.dto.PageInfDto;
import com.convict.dto.RentDto;
import com.convict.model.DataSourceExtra;
import com.convict.po.Rent;
import com.convict.service.RentService;
import com.convict.util.CrawlerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Convict
 * 2018/10/13 13:58
 */

@Controller
@RequestMapping("/manage")
public class ManageRentController extends MaintainSessionList {

    private RentService rentService;
    private HttpServletRequest request;

    @Autowired
    public ManageRentController(RentService rentService, HttpServletRequest request) {
        this.rentService = rentService;
        this.request = request;
    }

    /**
     * 删除一条租房信息
     * @param dto
     */
    @ResponseBody
    @RequestMapping(value = "/removeOneRent")
    public void removeOneRent(RentDto dto) {
        rentService.remove(dto);
    }

    /**
     * 删除多条租房信息
     * @param ids
     * @param dataSource
     */
    @ResponseBody
    @RequestMapping(value = "/removeBatchRents")
    public void removeBatchRents(@RequestParam(value = "ids[]") List<Integer> ids, @RequestParam(value = "dataSource") String dataSource) {
        rentService.removeBatch(ids, dataSource);
    }

    @ResponseBody
    @RequestMapping(value = "/removeAllRents")
    public void removeAllRents(@RequestParam(value = "dataSource") String dataSource) {
        rentService.removeAll(dataSource);
    }

    /**
     * 修改一条租房信息
     * @param rent
     * @param dataSource
     * @return
     */
    @RequestMapping(value = "/modifyOneRent")
    public String modifyOneRent(Rent rent, String dataSource) {
        DataSourceExtra extra = CrawlerUtil.getExtra(dataSource);
        if (rentService.modify(rent, dataSource)) {
            // 移除对应session、sessionList对应的值
            autoUpdateSession(request, extra.getRentsKey(), null);
        }
        return "redirect:/show/rent/impl/" + extra.getViewName();
    }

    /**
     * 根据id查询一条租房信息
     * @param dto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryRentById")
    public JSONObject queryRentById(RentDto dto) {
        Rent modifyRent = rentService.queryRentById(dto);
        autoUpdateSession(request, "modifyDataSource", dto.getDataSource());
        autoUpdateSession(request, "modifyRent", modifyRent);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", true);
        return jsonObject;
    }

    /**
     * 根据数据源，关键词，查询相关租房信息
     * @param rent
     * @param dataSource
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryRentsByKey")
    public JSONObject queryRentsByKey(Rent rent, String dataSource) {
        return rentService.queryRentByKey(rent, dataSource);
    }

    /**
     * 查询页数据
     * @param dto
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryPageRents")
    public JSONObject queryPageRents(@ModelAttribute PageInfDto dto) {
        JSONObject pageRents = rentService.queryPageRents(dto);
        String rentsKey = CrawlerUtil.getExtra(dto.getDataSource()).getRentsKey();
        autoUpdateSession(request, rentsKey, pageRents);
        return pageRents;
    }

}
