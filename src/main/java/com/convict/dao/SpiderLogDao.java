package com.convict.dao;

import com.convict.dto.LogDto;
import com.convict.dto.PageInfDto;
import com.convict.dto.RentDto;
import com.convict.po.Rent;
import com.convict.po.SpiderLog;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Convict
 * @Date 2018/10/23 10:45
 */

@Repository
public interface SpiderLogDao {

    /**
     * 增加模块
     */
    @Insert("insert into spider_log (uuid, spider_name, data_source, start_time, end_time, crawl_second, page_count, spider_status, spider_status_message)" +
            " values(#{log.uuid}, #{log.spiderName}, #{log.dataSource}, #{log.startTime}, #{log.endTime}, #{log.crawlSecond}, #{log.pageCount}, #{log.spiderStatus}, #{log.spiderStatusMessage})")
    void insertLog(@Param("log") SpiderLog log);

    /**
     * 删除模块
     */
    @Delete("delete from spider_log where data_source = #{dataSource} and uuid = #{uuid}")
    void delete(LogDto dto);

    void deleteBatch(@Param("uuids") List<String> uuids, @Param("dataSource") String dataSource);

    /**
     * 更新模块
     */
    @Update("update spider_log set remark = #{l.remark} where uuid = #{l.uuid}")
    boolean update(@Param("l") SpiderLog log);


    /**
     * 查询模块
     */
    @Select("select * from spider_log where uuid = #{uuid}")
    SpiderLog selectLogByUuid(String uuid);

    @Select("select spider_status from spider_log where uuid = #{uuid}")
    Integer selectSpiderStatusByUuid(String uuid);

    @Select("select count(*) from spider_log where uuid = #{uuid}")
    Integer selectCountByUuid(String uuid);

    @Select("select count(*) from spider_log where data_source = #{dataSource}")
    Integer selectCountByDataSource(String dataSource);

    List<SpiderLog> selectPageLog(PageInfDto dto);

    List<SpiderLog> selectLogByKey(@Param("s") SpiderLog spiderLog, @Param("dataSource") String dataSource);

}
