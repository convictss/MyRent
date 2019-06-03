package com.convict.service;

import com.alibaba.fastjson.JSONObject;
import com.convict.dto.LogDto;
import com.convict.dto.PageInfDto;
import com.convict.po.SpiderLog;

import java.util.List;

/**
 * @Author Convict
 * @Date 2018/10/24 15:41
 */
public interface SpiderLogService {

    void remove(LogDto dto);

    void removeBatch(List<String> uuids, String dataSource);

    boolean modify(SpiderLog log);

    SpiderLog queryLogByUuid(String uuid);

    JSONObject queryLogByKey(SpiderLog spiderLog, String dataSource);

    JSONObject queryPageLog(PageInfDto dto);

    Integer queryCountByUuid(String uuid);

    Integer querySpiderStatusByUuid(String uuid);

}
