package com.convict.spider.impl;

import com.convict.dto.RentDto;
import com.convict.model.enums.ListUrlEnum;
import com.convict.model.enums.TableEnum;
import com.convict.po.Rent;
import com.convict.spider.SpiderEntrance;
import com.convict.util.CommonUtil;
import com.convict.util.FileUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BaiXingWangSpider extends SpiderEntrance {

    @Override
    public void process(Page page) {
        String curUrl = page.getUrl().toString();
        Html html = page.getHtml();
        try {
            if (curUrl.startsWith(ListUrlEnum.BaiXingWang.getValue())) {
                List<String> links = html.xpath("//ul[@class='list-ad-items has-melior-fang']/li/a").links().all();
                for (String link : links) {
                    if (rentDao.selectCountByLink(link, TableEnum.BaiXingWang.getValue()) < 1) {
                        page.addTargetRequest(link);
                    }
                }

                // 下一页链接
                if (html.xpath("//ul[@class='list-pagination']").get().contains("下一页")) {
                    this.context.setPageNum(this.context.getPageNum() + 1); // 设置线程的下一页
                    this.context.setPageCount(this.context.getPageCount() + 1); // 设置已爬页数
                    page.addTargetRequest(ListUrlEnum.BaiXingWang.getValue() + this.context.getPageNum());
                }
            }
            // 没有成交
            else if (html.xpath("//div[@class='chengjiao-msg']").get() == null) {
                String title = html.xpath("//div[@class='viewad-title']/h1/text()").get();

                String region = html.xpath("//*[@class='meta-address']/a/text()").get();

                String community = html.xpath("//*[@class='meta-小区名']/text()").get();

                String priceStr = html.xpath("//*[@class='meta-价格']/text()").get().replace("元", "");
                Integer price = Integer.parseInt(priceStr);

                String depositType = html.xpath("//*[@class='meta-付款方式']/text()").get().replace("元", "");

                String contact = html.xpath("//a[@class='poster-name']/text()").get();

                String phoneNumber = html.xpath("//p[@id='mobileNumber']/strong/text()").get();

                Elements eles = html.getDocument().select("div[class='viewad-meta2-item']");
                String houseType = null;
                String area = null;
                String floor = null;
                String address = null;
                String trafficRoute = null;
                for (int i = 0; i < eles.size(); i++) {
                    Element ele = eles.get(i);
                    String eleStr = ele.toString();
                    if (eleStr.contains("房间类型") || eleStr.contains("房型")) {
                        houseType = ele.select("label").get(1).text();
                    } else if (eleStr.contains("租房类型")) {
                        houseType = ele.select("a").text();
                    }

                    if (eleStr.contains("面积")) {
                        area = ele.select("label").get(1).text().replace("平米", "");
                    }

                    if (eleStr.contains("楼层概况")) {
                        floor = ele.select("label").get(1).text().replace(" ", "");
                    }

                    if (eleStr.contains("具体地点")) {
                        address = ele.select("label").get(1).text();
                    }

                    if (eleStr.contains("临近地铁线")) {
                        StringBuffer trTemp = new StringBuffer();
                        Elements as = ele.select("a");
                        for (Element a : as) {
                            trTemp.append(a.attr("title")).append(",");
                        }
                        if (trafficRoute == null) {
                            trafficRoute = trTemp.toString();
                        } else {
                            trafficRoute += trTemp.toString();
                        }
                    }

                    if (eleStr.contains("临近地铁站")) {
                        StringBuffer trTemp = new StringBuffer();
                        Elements as = ele.select("a");
                        for (Element a : as) {
                            trTemp.append(a.attr("title")).append(",");
                        }
                        if (trafficRoute == null) {
                            trafficRoute = trTemp.toString();
                        } else {
                            trafficRoute += trTemp.toString();
                        }
                    }
                }

                // 去除trafficRoute最后的逗号
                if (trafficRoute != null && trafficRoute.endsWith(",")) {
                    trafficRoute = trafficRoute.substring(0, trafficRoute.length() - 1);
                }

                Rent rent = new Rent(curUrl, title, region, community, address, area, houseType, floor, price, depositType, contact, phoneNumber, trafficRoute);

                String pdTemp = html.xpath("//span[@data-toggle='tooltip']/@title").get();
                String publishDate = null;
                Pattern pdCompile = Pattern.compile("\\d+.*日");
                Matcher pdMatcher = pdCompile.matcher(pdTemp);
                if (pdMatcher.find()) {
                    publishDate = pdMatcher.group().replace("年" ,"-").replace("月", "-").replace("日", "");
                }
                rent.setPublishDate(CommonUtil.str2Date(publishDate, "yyyy-MM-dd"));

                // 保存数据表
                rentDao.insert(rent, TableEnum.BaiXingWang.getValue());

                // 是否下载图片
                if (super.context.getImgDown()) {
                    List<String> images = new ArrayList<>();
                    List<String> imageUrls = html.xpath("//a[@class='photo-grid-photo photo-trigger']/@style").all();

                    // 设置图片在数据库前置参数
                    String param = curUrl.substring(curUrl.lastIndexOf("/") + 1, curUrl.lastIndexOf(".html"));
                    String imagePrefix = "baixingwang/" + param + "/";

                    // 抽取有效url、设置rent的image
                    Pattern compile = Pattern.compile("url\\((.*?)\\)");
                    for (int i = 0; i < imageUrls.size(); i++) {
                        String temp = imageUrls.get(i);
                        Matcher m = compile.matcher(temp);
                        if (m.find()) {
                            imageUrls.set(i, m.group(1));
                            images.add(imagePrefix + i + ".jpg");
                        }
                    }

                    // 设置图片物理路径前置参数
                    String webPath = super.webPath.getPath();
                    String filePathPrefix = webPath + "\\\\static\\\\image\\\\crawl\\\\" + imagePrefix.replace("/", "\\\\");
                    FileUtil.downloadFileByUrls(imageUrls, filePathPrefix);
                    rentDao.insertImages(new RentDto(rent.getId(), TableEnum.BaiXingWangImage.getValue(), images));
                }
            }
        } catch (Exception e) {
            this.stopSpider();
            this.context.getSpiderLog().setSpiderStatus(0);
            this.context.getSpiderLog().setSpiderStatusMessage(CommonUtil.formatException(e, curUrl));
        }
    }

}
