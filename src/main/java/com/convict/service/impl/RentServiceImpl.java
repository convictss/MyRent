package com.convict.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.convict.dao.RentDao;
import com.convict.dto.PageInfDto;
import com.convict.dto.RentDto;
import com.convict.model.WebPath;
import com.convict.po.Rent;
import com.convict.service.RentService;
import com.convict.util.CrawlerUtil;
import com.convict.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Convict
 * @Date 2018/10/14 16:03
 */

@Service("rentService")
public class RentServiceImpl implements RentService {

    private RentDao rentDao;
    private WebPath webPath;

    @Autowired
    public RentServiceImpl(RentDao rentDao, WebPath webPath) {
        this.rentDao = rentDao;
        this.webPath = webPath;
    }

    @Override
    public void remove(RentDto dto) {
        String tableName = CrawlerUtil.getExtra(dto.getDataSource()).getRentTableName();
        dto.setTableName(tableName);
        String path = webPath.getPath();
        // 查出照片的位置，工具类删除物理照片
        List<String> list = rentDao.selectImagesByRentId(dto);
        if (list.size() != 0) {
            String dirTemp = list.get(0);
            String dir = path + "\\\\static\\\\image\\\\crawl\\\\" + dirTemp.substring(0, dirTemp.lastIndexOf("/")).replace("/", "\\\\");
            FileUtil.deleteFileByPath(dir);
        }
        // 删除数据库的数据
        rentDao.delete(dto);
    }

    @Override
    public void removeBatch(List<Integer> ids, String dataSource) {
        String tableName = CrawlerUtil.getExtra(dataSource).getRentTableName();
        // 遍历id，查出id对应所有照片的位置，工具类删除物理照片
        for (Integer id : ids) {
            RentDto dto = new RentDto(id, tableName);
            String path = webPath.getPath();
            List<String> list = rentDao.selectImagesByRentId(dto);
            if (list.size() != 0) {
                String dirTemp = list.get(0);
                String dir = path + "\\\\static\\\\image\\\\crawl\\\\" + dirTemp.substring(0, dirTemp.lastIndexOf("/")).replace("/", "\\\\");
                FileUtil.deleteFileByPath(dir);
            }
        }
        rentDao.deleteBatch(ids, tableName);
    }

    @Override
    public void removeAll(String dataSource) {
        String tableName = CrawlerUtil.getExtra(dataSource).getRentTableName();
        String path = webPath.getPath();
        String delDir = path + "\\\\static\\\\image\\\\crawl\\\\" + tableName;
        FileUtil.deleteFileByPath(delDir);
        rentDao.deleteAll(tableName);
    }

    @Override
    public boolean modify(Rent rent, String dataSource) {
        String tableName = CrawlerUtil.getExtra(dataSource).getRentTableName();
        return rentDao.update(rent, tableName);
    }

    @Override
    public JSONObject queryPageRents(PageInfDto dto) {
        String tableName = CrawlerUtil.getExtra(dto.getDataSource()).getRentTableName();
        dto.setTableName(tableName);
        if (dto.getPageNum() > 0) {
            dto.setOffset((dto.getPageNum() - 1) * dto.getPageSize());
        }
        JSONObject jsonObject = new JSONObject();
        Integer rentsCount = rentDao.selectAllCountByTableName(dto.getTableName());
        Integer pageSize = dto.getPageSize();
        Integer pageCount;
        if (rentsCount % pageSize == 0) {
            pageCount = rentsCount / pageSize;
        } else {
            pageCount = rentsCount / pageSize + 1;
        }
        jsonObject.put("rentsCount", rentsCount);
        jsonObject.put("pageCount", pageCount);
        jsonObject.put("pageNum", dto.getPageNum());
        jsonObject.put("rents", rentDao.selectPageRentExcImages(dto));
        return jsonObject;
    }

    @Override
    public Rent queryRentById(RentDto dto) {
        String tableName = CrawlerUtil.getExtra(dto.getDataSource()).getRentTableName();
        dto.setTableName(tableName);
        return rentDao.selectRentIncImagesById(dto);
    }

    @Override
    public JSONObject queryRentByKey(Rent rent, String dataSource) {
        JSONObject jsonObject = new JSONObject();
        String tableName = CrawlerUtil.getExtra(dataSource).getRentTableName();
        jsonObject.put("rents", rentDao.selectRentExcImagesByKey(rent, tableName));
        return jsonObject;
    }

    @Override
    public Integer queryAllCount(String dataSource) {
        String tableName = CrawlerUtil.getExtra(dataSource).getRentTableName();
        return rentDao.selectAllCountByTableName(tableName);
    }

}
