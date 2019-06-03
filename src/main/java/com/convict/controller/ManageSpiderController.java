package com.convict.controller;

import com.alibaba.fastjson.JSONObject;
import com.convict.model.CrawlContext;
import com.convict.po.SpiderLog;
import com.convict.service.SpiderLogService;
import com.convict.spider.SpiderEntrance;
import com.convict.util.CommonUtil;
import com.convict.util.CrawlerUtil;
import com.convict.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Convict
 * @Date 2018/10/18 17:15
 */

@Controller
@RequestMapping("/manage")
public class ManageSpiderController {

    private SpiderLogService spiderLogService;

    private final HttpServletRequest request;

    @Autowired
    public ManageSpiderController(SpiderLogService spiderLogService, HttpServletRequest request) {
        this.spiderLogService = spiderLogService;
        this.request = request;
    }

    /**
     * 启动爬虫
     * @param dataSource
     * @param threadCount
     * @param imgDown
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/launchSpider", produces = "text/html;charset=UTF-8")
    public JSONObject launchSpider(
            @RequestParam("dataSource") String dataSource,
            @RequestParam("threadCount") Integer threadCount,
            @RequestParam("imgDown") Boolean imgDown) {

        String uuid = CommonUtil.getUUID();
        SpiderLog log = new SpiderLog(uuid, dataSource);
        CrawlContext context = new CrawlContext(log, imgDown, threadCount);

        String spiderName = CrawlerUtil.getExtra(dataSource).getSpiderName();
        SpiderEntrance entrance = SpringContextUtil.getBean(spiderName);

        // 房天下、链家需要额外设置site
        if (spiderName.startsWith("fangTianXia")) {
            Site site = entrance.getSite();
            site.setCharset("GBK");
            entrance.setSite(site);
        } else if(spiderName.startsWith("lianJia")) {
            Site site = entrance.getSite();
            site.setCharset("UTF-8");
            entrance.setSite(site);
        }

        HttpSession session = request.getSession();
        List<String> activeDataSource = (List<String>) session.getAttribute("activeDataSource");
        if (activeDataSource == null) {
            activeDataSource = new ArrayList<>();
        }
        activeDataSource.add(dataSource);
        session.setAttribute("activeDataSource", activeDataSource);

        entrance.startSpider(Spider.create(entrance), context);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 3);
        jsonObject.put("message", "爬虫爬取数据中...");
        jsonObject.put("uuid", uuid);
        return jsonObject;
    }

    /**
     * 停止爬虫
     * @param dataSource
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/suspendSpider", produces = "text/html;charset=UTF-8")
    public JSONObject suspendSpider(@RequestParam("dataSource") String dataSource) {
        String spiderName = CrawlerUtil.getExtra(dataSource).getSpiderName();
        SpiderEntrance entrance = SpringContextUtil.getBean(spiderName);

        entrance.stopSpider();
        HttpSession session = request.getSession();
        List<String> activeDataSource = (List<String>) session.getAttribute("activeDataSource");
        activeDataSource.remove(dataSource);
        if (activeDataSource.size() == 0) {
            session.removeAttribute("activeDataSource");
        } else {
            session.setAttribute("activeDataSource", activeDataSource);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 2);
        jsonObject.put("message", "被手动停止");
        return jsonObject;
    }

    /**
     * 检查爬虫是否在运行
     * @param uuid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkSpider", produces = "text/html;charset=UTF-8")
    public JSONObject checkSpider(@RequestParam("uuid") String uuid) {
        JSONObject jsonObject = new JSONObject();
        if (uuid != null && !uuid.equals("")) {
            if (spiderLogService.queryCountByUuid(uuid) == 0) {
                jsonObject.put("status", 3);
                jsonObject.put("message", "爬虫爬取数据中...");
            } else {
                Integer spiderStatus = spiderLogService.querySpiderStatusByUuid(uuid);
                String dataSource = spiderLogService.queryLogByUuid(uuid).getDataSource();
                if (spiderStatus == 0) {
                    jsonObject.put("status", 0);
                    jsonObject.put("message", "异常！请查看日志！");
                } else if (spiderStatus == 1) {
                    jsonObject.put("status", 1);
                    jsonObject.put("message", "自然爬取结束");
                } else if (spiderStatus == 2) {
                    jsonObject.put("status", 2);
                    jsonObject.put("message", "被手动停止");
                }
                HttpSession session = request.getSession();
                List<String> activeDataSource = (List<String>) session.getAttribute("activeDataSource");
                activeDataSource.remove(dataSource);
                if (activeDataSource.size() == 0) {
                    session.removeAttribute("activeDataSource");
                } else {
                    session.setAttribute("activeDataSource", activeDataSource);
                }
            }
        }
        return jsonObject;
    }

    /**
     * 获取正在运行的数据源列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getActiveDataSource")
    public JSONObject getActiveDataSource() {
        JSONObject jsonObject = new JSONObject();
        HttpSession session = request.getSession();
        List<String> activeDataSource = (List<String>) session.getAttribute("activeDataSource");
//        System.out.println("list " + activeDataSource);
        jsonObject.put("activeDataSource", activeDataSource);
        return jsonObject;
    }

}
