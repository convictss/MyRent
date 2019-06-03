package com.convict.spider.impl;

import com.convict.dto.RentDto;
import com.convict.model.WebPath;
import com.convict.model.enums.ListUrlEnum;
import com.convict.model.enums.TableEnum;
import com.convict.po.Rent;
import com.convict.spider.SpiderEntrance;
import com.convict.util.CommonUtil;
import com.convict.util.FileUtil;
import com.convict.util.SpringContextUtil;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LeYouJiaSpider extends SpiderEntrance {

    @Override
    public void process(Page page) {
        String curUrl = page.getUrl().toString();
        Html html = page.getHtml();
        try {
            if (curUrl.startsWith(ListUrlEnum.LeYouJia.getValue())) {
                List<String> links = html.xpath("//p[@class='tit']/a").links().all();
                for (String link : links) {
                    if (rentDao.selectCountByLink(link, TableEnum.FangTianXia.getValue()) < 1) {
                        page.addTargetRequest(link);
                    }
                }

                // 下一页链接
                if (html.xpath("//div[@class='jjs-new-page fr']").get().contains("下一页")) {
                    this.context.setPageNum(this.context.getPageNum() + 1); // 设置线程的下一页
                    this.context.setPageCount(this.context.getPageCount() + 1); // 设置已爬页数
                    page.addTargetRequest(ListUrlEnum.LeYouJia.getValue() + this.context.getPageNum());
                }
            } else {
                String title = html.xpath("//h1[@class='tit-conno fl']/text()").get();

                String region = html.xpath("//span[@class='location mr10 fl']/a[1]/text()").get();

                String community = html.xpath("//span[@class='xqname fl']/a/text()").get();

                String address = html.xpath("//span[@class='location mr10 fl']/a[2]/text()").get().trim();

                String houseType = html.xpath("//div[@class='intro-box2 clearfix']/span[1]/text()").get();

                String area = html.xpath("//div[@class='intro-box2 clearfix']/span[2]/text()").get().replace("㎡", "");

                List<String> floorTemp = html.xpath("//em[@class='ml20 c333']/text()").all();
                String floor = floorTemp.get(2);

                String priceStr = html.xpath("//em[@class='total']/text()").get();
                Integer price = Integer.parseInt(priceStr);

                String dpsTypeTemp = html.xpath("//p[@class='price-2']/text()").get();
                String depositType = dpsTypeTemp.substring(dpsTypeTemp.lastIndexOf("押"));

                List<String> contactTemp = html.xpath("//span[@class='name']/text()").all();
                String contact = contactTemp.get(2);

                String phoneNumber = html.xpath("span[@class='telnum']/text()").get();

                Rent rent = new Rent(curUrl, title, region, community, address, area, houseType, floor, price, depositType, contact, phoneNumber);

                String pdTemp = html.xpath("//em[@class='tip']/text()").get();
                String publishDate = null;
                Pattern pdCompile = Pattern.compile("\\d+.*");
                Matcher pdMatcher = pdCompile.matcher(pdTemp);
                if (pdMatcher.find()) {
                    publishDate = pdMatcher.group();
                }
                rent.setPublishDate(CommonUtil.str2Date(publishDate, "yyyy-MM-dd"));
                // 保存数据表
                rentDao.insert(rent, TableEnum.LeYouJia.getValue());

                // 是否下载图片
                if (super.context.getImgDown()) {
                    List<String> imageUrls = html.xpath("//div[@data-title='房源图片']/div/@data-src").all();
                    List<String> images = new ArrayList<>();
                    // 设置照片路径的前置参数
                    String param = curUrl.substring(curUrl.lastIndexOf("/") + 1, curUrl.lastIndexOf("."));
                    String imagePrefix = "leyoujia/" + param + "/";

                    // 设置rent的image
                    if (imageUrls != null && imageUrls.size() != 0) {
                        for (int i = 0; i < imageUrls.size(); i++) {
                            images.add(imagePrefix + i + ".jpg");
                        }
                    }

                    // 设置图片物理路径前置参数
                    String webPath = super.webPath.getPath();
                    String filePathPrefix = webPath + "\\\\static\\\\image\\\\crawl\\\\" + imagePrefix.replace("/", "\\\\");
                    FileUtil.downloadFileByUrls(imageUrls, filePathPrefix);
                    rentDao.insertImages(new RentDto(rent.getId(), TableEnum.LeYouJiaImage.getValue(), images));
                }
            }
        } catch (Exception e) {
            this.stopSpider();
            this.context.getSpiderLog().setSpiderStatus(0);
            this.context.getSpiderLog().setSpiderStatusMessage(CommonUtil.formatException(e, curUrl));
        }
    }

}
