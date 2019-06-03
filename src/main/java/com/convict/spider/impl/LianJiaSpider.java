package com.convict.spider.impl;

import com.convict.dto.RentDto;
import com.convict.model.enums.ListUrlEnum;
import com.convict.model.enums.TableEnum;
import com.convict.po.Rent;
import com.convict.spider.SpiderEntrance;
import com.convict.util.CommonUtil;
import com.convict.util.CrawlerUtil;
import com.convict.util.FileUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LianJiaSpider extends SpiderEntrance {

    private static Pattern areaPat = Pattern.compile("(\\d+-?\\d+)㎡");
    private static Pattern houseTypePat = Pattern.compile("\\d+室\\d+厅\\d+卫");

    @Override
    public void process(Page page) {
        String curUrl = page.getUrl().toString();
        Html html = page.getHtml();
        try {
            if (curUrl.startsWith(ListUrlEnum.LianJia.getValue())) {
                List<String> divs = html.xpath("//div[@class='content__list--item']").all();
                for (String divStr : divs) {
                    Html div = new Html(divStr);
                    String title = div.xpath("//div[@class='content__list--item--main']/p/a/text()").get().trim();
                    String community;
                    if (title.contains(" ")) {
                        community = title.substring(0, title.indexOf(" "));
                    } else {
                        community = title;
                    }
                    String tmp = div.xpath("//p[@class='content__list--item--des']/text()").get();
                    String area = null;
                    String houseType = null;
                    String address = null;
                    String region = null;
                    String priceStr;
                    Integer price;
                    Matcher areaMat = areaPat.matcher(tmp);
                    if (areaMat.find()) {
                        area = areaMat.group(1);
                    }
                    Matcher houseTypeMat = houseTypePat.matcher(tmp);
                    if (houseTypeMat.find()) {
                        houseType = houseTypeMat.group(0);
                    }
                    priceStr = div.xpath("//em/text()").get();
                    if (priceStr.contains("-")) {
                        priceStr = priceStr.substring(0, priceStr.indexOf("-"));
                    }
                    price = Integer.parseInt(priceStr);

                    // 租房类型，在列表获取区域、地址
                    String link = div.xpath("//p[@class='content__list--item--title twoline']/a").links().get();
                    link = "https://gz.lianjia.com" + link;
                    if (link.contains("zufang")) {
                        region = div.xpath("//p[@class='content__list--item--des']/a/text()").get();
                        address = div.xpath("//p[@class='content__list--item--des']/a[2]/text()").get();
                    }

                    if (rentDao.selectCountByLink(link, TableEnum.LianJia.getValue()) < 1) {
                        Rent rent = new Rent();
                        rent.setLink(link);
                        rent.setTitle(title);
                        rent.setRegion(region);
                        rent.setCommunity(community);
                        rent.setAddress(address);
                        rent.setArea(area);
                        rent.setHouseType(houseType);
                        rent.setPrice(price);
                        this.context.getDetailsMap().put(link, rent);
                        page.addTargetRequest(link);
                    }
                }

            }

            // 第二次跳转--租房类型获取联系方式时才请求到此代码块
            else if (curUrl.endsWith("ZuFangGetPhone")) {
                Rent rent = this.context.getDetailsMap().get(curUrl);
                this.context.getDetailsMap().remove(curUrl);
                Pattern phonePat = Pattern.compile("tp_number\":\"(\\d+,\\d+)\",");
                Matcher phoneMat = phonePat.matcher(page.getJson().toString());
                String phoneNumber = null;
                if (phoneMat.find()) {
                    phoneNumber = phoneMat.group(1).replace(",", "转");
                }
                // 在此处更新 租房类型 的联系电话
                String tableName = CrawlerUtil.getExtra("链家").getRentTableName();
                rent.setPhoneNumber(phoneNumber);
                rentDao.update(rent, tableName);
            }

            // 第一次跳转--详情页
            else {
                Rent rent = this.context.getDetailsMap().get(curUrl);
                this.context.getDetailsMap().remove(curUrl);

                // 租房类型
                if (curUrl.contains("zufang")) {
                    Document document = html.getDocument();

                    // 楼层
                    Elements floors = document
                            .select("div[class=content__article fl]")
                            .select(".content__article__info")
                            .select("li");
                    for (Element li : floors) {
                        if (li.text().contains("楼层")) {
                            String line = li.text();
                            String floor = line.substring(line.indexOf("：") + 1);
                            rent.setFloor(floor);
                            break;
                        }
                    }
                    // 发布时间
                    String pdTemp = html.xpath("//div[@class='content__subtitle']/text()").get();
                    Pattern pdPat = Pattern.compile("\\d{4}(\\-|\\/|.)\\d{1,2}\\1\\d{1,2}");
                    Matcher pdMat = pdPat.matcher(pdTemp);
                    if (pdMat.find()) {
                        Date publishDate = CommonUtil.str2Date(pdMat.group(), "yyyy-MM-dd");
                        rent.setPublishDate(publishDate);
                    }
                    // 租房方式
                    String depositType = html.xpath("//p[@class='content__article__table']/span/text()").get();
                    rent.setDepositType(depositType);

                    // 交通方式
                    Elements trs = document.select(".content__article__info4 > ul > li");
                    StringBuffer trb = new StringBuffer();
                    for (Element tr : trs) {
                        Elements spans = tr.select("span");
                        trb.append("距离 " + spans.get(0).text() + " " + spans.get(1).text() + ", ");
                    }
                    String trafficRoute = trb.substring(0, trb.length() - 2);
                    rent.setTrafficRoute(trafficRoute);

                    // 联系人、联系电话
                    String contact = html.xpath("//div[@class='content__aside__list--title oneline']/span[@class='contact_name']/@title").get();

                    // 平台客服，联系电话在页面上就可找到，因此不需要再次请求
                    if (contact == null || contact == "") {
                        contact = html.xpath("//span[@class='contact_name']/text()").get();
                        rent.setContact(contact);
                        String phone = html.xpath("//p[@class='content__aside__list--bottom oneline']/text()").get();
                        rent.setPhoneNumber(phone);
                        rentDao.insert(rent, TableEnum.LianJia.getValue());
                    }

                    // 个人型，联系电话需要再次请求
                    else {
                        rent.setContact(contact);
                        rentDao.insert(rent, TableEnum.LianJia.getValue());

                        String hc = html.xpath("//i[@class='house_code']/text()").get();
                        String houseCode = hc.substring(hc.indexOf("：") + 1);
                        String ucid = html.xpath("//span[@class='contact__im']/@data-im_id").get();
                        String phoneUrl = "https://gz.lianjia.com/zufang/aj/house/brokers?house_codes=" + houseCode
                                + "&position=bottom&ucid=" + ucid + "&ZuFangGetPhone";

                        // 添加获取联系电话的请求，实体在下一个请求更新联系电话
                        this.context.getDetailsMap().put(phoneUrl, rent);
                        page.addTargetRequest(phoneUrl);
                    }

                }

                // 公寓类型，此类型没有[楼层]、[押金类型]、[交通路线]、[发布日期]
                else if (curUrl.contains("apartment")) {
                    String regionTemp = html.xpath("//p[@class='bread__nav__wrapper oneline']/a[2]/text()").get();
                    String region = regionTemp.replace("租房", "");
                    String address = html.xpath("//p[@class='flat__info--subtitle online']/text()").get().trim();
                    String contact = html.xpath("//span[@class='contact_name']/text()").get();
                    String phoneNumber = html.xpath("//p[@class='content__aside__list--bottom oneline']/text()").get();
                    String trTemp = html.xpath("//p[@class='flat__info--description threeline']/text()").get();
                    Pattern trPat = Pattern.compile("【位置】(.*?)<br");
                    Matcher trMat = trPat.matcher(trTemp);
                    if (trMat.find()) {
                        String trafficRoute = trMat.group(1);
                        rent.setTrafficRoute(trafficRoute);
                    }
                    rent.setRegion(region);
                    rent.setAddress(address);
                    rent.setContact(contact);
                    rent.setPhoneNumber(phoneNumber);
                    rentDao.insert(rent, TableEnum.LianJia.getValue());
                }

                // 是否下载图片
                if (super.context.getImgDown()) {
                    List<String> imageUrls = html.xpath("//div[@class='content__article__slide__item']/img/@data-src").all();
                    List<String> images = new ArrayList<>();

                    // 设置照片路径的前置参数
                    String param = curUrl.substring(curUrl.lastIndexOf(".com/") + 5, curUrl.lastIndexOf(".html"));
                    String imagePrefix = "lianjia/" + param + "/";

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
                    rentDao.insertImages(new RentDto(rent.getId(), TableEnum.LianJiaImage.getValue(), images));

                }

            }
        } catch (Exception e) {
            this.stopSpider();
            this.context.getSpiderLog().setSpiderStatus(0);
            this.context.getSpiderLog().setSpiderStatusMessage(CommonUtil.formatException(e, curUrl));
        }
    }

}
