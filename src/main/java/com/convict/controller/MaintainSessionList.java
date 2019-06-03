package com.convict.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

/**
 * @Author Convict
 * @Date 2019/1/19 23:06
 */

/**
 * 维护session中的保存所有session的key的一个list集合
 * autoUpdateSession：如果第三个参数 object为空即为减，不为空即往session加
 */
public class MaintainSessionList {

    // default 包访问级别
    void autoUpdateSession(HttpServletRequest request, String key, Object object) {
        HttpSession session = request.getSession();
        ArrayList<String> sessionList;
        if (session.getAttribute("sessionList") != null) {
            sessionList = (ArrayList<String>) session.getAttribute("sessionList");
        } else {
            sessionList = new ArrayList<>();
        }

        // 移除属性
        if (object == null) {
            session.removeAttribute(key);
            sessionList.remove(key);
        }
        // 增加属性
        else {
            session.setAttribute(key, object);
            if (!sessionList.contains(key)) {
                sessionList.add(key);
            }
        }
        // 更新 sessionList
        session.setAttribute("sessionList", sessionList);
    }
}
