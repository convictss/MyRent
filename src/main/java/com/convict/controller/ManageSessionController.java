package com.convict.controller;

import com.alibaba.fastjson.JSONObject;
import com.convict.model.DataSourceExtra;
import com.convict.util.CrawlerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @Author Convict
 * @Date 2018/11/17 16:04
 */
@Controller
@RequestMapping("/manage")
@SessionAttributes(value = {"curAdmin"})
public class ManageSessionController extends MaintainSessionList {

    @Autowired
    private HttpServletRequest request;

    /**
     * 根据数据源，删除session缓存，如果数据源为空，则清空所有缓存只保留登录用户
     * @param dataSource
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/removeSessionByDataSource", produces = "text/html;charset=UTF-8")
    public JSONObject removeSessionByDataSource(@RequestParam("dataSource") String dataSource, HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        // 没有dataSource，即清空所有session
        if (dataSource == "" || dataSource == null) {

            ArrayList<String> sessionList = (ArrayList<String>) session.getAttribute("sessionList");

            // 保留已经登录的管理员，其余清除
            // 不使用增强型for循环，多线程环境会引发java.util.ConcurrentModificationException: null
            Iterator<String> it = sessionList.iterator();
            while (it.hasNext()) {
                String key = it.next();
                it.remove();
                if (!key.equals("curAdmin")) {
                    // autoUpdateSession方法每次调用会重新获取sessionList，故直接在这里直接维护sessionList
                    // autoUpdateSession(request, sl_key, null);
                    session.removeAttribute(key);
                    sessionList.remove(key);
                }
            }
            session.setAttribute("sessionList", sessionList);
            jsonObject.put("status", 0);
            jsonObject.put("message", "已清除所有缓存");
        }
        
        // 清除session指定key相关缓存
        else {
            DataSourceExtra extra = CrawlerUtil.getExtra(dataSource);
            String rentsKey = extra.getRentsKey();
            String logKey = extra.getLogKey();
            autoUpdateSession(request, rentsKey, null);
            autoUpdateSession(request, logKey, null);
            jsonObject.put("status", 1);
            jsonObject.put("message", "已清除指定缓存");
        }
        return jsonObject;
    }

    /**
     * 根据关键词删除缓存，当前只有登录页面清除错误提示用到
     * @param key
     */
    @ResponseBody
    @RequestMapping("/removeSessionByKey")
    public void removeSessionByKey(String key) {
        if (key != null && key != "") {
            autoUpdateSession(request, key, null);
        }
    }

}
