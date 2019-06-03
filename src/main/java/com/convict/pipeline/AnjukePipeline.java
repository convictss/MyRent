package com.convict.pipeline;

import com.convict.dao.RentDao;
import com.convict.po.Rent;
import com.convict.model.enums.TableEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class AnjukePipeline implements Pipeline {

    @Autowired
    private RentDao rentDao;

    @Override
    public void process(ResultItems resultItems, Task task) {
        Rent anjuke = resultItems.get("anjuke");
        // 下载图片
//        String filePath =  "D:\\桌面[D盘]\\AnJuKe\\" + curUrl.substring(curUrl.lastIndexOf("/") + 1);
//                FileUtil.downloadFileByUrls(imagesUrl, CommonUtil.getServiceName(this.getClass().getSimpleName()), filePath);
//        FileUtil.downloadFileByUrls(imagesUrl, filePath);
        // 保存实体
        rentDao.insert(anjuke, TableEnum.valueOf("AnJuKe").getValue());

        System.out.println("insert finish id=" + anjuke.getId());


    }
}
