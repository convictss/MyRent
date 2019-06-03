package com.convict.spider.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.convict.dto.RentDto;
import com.convict.model.enums.ListUrlEnum;
import com.convict.model.enums.TableEnum;
import com.convict.po.Rent;
import com.convict.spider.SpiderEntrance;
import com.convict.util.CommonUtil;
import com.convict.util.FileUtil;
import com.convict.util.site.AnjukeUtil;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AnJuKeSpider extends SpiderEntrance {

    @Override
    public void process(Page page) {
        String curUrl = page.getUrl().toString();
        Html html = page.getHtml();
        try {
            if (curUrl.startsWith(ListUrlEnum.AnJuKe.getValue())) {
                List<String> links = html.xpath("//div[@class='zu-itemmod']/a").links().all();
                List<String> titles = html.xpath("//div[@class='zu-itemmod']/a/@title").all();
                List<String> prices = html.xpath("//div[@class='zu-side']/p/strong/text()").all();
                List<String> houseTypeAndAreaAndFloors = html.xpath("//div[@class='zu-info']/p[@class='details-item tag']/text()").all();
                List<String> regionAndAddresses = html.xpath("//div[@class='zu-info']/address[@class='details-item']/text()").all();
                List<String> communities = html.xpath("//div[@class='zu-info']/address[@class='details-item']/a/text()").all();
                for (int i = 0; i < links.size(); i++) {
                    String link = links.get(i);
                    String title = titles.get(i);
                    Integer price = Integer.parseInt(prices.get(i));

                    String houseTypeAndAreaAndFloor = houseTypeAndAreaAndFloors.get(i).trim();
                    String houseType = AnjukeUtil.getHouseTypeByStr(houseTypeAndAreaAndFloor);
                    String area = AnjukeUtil.getAreaByStr(houseTypeAndAreaAndFloor);
                    String floor = AnjukeUtil.getFloorByStr(houseTypeAndAreaAndFloor);

                    String regionAndAddress = regionAndAddresses.get(i).trim();
                    String region = AnjukeUtil.getRegionByStr(regionAndAddress);
                    String address = AnjukeUtil.getAddressByStr(regionAndAddress);
                    String community = communities.get(i);

                    if (rentDao.selectCountByLink(link, TableEnum.Anjuke.getValue()) < 1) {
                        Rent rent = new Rent(link, title, region, community, address, area, houseType, floor, price);
                        this.context.getDetailsMap().put(link, rent);
                        page.addTargetRequest(link);
                    }
                }

                // 下一页链接
                if (html.xpath("//a[@class='aNxt']").get() != null) {
                    this.context.setPageNum(this.context.getPageNum() + 1); // 设置线程的下一页
                    this.context.setPageCount(this.context.getPageCount() + 1); // 设置已爬页数
                    page.addTargetRequest(ListUrlEnum.AnJuKe.getValue() + this.context.getPageNum());
                }
            }

            else {
                Rent rent = this.context.getDetailsMap().get(curUrl);
                this.context.getDetailsMap().remove(curUrl);

                String depositType = html.xpath("//*[@class='full-line cf']/span[@class='type']/text()").get();

                String contact = html.xpath("//*[@class='broker-name']/@title").get();

                String phoneTemp = html.xpath("//div[@class='card-phone-click']/span/text()").get();

//                if (phoneTemp.contains("点击查看电话")) {
//                    // 单独请求
//                    rent.setPhoneNumber(phoneNumber);
//                } else {
//                    Pattern pnCompile = Pattern.compile("brokerPhone:'(.*?)',");
//                    Matcher pnMatcher = pnCompile.matcher(html.toString());
//                    if (pnMatcher.find()) {
//                        String phoneNumber = pnMatcher.group(1);
//                        rent.setPhoneNumber(phoneNumber);
//                    }
//                }

                String trafficRoute = html.xpath("//li[@class='title-label-item subway']/text()").get();

                String pdTemp = html.xpath("//div[@class='right-info']/text()").get();
                Pattern pdCompile = Pattern.compile("\\d+.*");
                Matcher pdMatcher = pdCompile.matcher(pdTemp);
                String publishDate = null;
                if (pdMatcher.find()) {
                    publishDate = pdMatcher.group().replace("年", "-").replace("月", "-").replace("日", "");
                }

                rent.setDepositType(depositType);
                rent.setContact(contact);
                rent.setPhoneNumber(phoneTemp);
                rent.setTrafficRoute(trafficRoute);
                rent.setPublishDate(CommonUtil.str2Date(publishDate, "yyyy-MM-dd"));

                // 保存数据表
                rentDao.insert(rent, TableEnum.Anjuke.getValue());

                // 是否下载图片
                if (super.context.getImgDown()) {
                    List<String> imageUrls = html.xpath("//*[@id='room_pic_wrap']/div[@class='img_wrap']/img/@data-src").all();
                    List<String> images = new ArrayList<>();

                    // 清除无效的imageUrl
                    for (int i = 0; i < imageUrls.size(); i++) {
                        String imageUrl = imageUrls.get(i);
                        if (imageUrl == null || imageUrl.equals("")) {
                            imageUrls.remove(i);
                        } else {
                            String imagePathPrev = "anjuke/" + curUrl.substring(curUrl.lastIndexOf("/") + 1) + "/";
                            images.add(imagePathPrev + i + ".jpg");
                        }
                    }
                    rent.setImages(images);
                    // 设置图片路径
//                    WebPath wp = SpringContextUtil.getBean("webPath");
                    String webPath = super.webPath.getPath();

                    String filePathBranch = curUrl.substring(curUrl.lastIndexOf("/") + 1);
                    // 下载图片
                    FileUtil.downloadFileByUrls(imageUrls, webPath + "\\\\static\\\\image\\\\crawl\\\\anjuke\\\\" + filePathBranch);
                    // 保存图片表
                    rentDao.insertImages(new RentDto(rent.getId(), TableEnum.AnjukeImage.getValue(), images));
                }

            }
        } catch (Exception e) {
            this.stopSpider();
            this.context.getSpiderLog().setSpiderStatus(0);
            this.context.getSpiderLog().setSpiderStatusMessage(CommonUtil.formatException(e, curUrl));
        }
    }

}
