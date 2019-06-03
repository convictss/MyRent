package com.convict.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.convict.dao.SpiderLogDao;
import com.convict.dto.LogDto;
import com.convict.dto.PageInfDto;
import com.convict.po.SpiderLog;
import com.convict.service.SpiderLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @Author Convict
 * @Date 2018/10/24 15:41
 */
@Service("spiderLogService")
public class SpiderLogServiceImpl implements SpiderLogService {

    private SpiderLogDao spiderLogDao;

    @Autowired
    public SpiderLogServiceImpl(SpiderLogDao spiderLogDao) {
        this.spiderLogDao = spiderLogDao;
    }


    @Override
    public void remove(LogDto dto) {
        spiderLogDao.delete(dto);
    }

    @Override
    public void removeBatch(List<String> uuids, String dataSource) {
        spiderLogDao.deleteBatch(uuids, dataSource);
    }

    @Override
    public boolean modify(SpiderLog log) {
        return spiderLogDao.update(log);
    }

    @Override
    public SpiderLog queryLogByUuid(String uuid) {
        return spiderLogDao.selectLogByUuid(uuid);
    }


    @Override
    public JSONObject queryLogByKey(SpiderLog spiderLog, String dataSource) {
        if (spiderLog.getStartTime() != null) {// 根据爬取日期查询，设置结束时间，以便发出sql语句
            Date date = spiderLog.getStartTime();
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(date);

            Calendar endCal = new GregorianCalendar(startCal.get(Calendar.YEAR), startCal.get(Calendar.MONTH), startCal.get(Calendar.DATE));
            endCal.set(Calendar.HOUR, 23);
            endCal.set(Calendar.MINUTE, 59);
            endCal.set(Calendar.SECOND, 59);
            spiderLog.setEndTime(endCal.getTime());
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("logs", spiderLogDao.selectLogByKey(spiderLog, dataSource));
        return jsonObject;
    }

    @Override
    public JSONObject queryPageLog(PageInfDto dto) {
        if (dto.getPageNum() > 0) {
            dto.setOffset((dto.getPageNum() - 1) * dto.getPageSize());
        }
        JSONObject jsonObject = new JSONObject();
        Integer logsCount = spiderLogDao.selectCountByDataSource(dto.getDataSource());
        Integer pageSize = dto.getPageSize();
        Integer pageCount;
        if (logsCount % pageSize == 0) {
            pageCount = logsCount / pageSize;
        }else {
            pageCount = logsCount / pageSize + 1;
        }
        jsonObject.put("logsCount", logsCount);
        jsonObject.put("pageCount", pageCount);
        jsonObject.put("pageNum", dto.getPageNum());
        List<SpiderLog> spiderLogs = spiderLogDao.selectPageLog(dto);
        jsonObject.put("logs", spiderLogs);
        return jsonObject;
    }

    @Override
    public Integer queryCountByUuid(String uuid) {
        return spiderLogDao.selectCountByUuid(uuid);
    }

    @Override
    public Integer querySpiderStatusByUuid(String uuid) {
        return spiderLogDao.selectSpiderStatusByUuid(uuid);
    }


}
