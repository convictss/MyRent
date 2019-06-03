package com.convict;

import com.convict.po.Rent;
import com.convict.util.FileUtil;
import org.json.JSONObject;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestLianJia implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setTimeOut(1000).setSleepTime(1000)
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
    private static String listUrl = "https://gz.lianjia.com/zufang/pg";
    private static Spider spider;
    private static Integer pageNum = 1;
    private static Integer pageCount = 17;
    private static Request request;
    private static JSONObject params = new JSONObject();
    private Pattern areaPat = Pattern.compile("(\\d+-?\\d+)㎡");
    private Pattern houseTypePat = Pattern.compile("\\d+室\\d+厅\\d+卫");

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        String curUrl = page.getUrl().toString();
        Html html = page.getHtml();

//        String c1 = "getPhoneParam:(\\{.*?}),";
//        String c2 = "prop_id:'(.*?)',[\\s\\S]*broker_id:'(.*?)'[\\s\\S]*token: *'(.*?)',";
//        Pattern pattern = Pattern.compile(c2);
//        Matcher matcher = pattern.matcher(html.toString());
//        if (matcher.find()) {
//            String prop_id = matcher.group(1);
//            String broker_id = matcher.group(2);
//            String token = matcher.group(3);
//
//            System.out.println(prop_id);
//            System.out.println(broker_id);
//            System.out.println(token);
//        }

//        Matcher matcher1 = Pattern.compile("token: '[\\s\\S]*prop_city_id").matcher(html.toString());
//        if (matcher1.find()) {
//            System.out.println(matcher1.group(0));
//        }

        System.out.println(page.getStatusCode());
        System.out.println(html.xpath("//title/text()").get());

    }


    public static void main(String[] args) {
        spider = Spider.create(new TestLianJia());
        String url = "https://gz.zu.anjuke.com/fangyuan/p1";
        spider.addUrl(url).thread(1).run();
    }

}