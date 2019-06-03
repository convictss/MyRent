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
import com.convict.util.site.FiveEightTongChengUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
public class GanJiWangSpider extends SpiderEntrance {

    @Override
    public void process(Page page) {
        String curUrl = page.getUrl().toString();
        Html html = page.getHtml();
        try {
            if (html.xpath("//title").get().contains("请输入验证码")) {
                this.stopSpider();
                this.context.getSpiderLog().setSpiderStatus(0);
                this.context.getSpiderLog().setSpiderStatusMessage("出现验证码！");

            } else if (curUrl.startsWith(ListUrlEnum.GanJiWang.getValue())) {
                List<String> links = html.xpath("//dd[@class='dd-item title']/a").links().all();
                for (String link : links) {
                    // 清除无用链接、跳转到58同城的链接
                    if (link.contains("http://gz.ganji.com/fanggongyu") || link.contains("//jxjump.58.com")) {
                        continue;
                    }
                    // 保证清除上面无用链接不可以用else if
                    if (rentDao.selectCountByLink(link, TableEnum.GanJiWang.getValue()) < 1) {
                        if (link.contains("cookie=")) {
                            link = link.substring(0, link.lastIndexOf("&apptype="));
                        }
                        page.addTargetRequest(link);
                    }
                }
                // 下一页链接
                if (html.xpath("//a[@class='next']").get() != null) {
                    this.context.setPageNum(this.context.getPageNum() + 1); // 设置线程的下一页
                    this.context.setPageCount(this.context.getPageCount() + 1); // 设置已爬页数
                    page.addTargetRequest(ListUrlEnum.GanJiWang.getValue() + context.getPageNum());
                }
            } else {
                String title = html.xpath("//p[@class='card-title']/i/text()").get();

                Document document = html.getDocument();
                Elements h_a_f = document.select("li[class='item f-fl']");
                String houseType = h_a_f.get(0).select("span").get(1).text();
                String areaTemp = h_a_f.get(1).select("span").get(1).text();
                String area = areaTemp.substring(areaTemp.lastIndexOf(" ") + 1).replace("㎡", "");
                String floor = h_a_f.get(3).select("span").get(1).text();

                Elements c_r_addr = document.select("li[class='er-item f-fl']");
                Element cEle = c_r_addr.select("span[class='content']").get(0);
                String community;
                if (cEle.select("a") != null && !cEle.select("a").text().equals("")) {
                    community = cEle.select("a > span").text();
                } else {
                    community = cEle.select("span > span").text();
                }

                String regionAndAddress = c_r_addr.select("span[class='content']").get(1).text();
                String region = "";
                String address = "";
                Pattern ra = Pattern.compile("(.*?) - (.*)");
                Matcher m = ra.matcher(regionAndAddress);
                if (m.find()) {
                    region = m.group(1);
                    address = m.group(2);
                }

                String priceStr = html.xpath("//span[@class='price']/text()").get();
                Integer price;

                String depositType = html.xpath("//span[@class='unit']/text()").get();

                String contact = html.xpath("//a[@class='name']/text()").get();

                String phoneNumber = html.xpath("//a[@class='phone_num js_person_phone']/text()").get();

                String trafficRoute = html.xpath("//div[@class='subway-wrap']/span/text()").get();

                // 判断是否数字加密
                if (html.toString().contains("fangchan")) {
                    String base64Str = null;
                    Pattern compile = Pattern.compile("(base64,)(.*?)(')");
                    Matcher matcher = compile.matcher(html.toString());
                    if (matcher.find()) {
                        base64Str = matcher.group(2);
                    }
                    houseType = FiveEightTongChengUtil.decodeByBase64(base64Str, houseType);
                    area = FiveEightTongChengUtil.decodeByBase64(base64Str, area);
                    floor = FiveEightTongChengUtil.decodeByBase64(base64Str, floor);
                    priceStr = FiveEightTongChengUtil.decodeByBase64(base64Str, priceStr);
                    price = Integer.parseInt(priceStr);
                    depositType = FiveEightTongChengUtil.decodeByBase64(base64Str, depositType);
                    contact = FiveEightTongChengUtil.decodeByBase64(base64Str, contact);
                    phoneNumber = FiveEightTongChengUtil.decodeByBase64(base64Str, phoneNumber);

                } else {
                    price = Integer.parseInt(priceStr);
                }

                Rent rent = new Rent(curUrl, title, region, community, address, area, houseType, floor, price, depositType, contact, phoneNumber, trafficRoute);

                String pdTemp = html.xpath("//li[@class='date']/text()").get();
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
                rentDao.insert(rent, TableEnum.GanJiWang.getValue());
                // 是否下载图片
                if (super.context.getImgDown()) {
                    List<String> imageUrls = html.xpath("//li[@class='small-item']/a/img/@src").all();
                    List<String> images = new ArrayList<>();
                    String webPath = super.webPath.getPath();

                    // 设置image
                    for (int i = 0; i < imageUrls.size(); i++) {
                        String imageUrl = "http:" + imageUrls.get(i);
                        Pattern compile = Pattern.compile("(.*)w=(.*)h=(.*)");
                        Matcher ma = compile.matcher(imageUrl);
                        if (ma.find()) {
                            imageUrl = ma.group(1) + "w=540&h=405&crop=1";
                        }
                        StringBuffer imagePrefix = new StringBuffer("ganjiwang/");
                        if (curUrl.startsWith("http://gz.ganji.com")) {
                            String urlId = curUrl.substring(curUrl.indexOf("zufang/") + 7, curUrl.indexOf(".shtml"));
                            imagePrefix.append("gz/").append(urlId);

                        }
                        StringBuffer image = new StringBuffer(imagePrefix);
                        image.append("/").append(i).append(".jpg");
                        images.add(image.toString());
                        // 下载图片
                        String filePath = webPath + "\\\\static\\\\image\\\\crawl\\\\" + imagePrefix.toString().replace("/", "\\\\");
                        FileUtil.downloadFileByUrl(imageUrl, filePath, String.valueOf(i));
                    }
                    rentDao.insertImages(new RentDto(rent.getId(), TableEnum.GanJiWangImage.getValue(), images));
                }
            }

        } catch (Exception e) {
            this.stopSpider();
            this.context.getSpiderLog().setSpiderStatus(0);
            this.context.getSpiderLog().setSpiderStatusMessage(CommonUtil.formatException(e, curUrl));
        }
    }

}
