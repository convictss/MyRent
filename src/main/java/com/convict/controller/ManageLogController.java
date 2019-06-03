package com.convict.controller;

import com.alibaba.fastjson.JSONObject;
import com.convict.dto.LogDto;
import com.convict.dto.PageInfDto;
import com.convict.model.DataSourceExtra;
import com.convict.po.SpiderLog;
import com.convict.service.SpiderLogService;
import com.convict.util.CrawlerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author Convict
 * @Date 2018/12/7 13:32
 */
@Controller
@RequestMapping("/manage")
public class ManageLogController extends MaintainSessionList {

    private SpiderLogService spiderLogService;
    private HttpServletRequest request;

    @Autowired
    public ManageLogController(SpiderLogService spiderLogService, HttpServletRequest request) {
        this.spiderLogService = spiderLogService;
        this.request = request;
    }

    @ResponseBody
    @RequestMapping(value = "/removeOneLog")
    public void removeOneLog(LogDto dto) {
        spiderLogService.remove(dto);
    }

    @ResponseBody
    @RequestMapping(value = "/removeBatchLogs")
    public void removeBatchLogs(@RequestParam(value = "uuids[]") List<String> uuids, @RequestParam(value = "dataSource") String dataSource) {
        spiderLogService.removeBatch(uuids, dataSource);
    }

    @RequestMapping(value = "/modifyOneLog")
    public String modifyOneLog(SpiderLog log, String dataSource) {
        DataSourceExtra extra = CrawlerUtil.getExtra(dataSource);
        if (spiderLogService.modify(log)) {
            autoUpdateSession(request, CrawlerUtil.getExtra(dataSource).getLogKey(), null);
        }
        return "redirect:/show/log/impl/" + extra.getViewName();
    }

    @ResponseBody
    @RequestMapping(value = "/queryLogByUuid")
    public JSONObject queryLogByUuid(String uuid) {
        autoUpdateSession(request, "modifyLog", spiderLogService.queryLogByUuid(uuid));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", true);
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping(value = "/queryLogByKey")
    public JSONObject queryLogByKey(SpiderLog spiderLog, String dataSource) {
        return spiderLogService.queryLogByKey(spiderLog, dataSource);
    }

    @ResponseBody
    @RequestMapping("/queryPageLogs")
    public JSONObject queryPageLogs(@ModelAttribute PageInfDto dto) {
        JSONObject jsonObject = spiderLogService.queryPageLog(dto);
        autoUpdateSession(request, CrawlerUtil.getExtra(dto.getDataSource()).getLogKey(), jsonObject);
        return jsonObject;
    }
}
