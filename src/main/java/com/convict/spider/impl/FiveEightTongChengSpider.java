package com.convict.spider.impl;

import com.convict.dto.RentDto;
import com.convict.model.enums.ListUrlEnum;
import com.convict.model.enums.TableEnum;
import com.convict.po.Rent;
import com.convict.spider.SpiderEntrance;
import com.convict.util.CommonUtil;
import com.convict.util.FileUtil;
import com.convict.util.site.FiveEightTongChengUtil;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Convict
 * @Date 2019/1/20 19:47
 */
@Component
public class FiveEightTongChengSpider extends SpiderEntrance {

    @Override
    public void process(Page page) {
        String curUrl = page.getUrl().toString();
        Html html = page.getHtml();
        try {
            if (html.xpath("//title").get().contains("请输入验证码")) {
                this.stopSpider();
                this.context.getSpiderLog().setSpiderStatus(0);
                this.context.getSpiderLog().setSpiderStatusMessage("出现验证码！");

            } else if (curUrl.startsWith(ListUrlEnum.FiveEightTongCheng.getValue())) {
                List<String> links = html.xpath("//div[@class='img_list']/a").links().all();
                for (String link : links) {
                    if (rentDao.selectCountByLink(link, TableEnum.FiveEightTongCheng.getValue()) < 1) {
                        if (link.contains("cookie=")) {
                            link = link.substring(0, link.lastIndexOf("&apptype="));
                        }
//                        System.out.println(link);
                        page.addTargetRequest(link);
                    }
                }
                // 下一页链接
                if (html.xpath("//a[@class='next']").get() != null) {
                    this.context.setPageNum(this.context.getPageNum() + 1); // 设置线程的下一页
                    this.context.setPageCount(this.context.getPageCount() + 1); // 设置已爬页数
                    page.addTargetRequest(ListUrlEnum.FiveEightTongCheng.getValue() + context.getPageNum());
                }
            } else {
                // 只抓取不过期的房源
                if (!html.xpath("//div[@class='house-fraud-tip']").get().contains("抱歉，该房源已过期")) {

                    String base64Script = html.xpath("//script[1]").get();
                    String base64Str = null;
                    Pattern compile = Pattern.compile("(base64,)(.*?)(')");
                    Matcher matcher = compile.matcher(base64Script);
                    if (matcher.find()) {
                        base64Str = matcher.group(2);
                    }

                    String titleTemp = html.xpath("//div[@class='house-title']/h1/text()").get();
                    String title = FiveEightTongChengUtil.decodeByBase64(base64Str, titleTemp);

                    String region = html.xpath("//ul[@class='f14']/li[5]/span[2]/a/text()").get();

                    String communityTemp = html.xpath("//ul[@class='f14']/li[4]/span[2]/a/text()").get();
                    String community = FiveEightTongChengUtil.decodeByBase64(base64Str, communityTemp);

                    String addressTemp = html.xpath("//ul[@class='f14']/li[6]/span[2]/text()").get().trim();
                    String address = FiveEightTongChengUtil.decodeByBase64(base64Str, addressTemp);

                    String houseTypeAndArea = html.xpath("//span[@class='strongbox']/text()").get();
                    String houseTypeTemp = FiveEightTongChengUtil.getHouseTypeByStr(houseTypeAndArea);
                    String houseType = FiveEightTongChengUtil.decodeByBase64(base64Str, houseTypeTemp);

                    String areaTemp = FiveEightTongChengUtil.getAreaByStr(houseTypeAndArea);
                    String area = FiveEightTongChengUtil.decodeByBase64(base64Str, areaTemp);

                    String floorTemp = html.xpath("ul[@class='f14']/li[3]/span[2]/text()").get();
                    floorTemp = FiveEightTongChengUtil.getFloorByStr(floorTemp);
                    String floor = FiveEightTongChengUtil.decodeByBase64(base64Str, floorTemp);

                    String priceTemp = html.xpath("//b[@class='f36 strongbox']/text()").get();
                    String priceStr = FiveEightTongChengUtil.decodeByBase64(base64Str, priceTemp);
                    Integer price = Integer.parseInt(priceStr);

                    String depositType = html.xpath("//span[@class='c_333']/text()").get();

                    String contact = html.xpath("//a[@class='c_000']/text()").get();

                    String phoneNumberTemp = html.xpath("//span[@class='house-chat-txt']/text()").get();
                    String phoneNumber = FiveEightTongChengUtil.decodeByBase64(base64Str, phoneNumberTemp);

                    String trafficRoute = html.xpath("//*[@class='dt c_888 f12']/text()").get();

                    Rent rent = new Rent(curUrl, title, region, community, address, area, houseType, floor, price, depositType, contact, phoneNumber, trafficRoute);

                    String pdTemp = html.xpath("//p[@class='house-update-info c_888 f12']/text()").get().trim();
                    if (pdTemp.contains("天前")) {
                        int last = Integer.parseInt(pdTemp.substring(0, pdTemp.lastIndexOf("天前")));
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.DATE, - last);
                        rent.setPublishDate(calendar.getTime());
                    } else if (pdTemp.contains("小时前") || pdTemp.contains("分钟前")) {
                        rent.setPublishDate(new Date());
                    } else {
                        String publishDate = null;
                        Pattern pdCompile = Pattern.compile("\\d+-\\d+");
                        Matcher pdMatcher = pdCompile.matcher(pdTemp);
                        if (pdMatcher.find()) {
                            publishDate = "2019-" + pdMatcher.group();
                        }
                        rent.setPublishDate(CommonUtil.str2Date(publishDate, "yyyy-MM-dd"));
                    }


                    // 保存数据表
                    rentDao.insert(rent, TableEnum.FiveEightTongCheng.getValue());

                    // 是否下载图片
                    if (super.context.getImgDown()) {
                        List<String> imageUrls = html.xpath("//*[@id='leftImg']/li/img/@data-src").all();
                        List<String> images = new ArrayList<>();
                        String webPath = super.webPath.getPath();

                        // 设置rent的image
                        for (int i = 0; i < imageUrls.size(); i++) {
                            String imageUrl = imageUrls.get(i).replace("w=182&h=150", "");
                            StringBuffer imagePrefix = new StringBuffer("58tongcheng/");
                            String urlId;
                            if (curUrl.contains("//jxjump.58.com/service")) {
                                urlId = curUrl.substring(curUrl.lastIndexOf("pubid=") + 6);
                                imagePrefix.append("jxjump/").append(urlId);

                            } else if (curUrl.contains("//gz.58.com")) {
                                urlId = curUrl.substring(curUrl.lastIndexOf("/") + 1, curUrl.lastIndexOf("."));
                                imagePrefix.append("gz/").append(urlId);

                            } else if (curUrl.contains("//short.58.com")) {
                                urlId = curUrl.substring(curUrl.lastIndexOf("target=") + 7, curUrl.lastIndexOf("&end"));
                                imagePrefix.append("short/").append(urlId);
                            }
                            StringBuffer image = new StringBuffer(imagePrefix);
                            image.append("/").append(i).append(".jpg");
                            images.add(image.toString());

                            String filePath = webPath + "\\\\static\\\\image\\\\crawl\\\\" + imagePrefix.toString().replace("/", "\\\\");
                            FileUtil.downloadFileByUrl(imageUrl, filePath, String.valueOf(i));
                        }
                        rentDao.insertImages(new RentDto(rent.getId(), TableEnum.FiveEightTongChengImage.getValue(), images));
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
