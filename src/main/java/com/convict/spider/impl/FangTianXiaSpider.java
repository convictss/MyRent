package com.convict.spider.impl;

import com.convict.dto.RentDto;
import com.convict.model.enums.ListUrlEnum;
import com.convict.model.enums.TableEnum;
import com.convict.po.Rent;
import com.convict.spider.SpiderEntrance;
import com.convict.util.CommonUtil;
import com.convict.util.FileUtil;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FangTianXiaSpider extends SpiderEntrance {

    @Override
    public void process(Page page) {
        String curUrl = page.getUrl().toString();
        Html html = page.getHtml();
        try {
            if (curUrl.startsWith(ListUrlEnum.FangTianXia.getValue())) {

                List<String> links = html.xpath("//dl[@class='list hiddenMap rel']/dt/a").links().all();
                for (String link : links) {
                    if (rentDao.selectCountByLink(link, TableEnum.FangTianXia.getValue()) < 1) {
                        page.addTargetRequest(link);
                    }
                }
                // 下一页链接
                if (html.xpath("//div[@class='fanye']").get().contains("下一页")) {
                    this.context.setPageNum(this.context.getPageNum() + 1); // 设置线程的下一页
                    this.context.setPageCount(this.context.getPageCount() + 1); // 设置已爬页数
                    page.addTargetRequest(ListUrlEnum.FangTianXia.getValue() + this.context.getPageNum());
                }

            } else {
                String title = html.xpath("//h1[@class='title']/text()").get();
                String region = html.xpath("//a[@id='agantzfxq_C02_08']/text()").get();
                String community = html.xpath("//a[@id='agantzfxq_C02_07']/text()").get();

                List<String> rcont = html.xpath("//div[@class='rcont']/a/text()").all();
                String address = rcont.get(rcont.size() - 1);
                String area = html.xpath("//div[@class='trl-item1 w132']/div/text()").get().replace("平米", "");
                String houseType = html.xpath("//div[@class='trl-item1 w182']/div/text()").get();

                String trafficRoute = null;
                String trTemp = rcont.get(rcont.size() - 2);
                if (trTemp.contains("距") && trTemp.contains("米")) {
                    trafficRoute = rcont.get(rcont.size() - 2);
                }
                Elements floorEles = html.getDocument().select("div[class='trl-item1 w182']").get(1).select("div");
                String floor1 = floorEles.get(1).text();
                String floor2 = floorEles.get(2).text().replace("楼层", "");
                String floor = floor1 + floor2;

                String priceStr = html.xpath("//div[@class='trl-item sty1']/i/text()").get();
                Integer price = Integer.parseInt(priceStr);

                String dpsTypeTemp = html.xpath("//div[@class='trl-item sty1']/text()").get();
                Pattern dpsTypePat = Pattern.compile("[（](.*)[）]");
                Matcher dpsTypeMat = dpsTypePat.matcher(dpsTypeTemp);
                String depositType = null;
                if (dpsTypeMat.find()) {
                    depositType = dpsTypeMat.group(1);
                }
                String contact = html.xpath("//span[@class='zf_jjname']/a/text()").get();
                String phoneNumber = html.xpath("//p[@class='text_phone']/text()").get();

                Rent rent = new Rent(curUrl, title, region, community, address, area, houseType, floor, price, depositType, contact, phoneNumber, trafficRoute);

                String pdTemp = html.xpath("//p[@class='gray9 fybh-zf']/span[2]").get();
                String publishDate = null;
                Pattern pdCompile = Pattern.compile("\\d{4}(\\-|\\/|.)\\d{1,2}\\1\\d{1,2}");
                Matcher pdMatcher = pdCompile.matcher(pdTemp);
                if (pdMatcher.find()) {
                    publishDate = pdMatcher.group(0);
                }
                rent.setPublishDate(CommonUtil.str2Date(publishDate, "yyyy-MM-dd"));

                // 保存数据表，图片表
                rentDao.insert(rent, TableEnum.FangTianXia.getValue());

                // 是否下载图片
                if (super.context.getImgDown()) {
                    List<String> imageUrls = html.xpath("//div[@class='bigImg']/img/@src").all();

                    // 设置rent的image
                    if (imageUrls != null && imageUrls.size() != 0) {

                        List<String> images = new ArrayList<>();

                        // 设置照片路径的前置参数
                        String param = curUrl.substring(curUrl.lastIndexOf("/") + 1, curUrl.lastIndexOf("."));
                        String imagePrefix = "fangtianxia/" + param + "/";

                        for (int i = 0; i < imageUrls.size(); i++) {
                            imageUrls.set(i, "https:" + imageUrls.get(i));
                            images.add(imagePrefix + i + ".jpg");
                        }

                        // 设置图片物理路径前置参数
                        String webPath = super.webPath.getPath();
                        String filePathPrefix = webPath + "\\\\static\\\\image\\\\crawl\\\\" + imagePrefix.replace("/", "\\\\");
                        FileUtil.downloadFileByUrls(imageUrls, filePathPrefix);
                        rentDao.insertImages(new RentDto(rent.getId(), TableEnum.FangTianXiaImage.getValue(), images));
                    }

                }
            }
        } catch (Exception e) {
            this.stopSpider();
            this.context.getSpiderLog().setSpiderStatus(0);
            this.context.getSpiderLog().setSpiderStatusMessage(CommonUtil.formatException(e, curUrl));
        }
    }

}
