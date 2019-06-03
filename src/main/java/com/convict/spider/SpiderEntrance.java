package com.convict.spider;

import com.convict.dao.RentDao;
import com.convict.dao.SpiderLogDao;
import com.convict.model.CrawlContext;
import com.convict.model.WebPath;
import com.convict.po.SpiderLog;
import com.convict.util.CommonUtil;
import com.convict.util.CrawlerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author Convict
 * @Date 2019/1/20 21:43
 */
public abstract class SpiderEntrance implements PageProcessor {

    private Site site = Site.me()
            .setRetryTimes(3)
            .setSleepTime(1000)
            .setTimeOut(3000)
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");

    @Autowired
    protected WebPath webPath;

    @Autowired
    protected RentDao rentDao;

    @Autowired
    private SpiderLogDao spiderLogDao;

    // 爬虫对象
    private Spider spider;

    // 爬虫上下文：日志、当前页、页数、线程数、详情map
    protected CrawlContext context;

    public void startSpider(Spider spider, CrawlContext context) {
        this.context = context;
        this.spider = spider;
        SpiderLog log = context.getSpiderLog();
        log.setSpiderName(this.getClass().getSimpleName());
        log.setStartTime(new Date());

        spider.addUrl(CrawlerUtil.getExtra(log.getDataSource()).getListUrl() + 1);
        spider.thread(this.context.getThreadCount()).start();

        // 开启single线程池，保证爬虫线程池中线程停止后执行，插入爬虫日志
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        threadPool.submit(new Callable<Integer>() {
            public Integer call() {
                while (true) {
                    if ((spider != null && spider.getStatus().equals(Spider.Status.Stopped))) {
                        System.out.println("spider stop, insert log ====================================================");

                        if (log.getSpiderStatus() == null) {
                            log.setSpiderStatus(1);
                        }
                        log.setEndTime(new Date());
                        log.setCrawlSecond(CommonUtil.getTimeSpan(log.getStartTime(), log.getEndTime()));
                        log.setPageCount(context.getPageCount());
                        if (log.getSpiderStatus() == 1) {
                            log.setSpiderStatusMessage("爬取正常结束");
                        } else if (log.getSpiderStatus() == 2) {
                            log.setSpiderStatusMessage("被手动停止");
                        }
                        spiderLogDao.insertLog(log);
                        break;
                    }
                }
                return 0;
            }
        });
        if (threadPool != null) {
            threadPool.shutdown();
        }
    }

    public void stopSpider() {
        if (spider != null) {
            context.getSpiderLog().setSpiderStatus(2);
            spider.stop();
        }
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @Override
    public Site getSite() {
        return site;
    }
}
